package fun.lance.common.encrypt.filter;

import cn.hutool.core.util.ObjectUtil;
import fun.lance.common.core.exception.ServiceException;
import fun.lance.common.core.utils.SpringUtils;
import fun.lance.common.core.utils.StrUtils;
import fun.lance.common.encrypt.annotation.ApiEncrypt;
import fun.lance.common.encrypt.config.properties.ApiEncryptProperties;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;

/**
 * 请求加密过滤器
 */
public class CryptoFilter implements Filter {

    private final ApiEncryptProperties properties;
    private final RequestMappingHandlerMapping handlerMapping;
    private final HandlerExceptionResolver exceptionResolver;

    public CryptoFilter(ApiEncryptProperties properties) {
        this.properties = properties;
        this.handlerMapping = SpringUtils.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        this.exceptionResolver = SpringUtils.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        try {
            // 获取加密注解
            ApiEncrypt apiEncrypt = getApiEncryptAnnotation(servletRequest);
            // 处理请求加密
            ServletRequest processedRequest = processRequest(servletRequest, apiEncrypt);
            // 处理响应加密
            ServletResponse processedResponse = processResponse(servletResponse, apiEncrypt);
            // 执行过滤链
            chain.doFilter(processedRequest, processedResponse);

            // 如果响应需要加密，处理加密后的响应内容
            if (processedResponse instanceof EncryptResponseBodyWrapper) {
                finalizeResponse(servletResponse, (EncryptResponseBodyWrapper) processedResponse);
            }
        } catch (Exception e) {
            handleException(servletRequest, servletResponse, e);
        }
    }

    private ServletRequest processRequest(HttpServletRequest request, ApiEncrypt apiEncrypt) throws IOException {
        // 是否为 PUT 或者 POST 请求
        if (HttpMethod.PUT.matches(request.getMethod()) || HttpMethod.POST.matches(request.getMethod())) {
            // 是否存在加密标头
            String headerValue = request.getHeader(properties.getHeaderFlag());
            if (StrUtils.isNotBlank(headerValue)) {
                // 请求解密
                return new DecryptRequestBodyWrapper(request, properties.getPrivateKey(), properties.getHeaderFlag());
            } else if (ObjectUtil.isNotNull(apiEncrypt)) {
                // 如有注解要求加密但请求未加密，抛出异常
                throw new ServiceException("没有访问权限，请联系管理员授权", HttpStatus.FORBIDDEN.value());
            }
        }
        return request;
    }

    private ServletResponse processResponse(HttpServletResponse response, ApiEncrypt apiEncrypt) throws IOException {
        // 判断是否响应加密
        boolean respEncryptFlag = apiEncrypt != null && apiEncrypt.response();
        if (respEncryptFlag) {
            return new EncryptResponseBodyWrapper(response);
        }
        return response;
    }

    private void finalizeResponse(HttpServletResponse originalResponse, EncryptResponseBodyWrapper wrapper) throws IOException {
        originalResponse.reset();
        // 对原始内容加密
        String encryptContent = wrapper.getEncryptContent(
                originalResponse, properties.getPublicKey(), properties.getHeaderFlag()
        );
        // 对加密后的内容写出
        originalResponse.getWriter().write(encryptContent);
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        ServiceException serviceException;
        if (e instanceof ServiceException) {
            serviceException = (ServiceException) e;
        } else {
            serviceException = new ServiceException("请求处理异常，请稍后再试", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        exceptionResolver.resolveException(request, response, null, serviceException);
    }

    /**
     * 获取 ApiEncrypt 注解
     */
    private ApiEncrypt getApiEncryptAnnotation(HttpServletRequest servletRequest) {
        // 获取注解
        try {
            HandlerExecutionChain handlerChain = handlerMapping.getHandler(servletRequest);
            if (ObjectUtil.isNotNull(handlerChain)) {
                Object handler = handlerChain.getHandler();
                if (ObjectUtil.isNotNull(handler)) {
                    // 从handler获取注解
                    if (handler instanceof HandlerMethod handlerMethod) {
                        return handlerMethod.getMethodAnnotation(ApiEncrypt.class);
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
