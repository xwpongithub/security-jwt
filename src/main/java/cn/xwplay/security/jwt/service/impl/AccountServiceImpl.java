package cn.xwplay.security.jwt.service.impl;

import cn.xwplay.security.jwt.entity.AccountEntity;
import cn.xwplay.security.jwt.mapper.AccountMapper;
import cn.xwplay.security.jwt.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("accountService")
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements IAccountService {
}
