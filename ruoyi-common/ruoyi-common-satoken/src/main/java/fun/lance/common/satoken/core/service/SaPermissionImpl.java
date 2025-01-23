package fun.lance.common.satoken.core.service;

import cn.dev33.satoken.stp.StpInterface;

import java.util.List;

/**
 * sa-token权限管理实现类
 */
public class SaPermissionImpl implements StpInterface {

    /**
     * 获取菜单权限列表
     * @param loginId  账号id
     * @param loginType 账号类型
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // todo getPermissionList
        return List.of();
    }

    /**
     * 获取角色权限列表
     * @param loginId  账号id
     * @param loginType 账号类型
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // todo getRoleList
        return List.of();
    }
}
