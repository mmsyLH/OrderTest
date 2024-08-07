package icu.cchen.ordertest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.cchen.ordertest.enums.ErrorCode;
import icu.cchen.ordertest.exception.BusinessException;
import icu.cchen.ordertest.mapper.DepartmentMapper;
import icu.cchen.ordertest.model.PageResult;
import icu.cchen.ordertest.model.domain.Department;
import icu.cchen.ordertest.model.domain.Order;
import icu.cchen.ordertest.model.dto.OrderAssignmentDTO;
import icu.cchen.ordertest.model.dto.OrderDTO;
import icu.cchen.ordertest.model.vo.DepartmentOrderStats;
import icu.cchen.ordertest.model.vo.MonthlyOrderStatsByDeptVO;
import icu.cchen.ordertest.model.vo.MonthlyOrderStatsVO;
import icu.cchen.ordertest.model.vo.OrderStats;
import icu.cchen.ordertest.service.OrderService;
import icu.cchen.ordertest.mapper.OrderMapper;
import icu.cchen.ordertest.utils.OrderNumberGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description 针对表【order(工单管理表)】的数据库操作Service实现
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private DepartmentMapper departmentMapper;

    /**
     * 保存订单
     *
     * @param orderDTO 订单dto
     * @return int
     */
    @Override
    public int saveOrder(OrderDTO orderDTO) {
        if (orderDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = orderDTO.getTitle();
        String content = orderDTO.getContent();
        Integer orderType = orderDTO.getOrderType();
        Integer handleDeptId = orderDTO.getHandleDeptId();

        // 验证必填字段
        if (!StringUtils.hasText(title) || !StringUtils.hasText(content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题和内容不能为空");
        }
        if (orderType == null || handleDeptId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单类型和处理部门ID不能为空");
        }
        if (orderType < 0 || orderType > 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的工单类型");
        }

        // 检查工单编号是否重复
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderDTO.getOrderNo());
        if (this.baseMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单编号已存在");
        }

        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        // 工具类 uuid结合时间戳
        order.setOrderNo(OrderNumberGenerator.generateOrderNumber());
        order.setIsOverdue(0);
        // 创建时间
        order.setCreateTime(new Date());

        return this.baseMapper.insert(order);
    }

    /**
     * 更新工单
     *
     * @param order 更新的订单对象
     * @return int
     */
    @Override
    public int updateOrder(Order order) {
        if (order == null || order.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单ID不能为空");
        }

        // 验证必填字段
        if (!StringUtils.hasText(order.getTitle()) || !StringUtils.hasText(order.getContent())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题和内容不能为空");
        }
        if (order.getOrderType() == null || order.getHandleDeptId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单类型和处理部门ID不能为空");
        }
        if (order.getOrderType() < 0 || order.getOrderType() > 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的工单类型");
        }

        // 检查工单编号是否重复
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", order.getOrderNo()).ne("id", order.getId());
        if (this.baseMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单编号已存在");
        }
        return this.baseMapper.updateById(order);
    }

    /**
     * 按页面搜索工单
     *
     * @param orderPage 工单页面
     * @return {@link PageResult }<{@link Order }>
     */
    @Override
    public PageResult<Order> searchOrdersByPage(OrderDTO orderPage) {
        int PageNo = orderPage.getPageNo();
        int pageSize = orderPage.getPageSize();
        Order order = new Order();
        BeanUtils.copyProperties(orderPage, order);

        // 设置分页参数
        Page<Order> page = new Page<>(PageNo, pageSize);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(order.getTitle() != null, "title", order.getTitle());
        queryWrapper.eq(order.getOrderNo() != null, "order_no", order.getOrderNo());
        queryWrapper.eq(order.getId() != null, "id", order.getId()); // 修正查询字段
        queryWrapper.eq(order.getOrderType() != null, "order_type", order.getOrderType());
        queryWrapper.eq(order.getHandleDeptId() != null, "handle_dept_id", order.getHandleDeptId());
        queryWrapper.eq(order.getIsOverdue() != null, "is_overdue", order.getIsOverdue());
        queryWrapper.orderByDesc("create_time");
        orderMapper.selectPage(page, queryWrapper);

        // 获取分页数据
        List<Order> list = page.getRecords();
        PageResult<Order> pageResult = new PageResult<>();
        pageResult.setPageNo(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotalRow(page.getTotal());
        pageResult.setPageTotalCount(page.getPages());
        pageResult.setItems(list);
        if (page.getTotal() == 0) {
            pageResult.setHasNext(false);
            pageResult.setHasPrevious(false);
        } else {
            pageResult.setHasNext(page.hasNext());
            pageResult.setHasPrevious(page.hasPrevious());
        }
        return pageResult;
    }

    /**
     * 分派工单
     *
     * @param orderAssignmentDTO 工单分配
     */
    @Override
    public void assignOrder(OrderAssignmentDTO orderAssignmentDTO) {
        if (orderAssignmentDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long orderId = orderAssignmentDTO.getOrderId();
        Integer handleDeptId = orderAssignmentDTO.getHandleDeptId();
        String handleDeptName = orderAssignmentDTO.getHandleDeptName();

        // 验证必填项
        if (orderId == null || handleDeptId == null || !StringUtils.hasText(handleDeptName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单ID、处理部门ID和处理部门名称不能为空");
        }

        // 验证处理部门ID是否有效
        if (!isDepartmentValid(handleDeptId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的处理部门ID");
        }

        // 查询工单是否存在
        Order order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "工单不存在");
        }

        // 更新工单的处理部门信息和分派时间
        order.setHandleDeptId(handleDeptId);
        order.setFenpaiTime(new Date());
        boolean updateSuccess = this.updateById(order);
        if (!updateSuccess) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新工单失败");
        }
    }

    /**
     * 查询某月每天的工单总量、超期率
     *
     * @param yearMonth 年月
     * @return {@link MonthlyOrderStatsVO }
     */
    @Override
    public MonthlyOrderStatsVO getMonthlyOrderStats(YearMonth yearMonth) {
        // 获取年月的第一天和最后一天
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        // 存放每天日期的集合
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate currentDate = startOfMonth;
        while (!currentDate.isAfter(endOfMonth)) {
            dateList.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        // 统计每天的工单总量和超期数量
        List<OrderStats> dailyStats = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date.plusDays(1), LocalTime.MIN).minusNanos(1);

            // 统计工单总量
            Long totalOrders = orderMapper.selectCount(
                    new LambdaQueryWrapper<Order>()
                            .ge(Order::getCreateTime, beginTime)
                            .lt(Order::getCreateTime, endTime)
            );

            // 统计超期工单数量
            Long overdueOrders = orderMapper.selectCount(
                    new LambdaQueryWrapper<Order>()
                            .ge(Order::getCreateTime, beginTime)
                            .lt(Order::getCreateTime, endTime)
                            .eq(Order::getIsOverdue, 1)
            );
            OrderStats orderStats = new OrderStats();
            orderStats.setDate(date);
            orderStats.setTotalOrders(totalOrders);
            orderStats.setOverdueOrders(overdueOrders);
            //计算 超期率 保留2位小数
            // 计算超期率，处理 totalOrders 为零的情况
            if (totalOrders != 0) {
                BigDecimal overdueRate = new BigDecimal(overdueOrders)
                        .divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP);
                orderStats.setOverdueRate(overdueRate);
            } else {
                orderStats.setOverdueRate(BigDecimal.ZERO);
            }            dailyStats.add(orderStats);
        }

        // 构造返回结果
        return MonthlyOrderStatsVO.builder()
                .dailyStats(dailyStats)
                .build();
    }

    /**
     * 某月各部门的工单总量、超期率
     *
     * @param yearMonth 年月
     * @return {@link MonthlyOrderStatsByDeptVO }
     */
    @Override
    public MonthlyOrderStatsByDeptVO getMonthlyOrderStatsByDept(YearMonth yearMonth) {
        // 获取年月的第一天和最后一天
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        // 查询所有部门
        List<Department> departments = departmentMapper.selectList(null);

        // 存放每天日期的集合
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate currentDate = startOfMonth;
        while (!currentDate.isAfter(endOfMonth)) {
            dateList.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        // 统计每个部门的工单总量和超期数量
        List<DepartmentOrderStats> departmentStatsList = new ArrayList<>();
        for (Department dept : departments) {
            Integer deptId = dept.getDeptId();
            String deptName = dept.getDeptName();

            Long totalOrders = orderMapper.selectCount(
                    new LambdaQueryWrapper<Order>()
                            .ge(Order::getCreateTime, startOfMonth.atStartOfDay())
                            .lt(Order::getCreateTime, endOfMonth.plusDays(1).atStartOfDay())
                            .eq(Order::getHandleDeptId, deptId)
            );

            Long overdueOrders = orderMapper.selectCount(
                    new LambdaQueryWrapper<Order>()
                            .ge(Order::getCreateTime, startOfMonth.atStartOfDay())
                            .lt(Order::getCreateTime, endOfMonth.plusDays(1).atStartOfDay())
                            .eq(Order::getHandleDeptId, deptId)
                            .eq(Order::getIsOverdue, 1)
            );

            DepartmentOrderStats stats = new DepartmentOrderStats();
            stats.setDeptName(deptName);
            stats.setTotalOrders(totalOrders);
            stats.setOverdueOrders(overdueOrders);

            // 计算超期率
            if (totalOrders != 0) {
                BigDecimal overdueRate = new BigDecimal(overdueOrders)
                        .divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP);
                stats.setOverdueRate(overdueRate);
            } else {
                stats.setOverdueRate(BigDecimal.ZERO);
            }

            departmentStatsList.add(stats);
        }

        // 构造返回结果
        return MonthlyOrderStatsByDeptVO.builder()
                .departmentStats(departmentStatsList)
                .build();
    }


    /**
     * 查询部门是否存在
     *
     * @param deptId 部门id
     * @return boolean
     */
    private boolean isDepartmentValid(Integer deptId) {
        // 查询部门是否存在
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId);
        Department department = departmentMapper.selectOne(queryWrapper);
        return department != null;
    }
}
