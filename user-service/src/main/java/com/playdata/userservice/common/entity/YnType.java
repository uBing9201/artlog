package com.playdata.userservice.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Y/N 여부 Enum - YES(예), NO(아니오)
 */
@Getter
public enum YnType {

    @JsonProperty("Y")
    YES("예", "Y"),

    @JsonProperty("N")
    NO("아니요", "N");

    private final String desc;
    private final String code;

    YnType(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }
}
