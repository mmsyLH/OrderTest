package icu.cchen.ordertest.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public  class DepartmentOrderStats {
        @ApiModelProperty(value = "部门名称")
        private String deptName;

        @ApiModelProperty(value = "工单总量")
        private Long totalOrders;

        @ApiModelProperty(value = "超期工单数量")
        private Long overdueOrders;

        @ApiModelProperty(value = "超期率")
        private BigDecimal overdueRate;

}