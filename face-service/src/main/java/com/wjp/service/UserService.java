package com.wjp.service;

import bean.vo.LoginVO;

public interface UserService {
    LoginVO login(String phone, String code);
}
