package cn.xwplay.security.jwt.service.impl;

import cn.xwplay.security.jwt.entity.PasswordEntity;
import cn.xwplay.security.jwt.mapper.PasswordMapper;
import cn.xwplay.security.jwt.service.IPasswordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("passwordService")
public class PasswordServiceImpl extends ServiceImpl<PasswordMapper, PasswordEntity>
implements IPasswordService {
}
