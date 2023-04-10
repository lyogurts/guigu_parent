package com.atguigu.eduservice.controller;



import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 讲师(EduTeacher)表控制层
 *
 * @author makejava
 * @since 2023-04-01 10:44:31
 */
@RestController
@RequestMapping("eduTeacher")
@Api(tags = {"讲师管理"})
@CrossOrigin
public class EduTeacherController extends ApiController {
    @Autowired
    private EduTeacherService teacherService;

    @GetMapping
    @ApiOperation(value = "所有讲师列表")
    public R list(){
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "根据id逻辑删除讲师")
    public R removeById(@ApiParam(name = "id",value = "讲师ID",required = true) @PathVariable("id")String  id){
        boolean flag = teacherService.removeById(id);
        return flag? R.ok() : R.error();
    }

    /**
     * 分页查询讲师的方法
     * @return
     */
    @PostMapping("pageTeacher/{current}/{size}")
    public R pageListTeacher(
            @ApiParam(name = "current",value = "当前页码",required = true)
            @PathVariable("current") Long current,
            @ApiParam(name = "size",value = "每页记录数",required = true)
            @PathVariable("size")  Long size,
            @ApiParam(name = "teacherQuery",value = "条件",required = true)
            @RequestBody TeacherQuery teacherQuery
    )
    {
        Page<EduTeacher> page = new Page<>(current,size);
        LambdaQueryWrapper<EduTeacher> queryWrapper = new LambdaQueryWrapper<>();
        //多条件组合查询
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like(EduTeacher::getName,name);
        }
        if (!StringUtils.isEmpty(level)){
            queryWrapper.eq(EduTeacher::getLevel,level);
        }
        if (!StringUtils.isEmpty(begin)){
            queryWrapper.le(EduTeacher::getGmtCreate,begin);
        }
        if (!StringUtils.isEmpty(end)){
            queryWrapper.ge(EduTeacher::getGmtCreate,end);
        }
//        queryWrapper.orderByDesc(Boolean.parseBoolean(begin));
        teacherService.page(page,queryWrapper);
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        return R.ok().data("total",total).data("records",records);
    }


    @ApiOperation(value = "新增讲师")
    @PostMapping("addTeacher")
    public R save(
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher){

        boolean save = teacherService.save(teacher);
        return save? R.ok():R.error();
    }


    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("{id}")
    public R getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){

        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("item", eduTeacher);
    }

    @ApiOperation(value = "根据ID修改讲师")
    @PutMapping("{id}")
    public R updateById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id,

            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher){

        teacher.setId(id);
        teacherService.updateById(teacher);
        return R.ok();
    }


}

