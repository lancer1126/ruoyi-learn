package fun.lance.common.security.utils;

import cn.hutool.core.util.ObjectUtil;
import fun.lance.common.core.utils.SpringUtils;
import fun.lance.common.security.config.properties.OAuthProperties;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.request.*;

public class OAuthUtils {

    private static final AuthRedisStateCache STATE_CACHE = SpringUtils.getBean(AuthRedisStateCache.class);

    public static AuthRequest buildAuthRequest(String source, OAuthProperties.LoginProperties prop) throws AuthException {
        if (ObjectUtil.isNull(prop)) {
            throw new AuthException("不支持的第三方登录类型");
        }
        AuthConfig.AuthConfigBuilder builder = AuthConfig.builder()
                .clientId(prop.getClientId())
                .clientSecret(prop.getClientSecret())
                .redirectUri(prop.getRedirectUri())
                .scopes(prop.getScopes());
        return switch (source.toLowerCase()) {
            case "github" -> new AuthGithubRequest(builder.build(), STATE_CACHE);
            case "gitee" -> new AuthGiteeRequest(builder.build(), STATE_CACHE);
            default -> throw new AuthException("未获取到有效的Auth配置");
        };
    }
}
