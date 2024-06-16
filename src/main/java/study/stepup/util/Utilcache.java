package study.stepup.util;

import study.stepup.Cache;
import study.stepup.Mutator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Utilcache implements InvocationHandler {
    private Object obj;
    // текущее состояние кэшируемого объекта
    private State curState = new State();
    // теперь исходим из того, что кэшировать надо несколько методов, поэтому храним в мапе
    private ConcurrentHashMap<Method, ConcurrentHashMap<State, CacheValue>> cache = new ConcurrentHashMap<>();

    // а объектов могли закашировать несколько, поэтому тоже мапа
    // private HashMap<Object, State> curState = new HashMap<>();

    public Utilcache(Object o) {
        obj = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method m = obj.getClass().getMethod(method.getName(), method.getParameterTypes());

        // мутатор
        if (m.isAnnotationPresent(Mutator.class)) {
            // мутатор теперь не сбрасывает кэш а правит текущее состояние
            curState.changeState(m, args);
        }

        // кэшируемый метод
        if (m.isAnnotationPresent(Cache.class)) {
            // беда с двойными мапами - проверяя вторую забываешь, что может не быть и первой ))
            if (!cache.contains(m)) {
                // первый вызов метода m
                cache.put(m, new ConcurrentHashMap<State, CacheValue>());
            }

            // для этого метода m уже есть такое состояние в кэше?
            if (cache.get(m).contains(curState)) {
                return cache.get(m).get(curState).getObj();
            }
            else {
                cache.get(m).put(curState, new CacheValue(m.invoke(obj, args)));
                return cache.get(m).get(curState).getObj();
            }
        }

        // все остальные методы - просто вызов
        return m.invoke(obj, args);
    }
}
