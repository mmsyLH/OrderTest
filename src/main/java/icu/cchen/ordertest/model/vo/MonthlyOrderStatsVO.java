package icu.cchen.ordertest.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "某月工单总量和超期率")
public class MonthlyOrderStatsVO {
    @ApiModelProperty(value = "某月统计列表")
    private List<OrderStats> dailyStats;
}