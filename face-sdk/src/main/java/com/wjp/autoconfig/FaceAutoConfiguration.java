package com.wjp.autoconfig;

import com.wjp.autoconfig.properties.FaceProperties;
import com.wjp.autoconfig.template.FaceTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({
        FaceProperties.class
})
public class FaceAutoConfiguration {


    @Bean
    @ConditionalOnProperty(prefix = "face.config",value = "enable", havingValue = "true")
    public FaceTemplate faceTemplate(FaceProperties faceProperties){
        return new FaceTemplate(faceProperties);
    }
}
