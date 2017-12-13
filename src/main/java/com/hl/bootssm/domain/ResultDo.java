package com.hl.bootssm.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Static
 */
public class ResultDo<T> extends BaseInfo {

    private boolean isSuccess = true;
    /**
     * 错误码
     */
    private String error;
    /**
     * 错误描述信息
     */
    private String errorDescription;

    /**
     * 返回结果对象
     */
    private T result;

    public ResultDo() {
    }

    public static ResultDo build() {
        return new ResultDo();
    }

    public static ResultDo build(String messCodeConstant) {
        ResultDo resultDo = new ResultDo();
        resultDo.setError(messCodeConstant).setErrorDescription(messageSource(messCodeConstant)).setSuccess(false);
        return resultDo;
    }

    public static ResultDo build(String messCodeConstant, Object[] objects) {
        ResultDo resultDo = new ResultDo();
        resultDo.setError(messCodeConstant)
                .setErrorDescription(messageSource(messCodeConstant, objects))
                .setSuccess(false);
        return resultDo;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public ResultDo setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
        if (StringUtils.isNotEmpty(this.errorDescription)) {
            this.setSuccess(false);
        }
        return this;
    }

    public void setErrorMessage(String messCodeConstant) {
        this.setError(messCodeConstant).setErrorDescription(messageSource(messCodeConstant)).setSuccess(false);
    }

    public void setErrorMessage(String messCodeConstant, Object[] objects) {
        this.setError(messCodeConstant).setErrorDescription(messageSource(messCodeConstant, objects)).setSuccess(false);
    }


    public String getError() {
        return error;
    }

    public ResultDo setError(String error) {
        this.error = error;
        if (StringUtils.isNotEmpty(this.error)) {
            this.isSuccess = false;
        }
        return this;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isError() {
        return !isSuccess;
    }

    public ResultDo setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ResultDo (");

        sb.append(isSuccess);
        sb.append(", ").append(error);
        sb.append(", ").append(errorDescription);
        sb.append(", ").append(result);

        sb.append(")");
        return sb.toString();
    }
}