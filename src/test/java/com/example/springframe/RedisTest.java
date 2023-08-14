package com.example.springframe;

import cn.hutool.core.util.ArrayUtil;
import com.example.springframe.utils.redis.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisUtil redisUtil;
    @Test
    void test(){
        {
            //测试String相关方法
            redisUtil.set("12345",1);
            System.out.println(redisUtil.get("12345"));
            redisUtil.incr("12345",1);
            System.out.println(redisUtil.get("12345"));
            redisUtil.decr("12345",1);
            System.out.println(redisUtil.get("12345"));
        }

        {
            //测试hash相关方法
            HashMap<String,Object> map =new HashMap<>();
            map.put("name","张");
            map.put("age",12);
            redisUtil.hmset("h12345",map);
            redisUtil.hset("h12345","addr","测试");
            System.out.println(redisUtil.hget("h12345","name"));
            System.out.println(redisUtil.hmget("h12345"));
        }

        {
            //测试list相关方法
            List<Object> list=new ArrayList<>();
            list.add("道可道");
            list.add("非常道");
            redisUtil.lSet("l12345",list);
            System.out.println(redisUtil.lGet("l12345",0,redisUtil.lGetListSize("l12345")-1));
        }

    }
}
