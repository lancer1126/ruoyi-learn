package fun.lance.admin.service;

public interface AuthService {

    /**
     * oauth登录验证
     */
    String oauthBinding(String source, String tenantId, String domain);
}
