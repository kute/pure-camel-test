package com.kute.camel.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Department implements Serializable {
    private int id;
    private String address;
    private String zip;
    private String country;

    public static Department newInstance() {
        return new Department()
                .setId(RandomUtils.nextInt(1, 100))
                .setAddress(RandomStringUtils.randomAlphanumeric(10))
                .setZip(RandomStringUtils.randomAlphabetic(4))
                .setCountry("china");
    }

}