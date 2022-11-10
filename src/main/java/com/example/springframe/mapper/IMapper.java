package com.example.springframe.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMapper {

    List<JSONObject> selectList(@Param("s") String s);
}
