package com.paic.util;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @Description:
 * @date 2021/3/7
 * @time 16:27
 */
@Getter
public enum SqlTypeEnum {
    DDL("0"), EXPORT("1");

    private String type;

    SqlTypeEnum(String type) {
        this.type = type;
    }

    public static SqlTypeEnum getType(String stateBackendType) {
        if (StringUtils.isEmpty(stateBackendType)) {
            return DDL;
        }

        for (SqlTypeEnum sqlTypeEnum : SqlTypeEnum.values()) {
            if (sqlTypeEnum.getType().equalsIgnoreCase(stateBackendType.trim())) {
                return sqlTypeEnum;
            }

        }
        throw new  RuntimeException("stateBackendType值只能是 0 1 2 非法参数值"+stateBackendType);
    }

    public static void main(String[] args) {
        System.out.println(getType("1"));

    }
}
