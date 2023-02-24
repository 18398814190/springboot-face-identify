package com.wjp.util;


import com.wjp.pojo.UserInfo;

/**
 * 登陆用户信息持有者
 * 通过ThreadLocal的形式，存储登陆用户的数据
 */
public class ThreadLocalUtils {

    private static ThreadLocal<UserInfo> userThreadLocal = new ThreadLocal<UserInfo>();

    /**
     * 向当前线程中存入用户数据
     * @param user
     */
    public static void setUser(UserInfo user){
        userThreadLocal.set(user);
    }

    /**
     * 从当前线程中获取用户数据
     * @return
     */
    public static UserInfo getUser(){
        return userThreadLocal.get();
    }

    /**
     * 获取登陆用户的id
     * @return
     */
    public static int getUserId(){
        return userThreadLocal.get().getUserId();
    }

    public static void remove(){
        userThreadLocal.remove();
    }
}