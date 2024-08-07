package icu.cchen.ordertest.controller;

import icu.cchen.ordertest.common.BaseResponse;
import icu.cchen.ordertest.common.ResultUtils;
import icu.cchen.ordertest.enums.ErrorCode;
import icu.cchen.ordertest.exception.BusinessException;
import icu.cchen.ordertest.model.PageResult;
import icu.cchen.ordertest.model.domain.Order;
import icu.cchen.ordertest.model.dto.OrderAssignmentDTO;
import icu.cchen.ordertest.model.dto.OrderDTO;
import icu.cchen.ordertest.model.vo.MonthlyOrderStatsByDeptVO;
import icu.cchen.ordertest.model.vo.MonthlyOrderStatsVO;
import icu.cchen.ordertest.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * 工单管理控制器
 *
 * @author 罗汉
 * @date 2024/08/07
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单模块")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 保存工单
     * @param orderDTO 工单对象
     * @return 操作结果
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存工单")
    public BaseResponse saveOrder(@RequestBody OrderDTO orderDTO) {
        if (orderDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单信息不能为空");
        }
        int id=orderService.saveOrder(orderDTO);
        return ResultUtils.success(id,"工单保存成功");
    }

    /**
     * 删除工单
     * @param id 工单ID
     * @return 操作结果
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除工单")
    public BaseResponse deleteOrder(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单ID不能为空");
        }
        if (!orderService.removeById(id)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "工单不存在");
        }
        return ResultUtils.success("工单删除成功");
    }

    /**
     * 分页查询工单
     *
     * @param orderPage 订单页面
     * @return 工单分页结果
     */
    @PostMapping("/search")
    @ApiOperation(value = "分页查询工单")
    public BaseResponse<PageResult<Order>> searchOrders(@RequestBody OrderDTO orderPage) {
        if (orderPage == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询条件不能为空");
        }
        Integer pageNo = orderPage.getPageNo();
        Integer pageSize = orderPage.getPageSize();
        if (pageNo < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "页码不能小于0");
        }
        if (pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "每页数量不能小于0");
        }
        PageResult<Order> pageResult=orderService.searchOrdersByPage(orderPage);
        return ResultUtils.success(pageResult);
    }

    /**
     * 更新工单
     * @param order 工单对象
     * @return 操作结果
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新工单")
    public BaseResponse updateOrder(@RequestBody Order order) {
        if (order == null || order.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单信息或ID不能为空");
        }
        int updateId = orderService.updateOrder(order);
        return ResultUtils.success(updateId,"工单更新成功");
    }


    /**
     * 分派工单
     * @return 操作结果
     */
    @PostMapping("/fenpai")
    @ApiOperation(value = "分派工单")
    public BaseResponse assignOrder(@RequestBody OrderAssignmentDTO orderAssignmentDTO) {
        if (orderAssignmentDTO == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单信息不能为空");
        }
        orderService.assignOrder(orderAssignmentDTO);
        return ResultUtils.success("工单分派成功");
    }

    /**
     * 查询某月每天的工单总量、超期率
     *
     * @param month 月
     * @return {@link BaseResponse }<{@link MonthlyOrderStatsVO }>
     */
    @GetMapping("/monthlyOrderStats")
    @ApiOperation("某月每天的工单总量、超期率")
    public BaseResponse<MonthlyOrderStatsVO> getMonthlyOrderStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        if (month == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "月份不能为空");
        }
        MonthlyOrderStatsVO monthlyOrderStatsVO = orderService.getMonthlyOrderStats(month);
        return ResultUtils.success(monthlyOrderStatsVO);
    }
    /**
     * 查询某月每天的工单总量、超期率
     *
     * @param month 月
     * @return {@link BaseResponse }<{@link MonthlyOrderStatsVO }>
     */
    @GetMapping("/monthlyOrderStatsByDept")
    @ApiOperation("某月各部门的工单总量、超期率")
    public BaseResponse<MonthlyOrderStatsByDeptVO> getMonthlyOrderStatsByDept(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        if (month == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "月份不能为空");
        }
        MonthlyOrderStatsByDeptVO monthlyOrderStatsByDept = orderService.getMonthlyOrderStatsByDept(month);
        return ResultUtils.success(monthlyOrderStatsByDept);
    }

}
