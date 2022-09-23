package com.paic.util;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyh
 * @Description:
 * @date 2021/11/2211:07 上午
 */
@Data
@Builder
public class Tuple2<T, U> {

    private final T a;
    private final U b;

    public Tuple2(T a, U b) {
        this.a = a;
        this.b = b;
    }

    public static <T0, T1> Tuple2<T0, T1> of(T0 value0, T1 value1) {
        return new Tuple2<>(value0, value1);
    }

    public static <T0, T1> List<Tuple2<T0, T1>> concat(T0[] value0, T1[] value1) {
        int len = value0.length;
        if (len != value1.length) {
            throw new RuntimeException("value0 和 value1 必须个数保持一致");
        }

        List<Tuple2<T0, T1>> result = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            result.add(new Tuple2<>(value0[i], value1[i]));
        }
        return result;
    }
}
