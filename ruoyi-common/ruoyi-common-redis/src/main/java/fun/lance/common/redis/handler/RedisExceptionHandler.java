package fun.lance.common.redis.handler;

import cn.hutool.http.HttpStatus;
import com.baomidou.lock.exception.LockFailureException;
import fun.lance.common.core.domain.model.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Redis相关异常处理
 */
@Slf4j
@RestControllerAdvice
public class RedisExceptionHandler {

    @ExceptionHandler(LockFailureException.class)
    public Result<Void> handleLockFailureException(LockFailureException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("获取锁失败了'{}',发生Lock4j异常.", requestURI, e);
        return Result.fail(HttpStatus.HTTP_UNAVAILABLE, "业务处理中，请稍后再试...");
    }
}
