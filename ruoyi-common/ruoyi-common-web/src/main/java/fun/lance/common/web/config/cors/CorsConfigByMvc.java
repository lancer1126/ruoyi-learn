package fun.lance.common.web.config.cors;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 通过 CorsRegistry 配置跨域
 */
//@AutoConfiguration
public class CorsConfigByMvc implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 允许哪些域的请求
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE") // 允许的方法
                .allowedHeaders("*") // 允许的头部设置
                .allowCredentials(true) // 是否发送cookie
                .maxAge(3600); // 预检间隔时间
    }
}
