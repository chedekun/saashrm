package com.ihrm.system.service;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 846602483
 * @Date: 2019-8-4 20:16
 * @Version 1.0
 */
@Service
@Transactional
public class PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PermissionMenuDao permissionMenuDao;

    @Autowired
    private PermissionPointDao permissionPointDao;

    @Autowired
    private PermissionApiDao permissionApiDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存权限
     */
    public void save(Map<String,Object> map)throws Exception{
        //设置主键的值
        String id = idWorker.nextId()+"";
        //通过map构造permission对象
        Permission permission = BeanMapUtils.mapToBean(map,Permission.class);
        permission.setId(id);
        //根据类型构造不同的资源对象（菜单，按钮，api）
        int type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map,PermissionMenu.class);
                menu.setId(id);
                permissionMenuDao.save(menu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map,PermissionPoint.class);
                point.setId(id);
                permissionPointDao.save(point);
                break;
            case PermissionConstants.PY_API:
                PermissionApi api = BeanMapUtils.mapToBean(map,PermissionApi.class);
                api.setId(id);
                permissionApiDao.save(api);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }

        // 保存
        permissionDao.save(permission);
    }

    //更新权限
    public void update(Map<String,Object>map)throws Exception{
        Permission permission = BeanMapUtils.mapToBean(map,Permission.class);
        //通过传递的权限id查询权限
        Permission permission1 = permissionDao.findById(permission.getId()).get();
        permission1.setName(permission.getName());
        permission1.setCode(permission.getCode());
        permission1.setDescription(permission.getDescription());
        permission1.setEnVisible(permission.getEnVisible());
        //根据类型构造不同的资源
        int type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map,PermissionMenu.class);
                menu.setId(permission.getId());
                permissionMenuDao.save(menu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map,PermissionPoint.class);
                point.setId(permission.getId());
                permissionPointDao.save(point);
                break;
            case PermissionConstants.PY_API:
                PermissionApi api = BeanMapUtils.mapToBean(map,PermissionApi.class);
                api.setId(permission.getId());
                permissionApiDao.save(api);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        //保存
        permissionDao.save(permission);

    }

    /**
     * 根据ID查询
     * 1、查询权限
     * 2、根据权限的类型查询资源
     * 3、构造map集合
     */

    public Map<String,Object> findById(String id)throws Exception{
        Permission permission = permissionDao.findById(id).get();
        int type = permission.getType();
        Object object = null;
        if (type == PermissionConstants.PY_MENU){
            object = permissionMenuDao.findById(id).get();

        }else if (type == PermissionConstants.PY_POINT){
            object = permissionPointDao.findById(id).get();

        }else if (type == PermissionConstants.PY_API){
            object = permissionApiDao.findById(id).get();
        }else {
            throw new CommonException(ResultCode.FAIL);
        }

        Map<String,Object> map = BeanMapUtils.beanToMap(object);
        map.put("name",permission.getName());
        map.put("type",permission.getType());
        map.put("code",permission.getCode());
        map.put("description",permission.getDescription());
        map.put("pid",permission.getPid());
        map.put("enVisible",permission.getEnVisible());

        return map;

    }
    /**
     * 查询全部
     * type:查询全部权限列表type
     * 0：菜单+按钮（权限点）
     * 1：菜单
     * 2：按钮（权限点）
     * 3：api接口
     * enVisible:
     * 0:查询所有SaaS平台的最高权限
     * 1：查询企业的权限
     * pid:父id
     */
    public List<Permission> findAll(Map<String,Object>map){
        //需要查询条件
        Specification<Permission> specification = new Specification<Permission>() {
            /**
             * 动态拼接查询条件
             * @param root
             * @param criteriaQuery
             * @param criteriaBuilder
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> list = new ArrayList<>();
                //根据父ID查询
                if (!StringUtils.isEmpty(map.get("pid"))){
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                //根据enVisible查询
                if (!StringUtils.isEmpty(map.get("enVisible"))){
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),(String)map.get("enVisible")));
                }
                //根据type
                if (!StringUtils.isEmpty(map.get("type"))){
                  String ty = (String)map.get("type");
                  CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("tyep"));
                  if ("0".equals(ty)){
                      in.value(1).value(2);
                  }else {
                      in.value(Integer.parseInt(ty));
                  }
                  list.add(in);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }

        };
        return permissionDao.findAll(specification);
    }

    /**
     * 根据id删除
     * 1、删除权限
     * 2、删除权限对应的资源
     */

    public void deleteById(String id)throws Exception{
        //通过传递的权限id查询权限
        Permission permission = permissionDao.findById(id).get();
        permissionDao.delete(permission);
        //根据类型构造不同的资源
        int type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
    }
}
