package com.yeexang.community.dto;

public class ResultDTO<T> {

    /**
     * 请求状态
     * true 为成功, false 为失败
     */
    private Boolean status;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 返回的数据集
     */
    private T data;

    /**
     * 请求成功
     * @return ResultDTO
     */
    public static <T> ResultDTO<T> getSuccessResult() {
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setStatus(true);
        return resultDTO;
    }

    /**
     * 请求成功并返回数据集
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> getSuccessResult(T data) {
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setStatus(true);
        resultDTO.setData(data);
        return resultDTO;
    }

    /**
     *
     * 请求失败
     * @return ResultDTO
     */
    public static <T> ResultDTO<T> getErrorResult() {
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setStatus(false);
        return resultDTO;
    }

    /**
     * 请求失败并返回错误信息
     * @param errorMsg
     * @return ResultDTO
     */
    public static <T> ResultDTO<T> getErrorResult(String errorMsg) {
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setStatus(false);
        resultDTO.setErrorMsg(errorMsg);
        return resultDTO;
    }

    /**
     * 请求失败并返回数据和错误信息
     * @param errorMsg
     * @param data
     * @param <T>
     * @return ResultDTO
     */
    public static <T> ResultDTO<T> getErrorResult(String errorMsg, T data) {
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setStatus(false);
        resultDTO.setErrorMsg(errorMsg);
        resultDTO.setData(data);
        return resultDTO;
    }

    private ResultDTO() {
    }

    public Boolean getStatus() {
        return status;
    }

    private void setStatus(Boolean status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    private void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    private void setData(T data) {
        this.data = data;
    }
}
