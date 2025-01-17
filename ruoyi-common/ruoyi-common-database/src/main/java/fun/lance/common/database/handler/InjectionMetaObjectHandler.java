package fun.lance.common.database.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import fun.lance.common.core.exception.ServiceException;
import fun.lance.common.database.core.domain.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * mybatis plus注入处理器
 * 在insert或update时自动补充实体类部分属性
 */
@Slf4j
public class InjectionMetaObjectHandler implements MetaObjectHandler {

    /**
     * 在inset对象时
     * 自动填充创建时间，更新时间，创建者，更新者
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity base) {
                Date current = ObjectUtil.isNotNull(base.getCreateTime()) ? base.getCreateTime() : new Date();
                base.setCreateTime(current);
                base.setUpdateTime(current);

                // 填充创建人/更新人信息
                if (ObjectUtil.isNotNull(base.getCreateBy())) {
                    // todo 获取当前登录的用户信息
                }
            } else {
                Date date = new Date();
                this.strictInsertFill(metaObject, "createTime", Date.class, date);
                this.strictInsertFill(metaObject, "updateTime", Date.class, date);
            }
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    /**
     * 在update对象时
     * 自动填充更新时间，更新者
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity base) {
                // 获取当前时间作为更新时间，无论原始对象中的更新时间是否为空都填充
                Date current = new Date();
                base.setUpdateTime(current);

                // todo 获取当前登录用户的ID，并填充更新人信息
            } else {
                this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
            }
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }
}
