package cn.xwplay.security.jwt.util;

import cn.hutool.core.codec.Base64;
import cn.xwplay.security.jwt.configuration.security.SecurityConstant;
import cn.xwplay.security.jwt.entity.AccountEntity;
import cn.xwplay.security.jwt.service.IAccountService;
import cn.xwplay.security.jwt.service.IAccountTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import org.springframework.security.authentication.LockedException;
import sun.security.ec.ECPublicKeyImpl;

import java.security.InvalidKeyException;
import java.security.Key;

class JwtEs512SignKeyResolver extends SigningKeyResolverAdapter {

    private final IAccountService accountService = SpringUtil.getBean(IAccountService.class);
    private final IAccountTokenService accountTokenService = SpringUtil.getBean(IAccountTokenService.class);

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
        String keyId = jwsHeader.getKeyId();
        AccountEntity account = accountService.getById(keyId);
        if (account.getLocking()) {
            throw new LockedException(SecurityConstant.ACCOUNT_LOCKED);
        }
        String strPubKey = accountTokenService.getById(keyId).getPublicKey();
        byte[] bytePublicKey = Base64.decode(strPubKey);
        try {
            return new ECPublicKeyImpl(bytePublicKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }
}