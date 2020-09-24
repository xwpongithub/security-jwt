package cn.xwplay.security.jwt.service.impl;

import cn.xwplay.security.jwt.entity.AccountTokenEntity;
import cn.xwplay.security.jwt.mapper.AccountTokenMapper;
import cn.xwplay.security.jwt.service.IAccountTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("accountTokenService")
public class AccountTokenServiceImpl extends ServiceImpl<AccountTokenMapper, AccountTokenEntity>
implements IAccountTokenService {
}
