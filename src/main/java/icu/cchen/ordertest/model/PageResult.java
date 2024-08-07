package icu.cchen.ordertest.model;


import lombok.Data;

import java.util.List;

/**
 * 分页模型
 *
 * @author 罗汉
 * @date 2024/08/07
 */
@Data
public class PageResult<T> {
	public static final Long PAGE_SIZE = 3L;

	// 表示显示当前页[显示第几页]
	// 前端页面来的
	private Long pageNo;
	// 表示每页显示几条记录
	private Long pageSize = PAGE_SIZE;
	// 表示共有多少页, 他是通过totalRow和pageSize计算得到
	private Long pageTotalCount;
	// 表示的是共有多少条记录
	// 计算得到pageTotalCount
	private Long totalRow;
	// 表示当前页，要显示的数据
	private List<T> items;
	private String url;
	private Boolean hasNext;
	private Boolean hasPrevious;
}
