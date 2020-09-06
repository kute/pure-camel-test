package com.kute.camel.myself.typeconverter;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * created by kute at 2020/9/6 10:23 上午
 */
@Data
@Builder
public class NothingType implements Serializable {

    private String value;

}
