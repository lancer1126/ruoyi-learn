package fun.lance.common.encrypt.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiEncrypt {

    /**
     * 请求的响应是否加密，默认不加密
     */
    boolean response() default false;
}
