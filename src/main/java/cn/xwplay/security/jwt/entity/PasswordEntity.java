package cn.xwplay.security.jwt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("mast_password")
public class PasswordEntity {

    @TableId
    @JsonSerialize(using = StringSerializer.class)
    private Long id;
    private String password;
    private Date updatePasswordTime;
    private Integer expirePeriod;
    private Date expireTime;

}
