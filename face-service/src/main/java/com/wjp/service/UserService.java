package com.wjp.service;

import bean.vo.LoginVo;

public interface UserService {
    LoginVo login(String phone, String code);
}
