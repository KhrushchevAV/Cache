package study.stepup.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class Utilcache implements InvocationHandler {
    private Object obj;
    // текущее состояние кэшируемого объекта
    private State curState = new State();
    // теперь исходим из того, что кэшировать надо несколько методов, поэтому храним в мапе
    private ConcurrentHashMap<Method, ConcurrentHashMap<String, CacheValue>> cache = new ConcurrentHashMap<>();

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
            // есть у нас мапа для этого метода?
            if (!cache.containsKey(m)) {
                // первый вызов метода m
                cache.put(m, new ConcurrentHashMap<String, CacheValue>());
            }

            // для этого метода m уже есть такое состояние в кэше?
            if (cache.get(m).containsKey(curState.toString())) {
                System.out.println("Нашли в кэше значение для метода "+m.toString()+" и состояния " + curState.toString()+" его и вернем! ");
                return cache.get(m).get(curState.toString()).getObj();
            }
            else {
                cache.get(m).put(curState.toString(), new CacheValue(m.invoke(obj, args)));
                return cache.get(m).get(curState.toString()).getObj();
            }
        }

        // все остальные методы - просто вызов
        return m.invoke(obj, args);
    }
}
