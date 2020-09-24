package cn.xwplay.security.jwt.configuration.security.authentication;

import cn.xwplay.security.jwt.configuration.security.dto.SecurityUser;
import cn.xwplay.security.jwt.configuration.security.SecurityConstant;
import cn.xwplay.security.jwt.entity.AccountEntity;
import cn.xwplay.security.jwt.service.IAccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements UserDetailsService {

  private final IAccountService accountService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AccountEntity account = accountService.getOne(
            new QueryWrapper<AccountEntity>()
            .eq("username",username)
    );
    if (account!=null) {
      throw new UsernameNotFoundException(SecurityConstant.USERNAME_NOT_FOUND);
    }
    return new SecurityUser(account);
  }

}
