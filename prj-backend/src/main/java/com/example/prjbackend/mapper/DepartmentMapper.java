package com.example.prjbackend.mapper;

import com.example.prjbackend.domain.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    @Insert("insert into department(name, leader, phone, descp) " +
            "values(#{name}, #{leader}, #{phone}, #{descp})")
    int insert(Department department);

    @Select("<script>select id, name, leader, phone, descp from department" +
            "<where>" +
            " <if test='name!=null and name!=\"\"'>and name like concat('%',#{name},'%')</if>" +
            " <if test='leader!=null and leader!=\"\"'>and leader like concat('%',#{leader},'%')</if>" +
            " <if test='phone!=null and phone!=\"\"'>and phone like concat('%',#{phone},'%')</if>" +
            "</where>" +
            " order by id desc" +
            "</script>")
    List<Department> list(Department department);

    @Update("<script>" +
            "update department " +
            "<set>" +
            "  <if test='name!=null'>name=#{name},</if>" +
            "  <if test='leader!=null'>leader=#{leader},</if>" +
            "  <if test='phone!=null'>phone=#{phone},</if>" +
            "  <if test='descp!=null'>descp=#{descp},</if>" +
            "</set>" +
            " where id=#{id}" +
            "</script>")
    int edit(Department department);

    @Delete("<script>" +
            "delete from department where id in " +
            "<foreach collection='array' item='id' open='(' close=')' separator=','>" +
            "  #{id}" +
            "</foreach>" +
            "</script>")
    int delete(Long[] ids);
}
