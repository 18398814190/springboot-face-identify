package com.wjp.util;


import com.wjp.pojo.BaseUser;

/**
 * 登陆用户信息持有者
 * 通过ThreadLocal的形式，存储登陆用户的数据
 */
public class ThreadLocalUtils {

    private static ThreadLocal<BaseUser> userThreadLocal = new ThreadLocal<BaseUser>();

    /**
     * 向当前线程中存入用户数据
     * @param user
     */
    public static void setUser(BaseUser user){
        userThreadLocal.set(user);
    }

    /**
     * 从当前线程中获取用户数据
     * @return
     */
    public static BaseUser getUser(){
        return userThreadLocal.get();
    }

    /**
     * 获取登陆用户的id
     * @return
     */
    public static int getUserId(){
        return userThreadLocal.get().getId();
    }

    public static void remove(){
        userThreadLocal.remove();
    }
}