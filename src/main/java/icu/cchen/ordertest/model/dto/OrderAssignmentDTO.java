package icu.cchen.ordertest.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分派工单数据传输对象（DTO）
 *
 * @author 罗汉
 * @date 2024/08/07
 */
@ApiModel(description = "分派工单数据传输对象")
@Data
public class OrderAssignmentDTO implements Serializable {

    @ApiModelProperty(value = "工单ID", example = "123", required = true)
    private Long orderId;

    @ApiModelProperty(value = "处理部门ID", example = "2", required = true)
    private Integer handleDeptId;

    @ApiModelProperty(value = "处理部门名称", example = "技术部", required = true)
    private String handleDeptName;

    private static final long serialVersionUID = 1L;
}
