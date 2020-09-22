package com.kute.camel.split;

import com.kute.camel.model.Customer;
import com.kute.camel.model.Department;

import java.util.List;

/**
 * created by kute at 2020/9/22 9:19 下午
 */
public class SomeSplitBean {

    public List<Department> splitDepartments(Customer customer) {
        return customer.getDepartments();
    }
}
