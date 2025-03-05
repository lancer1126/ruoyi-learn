package fun.lance.common.security.config;

import fun.lance.common.security.config.properties.OAuthProperties;
import fun.lance.common.security.utils.AuthRedisStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(OAuthProperties.class)
public class OAuthConfig {

    @Bean
    public AuthStateCache authStateCache() {
        return new AuthRedisStateCache();
    }
}
