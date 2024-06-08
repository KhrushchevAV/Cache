package study.stepup;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Utilcache implements InvocationHandler {
    private Object obj;

    // из условия задачи не ясно, может ли быть у класса два метода с аннотацией @Cache
    // а ведь тогда у этих методов надо разные кэши и возможно с разными типами, да и @Mutator у каждого отдельно...
    // Пока, неопределенность толкуем в пользу "single responsibility" - у одного класса только один метод с аннотацией @Cache
    // тогда кэшируем только одно значение одного типа
    private Object cacheValue;
    private boolean isCached;

    public Utilcache(Object o) {
        this.obj = o;
        isCached = false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method m = obj.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (m.isAnnotationPresent(Mutator.class)) {
            isCached = false;
        }

        if (m.isAnnotationPresent(Cache.class)) {
            if (isCached) {
                return cacheValue;
            }
            else {
                cacheValue = m.invoke(obj, args);
                isCached = true;
                return cacheValue;
            }
        }

        return m.invoke(obj, args);
    }
}
