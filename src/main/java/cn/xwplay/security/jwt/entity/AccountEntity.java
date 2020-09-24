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
@TableName("mast_account")
public class AccountEntity {

    @TableId
    @JsonSerialize(using = StringSerializer.class)
    private Long id;

    private String username;

    private String password;

    private Boolean locking;

    private Integer tryLoginCount;

    private Date lastLoginTime;

    private String nickname;

    private Boolean disabled;

    private Date updatePasswordTime;

}
