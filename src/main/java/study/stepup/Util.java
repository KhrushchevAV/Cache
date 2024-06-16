package study.stepup;

import study.stepup.util.Utilcache;

import java.lang.reflect.Proxy;

public class Util {
    public static <T> T cache(T objectIncome) {
        return (T) Proxy.newProxyInstance(
                objectIncome.getClass().getClassLoader(),
                objectIncome.getClass().getInterfaces(),
                new Utilcache(objectIncome)
        );
    }
}
