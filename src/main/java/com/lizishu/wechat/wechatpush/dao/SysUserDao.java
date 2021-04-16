package com.lizishu.wechat.wechatpush.dao;

import com.lizishu.wechat.wechatpush.entity.user.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author by lijiacheng
 * @date 2021/4/16.
 */
@Mapper
public interface SysUserDao {

    /**
     *  新增用户
     * @param user 数据实体
     * */
    int add (SysUser user);

    /**
     *  跟进openid获取用户ID
     * @param openId openid
     * */
    int getUserId (String openId);
}
