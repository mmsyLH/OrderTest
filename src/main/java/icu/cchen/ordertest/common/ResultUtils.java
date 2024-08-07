package icu.cchen.ordertest.common;


import icu.cchen.ordertest.enums.ErrorCode;

/**
 * 结果：结果实用程序
 * <p>
 * <p>
 * 返回工具类
 *
 * @author 罗汉
 * @date 2024/08/07
 */
public final class ResultUtils {
    private ResultUtils() {
    }

    /**
     * 成功
     *
     * @param <T> 任意类型
     * @param data 数据
     * @return {@link BaseResponse}<{@link T}>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(20000, data, "ok");
    }
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(20000, null, "ok");
    }
    /**
     * 成功
     *
     * @param data    数据
     * @param message 消息
     * @return {@link BaseResponse}<{@link T}>
     */
    public static <T> BaseResponse<T> success(T data,String message) {
        return new BaseResponse<>(20000, data, message);
    }

    /**
     * 错误
     * @param <T> 返回类型
     * @param errorCode 错误代码
     * @return {@link BaseResponse}<{@link T}>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 错误
     * 失败
     *
     * @param <T> 返回类型
     * @param code        代码
     * @param message     消息
     * @param description 描述
     * @return {@link BaseResponse}<{@link T}>
     */
    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    /**
     * 错误
     * 失败
     *
     * @param <T> 返回类型
     * @param errorCode   错误代码
     * @param message     消息
     * @param description 描述
     * @return {@link BaseResponse}<{@link T}>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    /**
     * 错误
     * 失败
     *
     * @param errorCode   错误代码
     * @param description 描述
     * @return {@link BaseResponse}
     */
    public static BaseResponse<String> error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), description);
    }

}
