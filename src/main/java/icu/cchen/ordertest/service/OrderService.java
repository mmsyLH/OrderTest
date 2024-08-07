package icu.cchen.ordertest.service;

import icu.cchen.ordertest.model.PageResult;
import icu.cchen.ordertest.model.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.cchen.ordertest.model.dto.OrderAssignmentDTO;
import icu.cchen.ordertest.model.dto.OrderDTO;

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

    void assignOrder(OrderAssignmentDTO orderAssignmentDTO);
}
