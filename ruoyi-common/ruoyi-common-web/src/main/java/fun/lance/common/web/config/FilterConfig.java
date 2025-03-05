package fun.lance.common.web.config;

import fun.lance.common.web.config.properties.XssProperties;
import fun.lance.common.web.filter.RepeatableFilter;
import fun.lance.common.web.filter.XssFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(XssProperties.class)
public class FilterConfig {

    @Bean
    @ConditionalOnProperty(value = "xss.enabled", havingValue = "true")
    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssFilter> regBean = new FilterRegistrationBean<>();
        regBean.setDispatcherTypes(DispatcherType.REQUEST);
        regBean.setFilter(new XssFilter());
        regBean.setName("xssFilter");
        regBean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
        regBean.addUrlPatterns("/*");

        return regBean;
    }

    @Bean
    public FilterRegistrationBean<RepeatableFilter> repeatableFilterRegistration() {
        FilterRegistrationBean<RepeatableFilter> regBean = new FilterRegistrationBean<>();
        regBean.setFilter(new RepeatableFilter());
        regBean.setName("repeatableFilter");
        regBean.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        regBean.addUrlPatterns("/*");

        return regBean;
    }
}
