package com.example.prjbackend.mapper;

import com.example.prjbackend.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select id, username, password, active from t_user where username=#{username}")
    User findByUsername(String username);

    @Insert("insert into t_user(username, password, active) values(#{username}, #{password}, #{active})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
}
