package fun.lance.common.doc.config.properties;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "springdoc")
public class SpringDocProperties {
    /**
     * 文档基本信息
     */
    @NestedConfigurationProperty
    private InfoProperties info = new InfoProperties();
    /**
     * 扩展文档地址
     */
    @NestedConfigurationProperty
    private ExternalDocumentation externalDoc;
    /**
     * 路径
     */
    @NestedConfigurationProperty
    private Paths paths;
    /**
     * 组件
     */
    @NestedConfigurationProperty
    private Components components;
    /**
     * 标签
     */
    private List<Tag> tags;

    /**
     * 文档基础属性信息
     */
    @Data
    public static class InfoProperties {
        private String title;
        private String description;
        @NestedConfigurationProperty
        private Contact contact;
        @NestedConfigurationProperty
        private License license;
        private String version;
    }
}
