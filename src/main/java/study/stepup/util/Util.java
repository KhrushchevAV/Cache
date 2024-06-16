package study.stepup.util;

import java.lang.reflect.Proxy;

public class Util {

    public static <T> T cache(T objectIncome) {
        Utilcache uc = new Utilcache(objectIncome);
        return (T) Proxy.newProxyInstance(
                objectIncome.getClass().getClassLoader(),
                objectIncome.getClass().getInterfaces(),
                uc
        );
    }

    public static <T> T cache(T objectIncome, GarbageCollector gc) {
        Utilcache uc = new Utilcache(objectIncome);
        gc.addObj(uc);
        return (T) Proxy.newProxyInstance(
                objectIncome.getClass().getClassLoader(),
                objectIncome.getClass().getInterfaces(),
                uc
        );
    }
}
