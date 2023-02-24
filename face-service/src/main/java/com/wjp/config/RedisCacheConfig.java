package com.wjp.config;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;
import java.util.Map;

@Configuration
public class RedisCacheConfig {

    private static final Map<String, Duration> cacheMap;

    static {
        // 需要缓存过期的key，往这个map加
        cacheMap = ImmutableMap.<String, Duration>builder().put("videos", Duration.ofMinutes(1)).build();
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> {
            for (Map.Entry<String, Duration> entry : cacheMap.entrySet()) {
                builder.withCacheConfiguration(entry.getKey(),
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(entry.getValue()));
            }
        };
    }
}
