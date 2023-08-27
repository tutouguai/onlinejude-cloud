package cn.leixd.service;

import mo.entity.po.main.Privilege;

public interface PrivilegeService {
    /**
     * 根据用户Id查询用户权限
     *
     * @param userId 用户Id
     * @return 用户级别
     */
    Privilege findPrivilegeByUserId(Integer userId);
}
