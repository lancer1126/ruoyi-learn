package fun.lance.common.web.filter;

import fun.lance.common.core.utils.StrUtils;
import fun.lance.common.web.filter.wrapper.RepeatableRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * Repeatable 过滤器
 * springboot默认的servletRequest不能多次读取，所以对于某些请求需要包装一下
 */
public class RepeatableFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 判断请求是否符合包装标准
        boolean needWrap = request instanceof HttpServletRequest
                && StrUtils.startsWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE);
        if (needWrap) {
            ServletRequest wrappedRequest = new RepeatableRequestWrapper((HttpServletRequest) request, response);
            chain.doFilter(wrappedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
