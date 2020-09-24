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
@TableName("mast_account_token")
public class AccountTokenEntity {

    @TableId
    @JsonSerialize(using = StringSerializer.class)
    private Long accountId;
    private String publicKey;
    private Date expireTime;

}
