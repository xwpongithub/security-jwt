package cn.xwplay.security.jwt.common;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

public final class Response extends HashMap<String,Object> {

  private static final long serialVersionUID = -6676367145436867669L;

  public static Response ok() {
    return ok("requestSuccess");
  }

  public static <T> Response ok(String msg, T data) {
    Response r = ok(msg);
    r.put("data", data);
    return r;
  }

  public static Response ok(String msg) {
    return putData(HttpStatus.OK.value(), msg);
  }


  public static <T> Response ok(T data) {
    Response r = ok();
    r.put("data", data);
    return r;
  }

  public static Response error() {
    return error("serverInternalError");
  }

  public static Response error(String msg) {
    return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
  }

  public static Response error(int code, String msg) {
    return putData(code, msg);
  }

  public static <T> Response error(int code, String msg,T data) {
    Response r = putData(code, msg);
    r.put("data",data);
    return r;
  }

  public static <T> Response error(int code,T data) {
    Response r = error(code,"服务器发生错误");
    r.put("data",data);
    return r;
  }

  public static <T> Response error(String msg,T data) {
    Response r = putData(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    r.put("data",data);
    return r;
  }

  private static Response putData(int code, String msg) {
    Response r = new Response();
    r.put("code", code);
    r.put("msg", msg);
    return r;
  }

  @Override
  public Response put(String key, Object value) {
    super.put(key, value);
    return this;
  }

}