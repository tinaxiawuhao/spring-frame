package com.example.springframe.exception.basic;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@ApiModel("基础响应信息")
public class APIResponse<E> implements Serializable {

    @ApiModelProperty(value = "响应码 200:成功，其他:失败", required = true)
    private Integer code;

    @ApiModelProperty(value = "响应信息")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private E data;

    @JsonIgnore
    private Object[] args;

    /**
     * 获取响应消息
     * @return 响应消息
     */
    public String getMsg() {
        return (null != args && null != msg) ? StrUtil.format(msg, args) : msg;
    }

    public APIResponse() {
        this.code = ResponseCode.SUCCESS.getCode();
    }

    private APIResponse(ResponseCode code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public static APIResponse ok() {
        return new APIResponse(ResponseCode.SUCCESS);
    }

    public static <E> APIResponse<E> ok(E data) {
        APIResponse<E> res = new APIResponse(ResponseCode.SUCCESS);
        res.setData(data);
        return res;
    }

    public static <E> APIResponse<E> ok(E data, String msg) {
        APIResponse res = ok(data);
        res.setMsg(msg);
        return res;
    }

    private static APIResponse fail() {
        return new APIResponse(ResponseCode.FAIL);
    }

    /**
     * 失败响应
     * @param responseCode 异常码
     * @return
     */
    public static APIResponse fail(IResponseCode responseCode) {
        return fail(responseCode.getCode(), responseCode.getMsg());
    }

    /**
     * 失败响应
     * @param responseCode 异常码
     * @param args 参数
     * @return
     */
    public static APIResponse fail(IResponseCode responseCode, Object... args) {
        APIResponse res = fail(responseCode.getCode(), responseCode.getMsg());
        res.setArgs(args);
        return res;
    }

    @Deprecated
    public static APIResponse fail(String msg) {
        APIResponse res = fail();
        res.setMsg(msg);
        return res;
    }

    @Deprecated
    public static APIResponse fail(String format, Object... msg) {
        APIResponse res = fail();
        res.setMsg(StrUtil.format(format, msg));
        return res;
    }

    public static APIResponse fail(Integer code, String msg) {
        APIResponse res = fail(msg);
        res.setCode(code);
        return res;
    }

    public APIResponse msg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 响应是否成功
     *
     * @return
     */
    @ApiModelProperty(value = "请求是否成功", hidden = true)
    public boolean isOk() {
        return this.code == ResponseCode.SUCCESS.getCode();
    }

}
