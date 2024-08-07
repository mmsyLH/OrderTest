package icu.cchen.ordertest.exception;

import icu.cchen.ordertest.common.BaseResponse;
import icu.cchen.ordertest.common.ResultUtils;
import icu.cchen.ordertest.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理程序
 *
 * @author 罗汉
 * @date 2024/08/07
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 业务异常处理程序
     *
     * @param e e
     * @return {@link BaseResponse}
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("BusinessException: ", e); // 打印完整的堆栈信息
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 运行时异常处理程序
     *
     * @param e e
     * @return {@link BaseResponse}
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException: ", e); // 打印完整的堆栈信息
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
