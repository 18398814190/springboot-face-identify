package com.wjp.service;

import bean.dto.UserInfoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserInfoService {
    void loginReginfo(UserInfoDTO userInfoDTO);

    String uphead(MultipartFile headPhoto) throws IOException;
}
