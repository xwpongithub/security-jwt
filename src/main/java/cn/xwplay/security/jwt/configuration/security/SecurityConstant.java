package cn.xwplay.security.jwt.configuration.security;

public interface SecurityConstant {

  String PROVIDER_NOT_FOUND = "provider not found";
  String USERNAME_NOT_FOUND = "username not found";
  String INCORRECT_PASSWORD = "incorrect password";
  String ACCOUNT_LOCKED = "account locked";
  String TOKEN_EXPIRED = "token expired";
  String INVALID_TOKEN = "invalid token";

  String TOKEN_AUDIENCE = "签发登录令牌";

}
