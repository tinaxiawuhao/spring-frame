package com.example.springframe.aop;

import com.alibaba.fastjson.JSON;
import com.example.springframe.utils.util.Util;
import com.google.common.base.Strings;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;


/**
 * 记录用户请求及响应数据日志
 */
public interface IControllerAspect {
    Logger log = LoggerFactory.getLogger(IControllerAspect.class);
    String START = Strings.repeat("┬", 200);
    String END = Strings.repeat("┴", 200);

    default void log(final JoinPoint joinPoint, final Object result, final LocalTime time) {
        if (result instanceof SseEmitter) {
            log.info("SseEmitter");
            return;
        }
//        参数和响应结果可能造成日志巨大，可以暂时将超出长度的响应写入独立文件
//        sb.append("args:").append(Arrays.toString(joinPoint.getArgs())).append("\n");
//        sb.append("result:").append((Util.isEmpty(result) ? "Empty" : JSON.toJSONString(result))).append("\n");
//        if (log.length() > 20000) { // 超长的内容不打印到全局日志，写入独立日志文件
//            sb.append("内容超长，请使用命令查看日志：\ncat ").append(Logs.start(this.getClass()).d(sb.toString().concat(log)).end()).append("\n");
//        } else {
//        }
        log.info("\n{}", String.join("\n",
                START,
                Objects.toString(joinPoint),
                String.format("time:%dms", Duration.between(time, LocalTime.now()).toMillis()),
                "url:".concat(getPath()),
                "args:".concat(Arrays.toString(joinPoint.getArgs())),
                "result:".concat(Util.isEmpty(result) ? "" : JSON.toJSONString(result)),
                END
        ));
    }

    /**
     * 获得请求URL
     *
     * @return String
     */
    default String getPath() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRequestURI();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "请求 URL 获取失败";
        }
    }
}
