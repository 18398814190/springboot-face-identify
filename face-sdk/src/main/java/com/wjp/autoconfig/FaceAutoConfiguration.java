package com.wjp.autoconfig;

import com.wjp.autoconfig.properties.FaceProperties;
import com.wjp.autoconfig.properties.SmsProperties;
import com.wjp.autoconfig.template.FaceTemplate;
import com.wjp.autoconfig.template.SmsTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({
        FaceProperties.class,
        SmsProperties.class
})
public class FaceAutoConfiguration {


    @Bean
    @ConditionalOnProperty(prefix = "face.config",value = "enable", havingValue = "true")
    public FaceTemplate faceTemplate(FaceProperties faceProperties){
        return new FaceTemplate(faceProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "tencent.sms",value = "enable", havingValue = "true")
    public SmsTemplate smsTemplate(SmsProperties faceProperties){
        return new SmsTemplate(faceProperties);
    }
}
