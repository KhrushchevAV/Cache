package study.stepup;

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
