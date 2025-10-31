package com.example.prjbackend.mapper;

import com.example.prjbackend.domain.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into category(name,descp) values(#{name},#{descp})")
    int insert(Category category);

    @Select("<script>select id,name,descp from category" +
            "<where>" +
            " <if test='name!=null'>and name like concat('%',#{name},'%')</if>" +
            " <if test='descp!=null'>and descp like concat('%',#{descp},'%')</if>" +
            "</where>" +
            "</script>")
    List<Category> list(Category category);

    @Update("<script>" +
            "update category " +
            "<set>" +
            "  <if test='name!=null'>name=#{name},</if>" +
            "  <if test='descp!=null'>descp=#{descp},</if>" +
            "</set>" +
            " where id=#{id}" +
            "</script>")
    int edit(Category category);


    @Delete("<script>" +
            "delete from category where id in " +
            "<foreach collection='array' item='id' open='(' close=')' separator=','>" +
              "#{id}" +
            "</foreach>" +
            "</script>")
    int delete(Long[] ids);
}
