package icu.cchen.ordertest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.cchen.ordertest.model.domain.Department;
import icu.cchen.ordertest.service.DepartmentService;
import icu.cchen.ordertest.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【department(部门管理表)】的数据库操作Service实现
* @createDate 2024-08-07 16:55:53
*/
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
    implements DepartmentService {

}




