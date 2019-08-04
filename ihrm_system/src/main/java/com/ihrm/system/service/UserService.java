package com.ihrm.system.service;

/**
 * @Author: 846602483
 * @Date: 2019-8-4 15:31
 * @Version 1.0
 */

import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import com.ihrm.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * 部门操作业务逻辑层
 */
@Service
public class UserService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    /**
     * 保存用户
     */

    public void save(User user){
        //设置主键的值
        String id = idWorker.nextId()+"";
        user.setPassword("123456");//设置初始密码
        user.setEnableState(1);
        user.setId(id);
        //调用dao保存部门
        userDao.save(user);

    }

    /**
     * 更新用户
     */
    public void update(User user){
        //根据id查询部门
        User target = userDao.findById(user.getId()).get();
        //设置部门属性
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        //更新部门
        userDao.save(target);
    }
    /**
     * 根据ID查询用户
     *
     */
    public User findById(String id){
        return userDao.findById(id).get();
    }

    /**
     * 删除部门
     */
    public void delete(String id){
        userDao.deleteById(id);
    }
    /**
     * 查询全部用户列表
     * 参数：map集合的形式
     * hasDept
     * departmentId
     * companyId
     */
    public Page findAll(Map<String,Object>map, int page, int size) {
        //需要查询条件
        Specification<User> spec = new Specification<User>() {
            /**
             * 动态拼接查询条件
             * @param root
             * @param criteriaQuery
             * @param criteriaBuilder
             * @return
             */
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的company是否为空构造查询条件
                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));

                }
                //根据请求的部门ID构造查询条件
                if (!StringUtils.isEmpty(map.get("departmentId"))){
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));

                }

                if (!StringUtils.isEmpty(map.get("hasDept"))){
                    //根据请求的hasDept判断，是否分配部门。0未分配 departmentId = null，1已分配 departmentId != null
                    if ("0".equals((String) map.get("hasDept"))){
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));

                    }else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }

        };

        //分页
        Page<User> pageUser = userDao.findAll(spec,new PageRequest(page-1,size));
        return pageUser;

    }

    /**
     * 分配角色
     * @param userId
     * @param roleIds
     */

    public void assignRoles(String userId, List<String> roleIds) {

        //根据id查询用户
        User user = userDao.findById(userId).get();
        //设置用户的角色集合
        Set<Role> roles = new HashSet<>();
        for (String roleId:roleIds){
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和角色集合的关系
        user.setRoles(roles);
        //更新用户
        userDao.save(user);
    }

    /**
     * 根据mobile查询用户
     * @param mobile
     * @return
     */
    public User findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }
}
