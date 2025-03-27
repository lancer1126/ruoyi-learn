package fun.lance.common.core.config;

import fun.lance.common.core.utils.OkHttpUtils;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * OkHttp配置类
 */
@AutoConfiguration
public class OkHttpConfig {

    @Bean
    @ConditionalOnMissingBean(OkHttpClient.class)
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(OkHttpUtils.class)
    public OkHttpUtils okHttpUtils(OkHttpClient okHttpClient) {
        return new OkHttpUtils(okHttpClient);
    }
}
