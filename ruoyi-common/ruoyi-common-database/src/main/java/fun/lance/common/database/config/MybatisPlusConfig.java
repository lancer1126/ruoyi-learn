package fun.lance.common.database.config;

import cn.hutool.core.bean.BeanException;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import fun.lance.common.core.factory.YmlPropertySourceFactory;
import fun.lance.common.core.utils.SpringUtils;
import fun.lance.common.database.handler.InjectionMetaObjectHandler;
import fun.lance.common.database.handler.MybatisExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis-plus配置类
 */
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("${mybatis-plus.mapper-package}")
@PropertySource(value = "classpath:common-mybatis.yml", factory = YmlPropertySourceFactory.class)
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        try {
//            // 多租户拦截器
//            TenantLineInnerInterceptor tenantInterceptor = SpringUtils.getBean(TenantLineInnerInterceptor.class);
//            interceptor.addInnerInterceptor(tenantInterceptor);
//        } catch (BeanException ignore) {
//        }
        //  分页拦截器
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        // 乐观锁拦截器
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());

        return interceptor;
    }

    /**
     * Mybatis plus异常处理
     */
    @Bean
    public MybatisExceptionHandler mybatisExceptionHandler() {
        return new MybatisExceptionHandler();
    }

    /**
     * 元对象字段填充控制器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new InjectionMetaObjectHandler();
    }

    /**
     * 分页拦截器
     */
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        pageInterceptor.setOverflow(true);
        return pageInterceptor;
    }

    /**
     * 乐观锁拦截器
     */
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }
}
