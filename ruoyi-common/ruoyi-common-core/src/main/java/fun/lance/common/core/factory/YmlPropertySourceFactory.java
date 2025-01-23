package fun.lance.common.core.factory;

import fun.lance.common.core.utils.StrUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.Assert;

import java.io.IOException;

public class YmlPropertySourceFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String sourceName = resource.getResource().getFilename();
        if (StrUtils.isNotBlank(sourceName) && StrUtils.endsWithAny(sourceName, ".yml", ".yaml")) {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            Assert.notNull(factory.getObject(), "YmlPropertySourceFactory - PropertySource object must not be null");
            return new PropertiesPropertySource(sourceName, factory.getObject());
        }
        return super.createPropertySource(name, resource);
    }
}
