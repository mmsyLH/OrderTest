package icu.cchen.ordertest.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 查询某月每个部门的工单总量和超期率的视图对象
 */
@ApiModel(description = "某月每个部门的工单总量和超期率")
@Data
@Builder
public class MonthlyOrderStatsByDeptVO {

    @ApiModelProperty(value = "部门统计列表")
    private List<DepartmentOrderStats> departmentStats;
}
