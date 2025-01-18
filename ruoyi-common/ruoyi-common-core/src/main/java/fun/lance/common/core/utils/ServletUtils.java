package fun.lance.common.core.utils;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServletUtils extends JakartaServletUtil {

    public static ServletRequestAttributes getRequestAttributes() {
        try {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) attributes;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getClientIP() {
        return getClientIP(getRequest());
    }
}
