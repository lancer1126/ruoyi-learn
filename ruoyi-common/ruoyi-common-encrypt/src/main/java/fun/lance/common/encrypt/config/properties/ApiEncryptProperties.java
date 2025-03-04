package fun.lance.common.encrypt.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "api-encrypt")
public class ApiEncryptProperties {

    /**
     * api加密开关
     */
    private Boolean enabled;

    /**
     * 头部标识
     */
    private String headerFlag;

    /**
     * 相应返回加密公钥
     */
    private String publicKey;

    /**
     * 请求解密私钥
     */
    private String privateKey;
}
