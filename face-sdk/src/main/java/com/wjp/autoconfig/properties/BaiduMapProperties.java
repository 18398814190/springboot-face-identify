package com.wjp.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "baidu.config")
public class BaiduMapProperties {
    private String ak;
}
