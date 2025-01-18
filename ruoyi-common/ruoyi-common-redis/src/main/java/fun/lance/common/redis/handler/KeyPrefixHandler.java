package fun.lance.common.redis.handler;

import fun.lance.common.core.utils.StrUtils;
import org.redisson.api.NameMapper;

/**
 * redis缓存的key的前缀处理
 */
public class KeyPrefixHandler implements NameMapper {

    private final String keyPrefix;

    public KeyPrefixHandler(String keyPrefix) {
        //前缀为空 则返回空前缀
        this.keyPrefix = StrUtils.isBlank(keyPrefix) ? "" : keyPrefix + ":";
    }

    @Override
    public String map(String name) {
        if (StrUtils.isBlank(name)) {
            return null;
        }
        if (StrUtils.isNotBlank(keyPrefix) && !name.startsWith(keyPrefix)) {
            return keyPrefix + name;
        }
        return name;
    }

    @Override
    public String unmap(String name) {
        if (StrUtils.isBlank(name)) {
            return null;
        }
        if (StrUtils.isNotBlank(keyPrefix) && name.startsWith(keyPrefix)) {
            return name.substring(keyPrefix.length());
        }
        return name;
    }
}
