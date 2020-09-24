package cn.xwplay.security.jwt.mapper;

import cn.xwplay.security.jwt.entity.AccountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper extends BaseMapper<AccountEntity> {
}
