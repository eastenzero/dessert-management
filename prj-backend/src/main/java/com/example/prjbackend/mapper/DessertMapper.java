package com.example.prjbackend.mapper;

import com.example.prjbackend.domain.Dessert;
import com.example.prjbackend.domain.DessertDetail;
import com.example.prjbackend.domain.DessertQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DessertMapper {
    @Select("<script>" +
            "SELECT d.id,d.name,photoUrl,price,d.descp,release_date releaseDate,cat_id catId,c.name categoryName " +
            "FROM dessert d left join category c on d.cat_id=c.id " +
            "<where>" +
              "<if test='catId!=0'>and cat_id=#{catId} </if>" +
              "<if test='name!=null'>and d.name like concat('%',#{name},'%') </if>" +
              "<if test='descp!=null'>and d.descp like concat('%',#{descp},'%') </if>" +
              "<if test='price1!=null and price2!=null'>and price between #{price1} and #{price2} </if>" +
            "</where>" +
            "</script>")
    List<DessertDetail> list(DessertQueryParam param);

    @Insert("insert into dessert(name,descp,photoUrl,price,release_date,cat_id)" +
            "values(#{name},#{descp},#{photoUrl},#{price},#{releaseDate},#{catId})")
    int add(Dessert dessert);

    @Update("<script>" +
            "update dessert " +
            "<set>" +
            "  <if test='name!=null'>name=#{name},</if>" +
            "  <if test='descp!=null'>descp=#{descp},</if>" +
            "  <if test='photoUrl!=null'>photoUrl=#{photoUrl},</if>" +
            "  <if test='price!=null'>price=#{price},</if>" +
            "  <if test='releaseDate!=null'>release_date=#{releaseDate},</if>" +
            "  <if test='catId!=null'>cat_id=#{catId},</if>" +
            "</set>" +
            " where id=#{id}" +
            "</script>")
    int edit(Dessert dessert);

    @Delete("<script>" +
            "delete from dessert where id in " +
            "<foreach collection='array' item='id' open='(' close=')' separator=','>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int delete(Long[] ids);
}
