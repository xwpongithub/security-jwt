drop table if exists mast_account_token;
create table mast_account_token (
    account_id bigint comment '用户id',
    public_key text comment '公钥',
    expire_time datetime(3) comment '过期时间',
    primary key (account_id)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '账号令牌表' ROW_FORMAT = Dynamic;

drop table if exists mast_account;
create table mast_account (
     id bigint comment '用户id',
     username varchar(16) comment '用户名',
     nickname varchar(16) comment '昵称',
     password varchar(64) comment '密码',
     locking int(1) comment '是否被锁',
     try_login_count int(1) comment '登录重试次数',
     last_login_time datetime(3) comment '最后一次登录时间',
     disabled int(1) comment '账号是否被禁用',
     update_password_time datetime(3) comment '密码更新时间',
     primary key (id)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '账号表' ROW_FORMAT = Dynamic;

insert into mast_account values (1,'admin','电车男','$2a$10$hJCcutulLmFWJboIpMnG3Or5rCx70M6EX.1waro0N.bVTWHgGZ2I.',0,0,current_timestamp,0,current_timestamp);

DROP TABLE IF EXISTS mast_password;
CREATE TABLE mast_password  (
    id bigint COMMENT '主键',
    user_id varchar(16) COMMENT '用户id，用于与其他系统的用户关联使用',
    password varchar(100) COMMENT '用户本次设置的密码',
    update_password_time DATETIME(3) COMMENT '本次密码更新时间',
    expire_period INT(10) COMMENT '过期时间间隔，单位：天',
    expire_time DATETIME COMMENT '本次密码的过期时间',
    PRIMARY KEY (id),
    INDEX idx_user_id(user_id,password,update_password_time) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '账号密码表' ROW_FORMAT = Dynamic;