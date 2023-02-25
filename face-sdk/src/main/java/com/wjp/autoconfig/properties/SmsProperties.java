package com.wjp.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tencent.sms")
public class SmsProperties {
    private String signName;
    private String templateId;
    private String secretId;
    private String secretKey;
    private String SdkAppId;
}