package com.kute.camel.model;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

@Data
@Accessors(chain = true)
public class Customer {
    private int id;
    private String name;
    private List<Department> departments;

    public static Customer newInstance() {
        return new Customer()
                .setId(RandomUtils.nextInt(1, 100))
                .setName(RandomStringUtils.randomAlphabetic(5))
                .setDepartments(Lists.newArrayList(
                        Department.newInstance(),
                        Department.newInstance(),
                        Department.newInstance()
                ));
    }
}