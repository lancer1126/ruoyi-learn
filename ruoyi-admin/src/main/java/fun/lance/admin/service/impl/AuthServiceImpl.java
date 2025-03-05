package fun.lance.admin.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.lance.admin.service.AuthService;
import fun.lance.common.security.config.properties.OAuthProperties;
import fun.lance.common.security.utils.OAuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final OAuthProperties oAuthProperties;
    private final ObjectMapper objectMapper;

    @Override
    public String oauthBinding(String source, String tenantId, String domain) {
        OAuthProperties.LoginProperties prop = oAuthProperties.getType().get(source);
        if (ObjectUtil.isNull(prop)) {
            return source + " 平台账号暂不支持";
        }
        AuthRequest authRequest = OAuthUtils.buildAuthRequest(source, prop);
        Map<String, String> map = new HashMap<>();
        map.put("tenantId", tenantId);
        map.put("domain", domain);
        map.put("state", AuthStateUtils.createState());

        String authUrl;
        try {
            String state = Base64.encode(objectMapper.writeValueAsString(map), StandardCharsets.UTF_8);
            authUrl = authRequest.authorize(state);
        } catch (JsonProcessingException e) {
            log.error("获取OAuth授权错误, source-{} ",source, e);
            throw new RuntimeException(e);
        }
        return authUrl;
    }
}
