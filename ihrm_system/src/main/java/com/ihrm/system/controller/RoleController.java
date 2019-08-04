package com.ihrm.system.controller;

import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.Role;
import com.ihrm.system.service.RoleService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: 846602483
 * @Date: 2019-8-4 17:13
 * @Version 1.0
 */
@RestController
@RequestMapping("sys")
public class RoleController {
    @Autowired
    private RoleService roleService;

    //添加角色
    @RequestMapping(value = "/role",method = RequestMethod.POST)
    public Result add(@RequestBody Role role)throws Exception{
        String companyId = "1";
        role.setCompanyId(companyId);
        roleService.save(role);
        return Result.SUCCESS();
    }
    /**
     * 更新角色
     */
    @RequestMapping(value="/role/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(name="id")String id,@RequestBody Role role)throws Exception{
        roleService.update(role);
        return Result.SUCCESS();
    }

    /**
     * 删除角色
     */
    @RequestMapping(value = "/role/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(name="id")String id)throws Exception{
        roleService.delete(id);
        return Result.SUCCESS();
    }

    /**
     * 根据ID 获取角色信息
     */
    @RequestMapping(value = "/role/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(name="id")String id)throws Exception{
        Role role = roleService.findById(id);
        return new Result(ResultCode.SUCCESS,role);
    }

    /**
     * 分页查询角色
     *
     */

    @RequestMapping(value = "/role",method = RequestMethod.GET)
    public Result findByPage(int page,int pageSize,Role role)throws Exception{
        String companyId = "1";
        Page<Role> searchPage = roleService.findSearch(companyId,page,pageSize);
     PageResult<Role> pageResult = new PageResult(searchPage.getTotalElements(),searchPage.getContent());
     return new Result(ResultCode.SUCCESS,pageResult);
    }

    /**
     * 分配权限
     */
    @RequestMapping(value="/role/assignPrem",method = RequestMethod.PUT)
    public Result assignPrem(@RequestBody Map<String,Object> map){
        //获取被分配的角色的id
        String roleId = (String)map.get("id");
        //获取到权限的id列表
        List<String>permIds = (List<String>)map.get("permIds");

        //调用service完成权限分配
        roleService.assignPrems(roleId,permIds);
        return new Result(ResultCode.SUCCESS);
    }
}