package fun.lance.common.ratelimiter.config;

import fun.lance.common.ratelimiter.aspect.RateLimiterAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConfiguration;

@AutoConfiguration(after = RedisConfiguration.class)
public class RateLimiterConfig {

    /**
     * 因为是在夫父模块中定义的切面类，
     * 为了子模块能继承，需要手动注入Bean
     */
    @Bean
    public RateLimiterAspect rateLimiterAspect() {
        return new RateLimiterAspect();
    }
}
