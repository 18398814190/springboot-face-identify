package com.wjp.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "face.config")
public class FaceProperties {
    private String apiSecret;
    private String apiKey;
    private String baseUrl;
    private Integer confidence;
}