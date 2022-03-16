package io.dataease.plugins.server;


import io.dataease.auth.service.ExtAuthService;
import io.dataease.commons.utils.BeanUtils;
import io.dataease.controller.sys.response.DeptNodeResponse;
import io.dataease.plugins.common.entity.XpackGridRequest;
import io.dataease.plugins.config.SpringContextUtil;
import io.dataease.plugins.xpack.dept.dto.request.XpackCreateDept;
import io.dataease.plugins.xpack.dept.dto.request.XpackDeleteDept;
import io.dataease.plugins.xpack.dept.dto.request.XpackMoveDept;
import io.dataease.plugins.xpack.dept.dto.response.XpackDeptTreeNode;
import io.dataease.plugins.xpack.dept.dto.response.XpackSysDept;
import io.dataease.plugins.xpack.dept.service.DeptXpackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;
@Api(tags = "xpack：部门管理")
@RequestMapping("/plugin/dept")
@RestController
public class XDeptServer {

    @Autowired
    private ExtAuthService extAuthService;

    @ApiOperation("查询子节点")
    @PostMapping("/childNodes/{pid}")
    public List<DeptNodeResponse> childNodes(@PathVariable("pid") Long pid){
        DeptXpackService deptService = SpringContextUtil.getBean(DeptXpackService.class);
        List<XpackSysDept> nodes = deptService.nodesByPid(pid);
        List<DeptNodeResponse> nodeResponses = nodes.stream().map(node -> {
            DeptNodeResponse deptNodeResponse = BeanUtils.copyBean(new DeptNodeResponse(), node);
            deptNodeResponse.setHasChildren(node.getSubCount() > 0);
            deptNodeResponse.setLeaf(node.getSubCount() == 0);
            deptNodeResponse.setTop(node.getPid() == 0L);
            return deptNodeResponse;
        }).collect(Collectors.toList());
        return nodeResponses;
    }

    @ApiOperation("搜索组织树")
    @PostMapping("/search")
    public List<DeptNodeResponse> search(@RequestBody XpackGridRequest request){
        DeptXpackService deptService = SpringContextUtil.getBean(DeptXpackService.class);
        List<XpackSysDept> ndoes = deptService.nodesTreeByCondition(request);
        List<DeptNodeResponse> nodeResponses = ndoes.stream().map(node -> {
            DeptNodeResponse deptNodeResponse = BeanUtils.copyBean(new DeptNodeResponse(), node);
            deptNodeResponse.setHasChildren(node.getSubCount() > 0);
            deptNodeResponse.setLeaf(node.getSubCount() == 0);
            deptNodeResponse.setTop(node.getPid() == 0L);
            return deptNodeResponse;
        }).collect(Collectors.toList());
        return nodeResponses;
    }

    @ApiIgnore
    @PostMapping("/root")
    public  List<XpackSysDept> rootData(){
        DeptXpackService deptService = SpringContextUtil.getBean(DeptXpackService.class);
        List<XpackSysDept> nodes = deptService.nodesByPid(null);
        return nodes;
    }

    @RequiresPermissions("dept:add")
    @ApiOperation("创建")
    @PostMapping("/create")
    public int create(@RequestBody XpackCreateDept dept){
        DeptXpackService deptService = SpringContextUtil.getBean(DeptXpackService.class);
        return deptService.add(dept);
    }

    @RequiresPermissions("dept:del")
    @ApiOperation("删除")
    @PostMapping("/delete")
    public void delete(@RequestBody List<XpackDeleteDept> requests){
        DeptXpackService deptService = SpringContextUtil.getBean(DeptXpackService.class);
        requests.forEach(request -> {
            extAuthService.clearDeptResource(request.getDeptId());
        });
        deptService.batchDelete(requests);
    }

    @RequiresPermissions("dept:edit")
    @ApiOperation("更新")
    @PostMapping("/update")
    public int update(@RequestBody XpackCreateDept dept){
        DeptXpackService deptService = SpringContextUtil.getBean(DeptXpackService.class);
        return deptService.update(dept);
    }



    @ApiIgnore
    @PostMapping("/nodesByDeptId/{deptId}")
    public List<XpackDeptTreeNode> nodesByDeptId(@PathVariable("deptId") Long deptId){
        DeptXpackService deptService = SpringContextUtil.getBean(DeptXpackService.class);
        return deptService.searchTree(deptId);
    }

    @RequiresPermissions("dept:edit")
    @ApiOperation("移动")
    @PostMapping("/move")
    public void move(@RequestBody XpackMoveDept xpackMoveDept){
        DeptXpackService deptService = SpringContextUtil.getBean(DeptXpackService.class);
        deptService.move(xpackMoveDept);
    }
}
