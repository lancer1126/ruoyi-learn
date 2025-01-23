package fun.lance.common.log.aspect;

import fun.lance.common.log.annotation.OfLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * 操作日志记录处理切面
 */
@Slf4j
@Aspect
@AutoConfiguration
public class LogAspect {

    private static final ThreadLocal<StopWatch> KEY_CACHE = new ThreadLocal<>();

    /**
     * 方法执行前
     */
    @Before(value = "@annotation(ofLog)")
    public void doBefore(JoinPoint joinPoint, OfLog ofLog) {
        StopWatch stopWatch = new StopWatch();
        KEY_CACHE.set(stopWatch);
        stopWatch.start();
    }

    /**
     * 方法执行后
     */
    @AfterReturning(pointcut = "@annotation(ofLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, OfLog ofLog, Object jsonResult) {
        // todo 日志切面操作
    }
}
