package com.lizishu.wechat.wechatpush.entity.user;

import lombok.Data;

/**
 * @author by lijiacheng
 * @date 2021/4/16.
 */
@Data
public class SysUser {
    //用户ID
    private Integer id;
    //用户名
    private String username;
    //联系方式
    private String phone;
    //微信的OpenID
    private String openId;
    //头像地址
    private String imgSrc;
}
