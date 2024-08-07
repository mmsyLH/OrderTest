package icu.cchen.ordertest.service;

import icu.cchen.ordertest.model.PageResult;
import icu.cchen.ordertest.model.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.cchen.ordertest.model.dto.OrderAssignmentDTO;
import icu.cchen.ordertest.model.dto.OrderDTO;
import icu.cchen.ordertest.model.vo.MonthlyOrderStatsByDeptVO;
import icu.cchen.ordertest.model.vo.MonthlyOrderStatsVO;

import java.time.YearMonth;

/**
* @author Administrator
* @description 针对表【order(工单管理表)】的数据库操作Service
* @createDate 2024-08-07 16:55:53
*/
public interface OrderService extends IService<Order> {

    /**
     * 保存订单
     *
     * @param order 订单
     * @return int
     */
    int saveOrder(OrderDTO order);

    int updateOrder(Order order);

    /**
     * 按页面搜索订单
     *
     * @param orderPage 订单页面
     * @return {@link PageResult }<{@link Order }>
     */
    PageResult<Order> searchOrdersByPage(OrderDTO orderPage);

    /**
     * 分配工单
     *
     * @param orderAssignmentDTO 工单分配
     */
    void assignOrder(OrderAssignmentDTO orderAssignmentDTO);

    /**
     * 查询某月每天的工单总量、超期率
     *
     * @param month 月
     * @return {@link MonthlyOrderStatsVO }
     */
    MonthlyOrderStatsVO getMonthlyOrderStats(YearMonth month);

    /**
     * 查询某月各个部门的工单总量、超期率
     *
     * @param yearMonth 年月
     * @return {@link MonthlyOrderStatsByDeptVO }
     */
    MonthlyOrderStatsByDeptVO getMonthlyOrderStatsByDept(YearMonth yearMonth);
}
