package icu.cchen.ordertest.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 工单数据传输对象（DTO）
 *
 * @author 罗汉
 * @date 2024/08/07
 */
@ApiModel(description = "工单数据传输对象")
@TableName(value ="order")
@Data
public class OrderDTO implements Serializable {

    @ApiModelProperty(value = "页码", example = "1", required = true)
    private Integer pageNo;

    @ApiModelProperty(value = "每页条数", example = "5", required = true)
    private Integer pageSize;
    @ApiModelProperty(value = "工单编号", example = "202308070001", required = true)
    private String orderNo;

    @ApiModelProperty(value = "工单类型", example = "0", notes = "0-交办, 1-直接答复, 3-无效工单", required = true)
    private Integer orderType;

    @ApiModelProperty(value = "标题", example = "系统故障", required = true)
    private String title;

    @ApiModelProperty(value = "内容", example = "服务器出现异常，需紧急处理", required = true)
    private String content;

    @ApiModelProperty(value = "处理部门ID", example = "2")
    private Integer handleDeptId;

    @ApiModelProperty(value = "是否超期", example = "0", notes = "0-否, 1-是")
    private Integer isOverdue;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
