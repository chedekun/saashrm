package com.ihrm.system.dao;

/**
 * @Author: 846602483
 * @Date: 2019-8-4 15:30
 * @Version 1.0
 */

import com.ihrm.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 企业数据访问接口
 */
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    public User findByMobile(String mobile);

}
