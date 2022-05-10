package com.xin.web.vo;


/**
 * 输出结果
 *
 * @author creator mafh 2019/11/15 14:17
 * @author updater
 * @version 1.0.0
 */
public class ResultVo {

    /**
     * 结果
     */
    private Boolean result;
    /**
     * 数据
     */
    private Object data;
    /**
     * 展示信息
     */
    private String message;

    private ResultVo(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    private ResultVo(Boolean result, Object data) {
        this.result = result;
        this.data = data;
    }

    private ResultVo(Boolean result, Object data, String message) {
        this.result = result;
        this.data = data;
        this.message = message;
    }

    /**
     * 生成结果静态调用方法
     *
     * @param result  结论
     * @param data    数据
     * @param message 信息
     * @return 对象
     */
    public static ResultVo newResultVo(Boolean result, Object data, String message) {
        return result ? new ResultVo(true, data, message + "成功！") : new ResultVo(false, data, message + "失败！");
    }

    /**
     * 生成结果静态调用方法
     *
     * @param result  结论
     * @param message 信息
     * @return 对象
     */
    public static ResultVo newResultVo(Boolean result, String message) {
        return result ? new ResultVo(true, message + "成功！") : new ResultVo(false, message + "失败！");
    }

    /**
     * 生成成功静态调用方法
     *
     * @param data 数据
     * @return 对象
     */
    public static ResultVo successVo(Object data) {
        return new ResultVo(true, data);
    }

    /**
     * 生成失败静态调用方法
     *
     * @param message 信息
     * @return 对象
     */
    public static ResultVo failureVo(String message) {
        return new ResultVo(false, message);
    }

    /**
     * 生成失败静态调用方法
     *
     * @param message 信息
     * @param data    数据
     * @return 对象
     */
    public static ResultVo failureVo(String message, Object data) {
        return new ResultVo(false, data, message);
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
