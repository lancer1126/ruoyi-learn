package fun.lance.common.core.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.core.env.Environment;

/**
 * spring工具类
 */
public class SpringUtils extends SpringUtil {
    public static boolean isVirtual() {
        return Threading.VIRTUAL.isActive(getBean(Environment.class));
    }
}
