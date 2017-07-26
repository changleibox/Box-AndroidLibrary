package net.izhuo.app.library.compat;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by box on 2017/7/26.
 * <p>
 * 数组
 */

public class IArraysCompat {

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(List<T> list, Class<T> cls) {
        return list.toArray((T[]) Array.newInstance(cls, list.size()));
    }
}
