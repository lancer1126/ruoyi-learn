package fun.lance.common.encrypt.config;

import fun.lance.common.encrypt.config.properties.ApiEncryptProperties;
import fun.lance.common.encrypt.filter.CryptoFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(ApiEncryptProperties.class)
public class ApiEncryptConfig {

    /**
     * 注册CryptoFilter过滤器
     */
    @Bean
    public FilterRegistrationBean<CryptoFilter> cryptoFilterRegistration(ApiEncryptProperties properties) {
        FilterRegistrationBean<CryptoFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CryptoFilter(properties));
        registration.addUrlPatterns("/*");
        registration.setName("cryptoFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return registration;
    }
}
