package com.example.prjbackend.mapper;

import com.example.prjbackend.domain.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    @Insert("insert into employee(name, gender, dept_id, phone, hire_date, salary, descp) " +
            "values(#{name}, #{gender}, #{deptId}, #{phone}, #{hireDate}, #{salary}, #{descp})")
    int insert(Employee employee);

    @Select("<script>" +
            "select id, name, gender, dept_id deptId, phone, hire_date hireDate, salary, descp " +
            "from employee " +
            "<where>" +
            "  <if test='name!=null and name!=\"\"'>and name like concat('%',#{name},'%')</if>" +
            "  <if test='deptId!=null and deptId!=0'>and dept_id=#{deptId}</if>" +
            "</where>" +
            " order by id desc" +
            "</script>")
    List<Employee> list(Employee employee);

    @Update("<script>" +
            "update employee " +
            "<set>" +
            "  <if test='name!=null'>name=#{name},</if>" +
            "  <if test='gender!=null'>gender=#{gender},</if>" +
            "  <if test='deptId!=null'>dept_id=#{deptId},</if>" +
            "  <if test='phone!=null'>phone=#{phone},</if>" +
            "  <if test='hireDate!=null'>hire_date=#{hireDate},</if>" +
            "  <if test='salary!=null'>salary=#{salary},</if>" +
            "  <if test='descp!=null'>descp=#{descp},</if>" +
            "</set>" +
            " where id=#{id}" +
            "</script>")
    int edit(Employee employee);

    @Delete("<script>" +
            "delete from employee where id in " +
            "<foreach collection='array' item='id' open='(' close=')' separator=','>" +
            "  #{id}" +
            "</foreach>" +
            "</script>")
    int delete(Long[] ids);
}
