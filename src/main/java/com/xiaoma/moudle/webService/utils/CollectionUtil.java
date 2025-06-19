package com.xiaoma.moudle.webService.utils;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionUtil extends CollectionUtils {
    public CollectionUtil() {
    }

    public static <T> boolean contains(@Nullable T[] array, final T element) {
        return array == null ? false : Arrays.stream(array).anyMatch((x) -> {
            return ObjectUtil.nullSafeEquals(x, element);
        });
    }

    public static boolean isArray(Object obj) {
        return null == obj ? false : obj.getClass().isArray();
    }

    public static boolean isNotEmpty(@Nullable Collection<?> coll) {
        return !CollectionUtils.isEmpty(coll);
    }

    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !CollectionUtils.isEmpty(map);
    }

    public  static boolean  isEmpty(Object[] arrays){
        return ArrayUtil.isEmpty(arrays);
    }

    /**
     * @param keyExtractor
     * @return
     * @param <T>
     * @description 通用去重方式
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
