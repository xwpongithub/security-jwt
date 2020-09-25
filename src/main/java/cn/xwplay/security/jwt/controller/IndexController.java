package cn.xwplay.security.jwt.controller;

import cn.xwplay.security.jwt.common.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping
    public Response index() {
        return Response.ok();
    }

    @GetMapping("test1")
    public Response test1() {
        return Response.ok("test1");
    }

}
