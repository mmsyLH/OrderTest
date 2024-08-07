package icu.cchen.ordertest.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName order
 */
@TableName(value ="`order`")
@Data
public class Order implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String orderNo;

    private Integer orderType;

    private String title;

    private String content;

    private Integer handleDeptId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fenpaiTime;

    private Integer isOverdue;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}