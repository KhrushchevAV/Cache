package study.stepup.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class Utilcache implements InvocationHandler, GCable {
    private Object obj;
    // текущее состояние кэшируемого объекта
    private State curState = new State();
    // теперь исходим из того, что кэшировать надо несколько методов, поэтому храним в мапе
    private ConcurrentHashMap<Method, ConcurrentHashMap<String, CacheValue>> cache = new ConcurrentHashMap<>();
    // мапа с временами жизни кэшей для каждого метода
    private ConcurrentHashMap<Method, Long> mTtl = new ConcurrentHashMap<>();

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
            // читаем время жизни кэша для этого метода из аннотации (time to live)
            long ttl = m.getAnnotation(Cache.class).ttl();
            if (!mTtl.containsKey(m)) {
                mTtl.put(m, ttl);
            }
            System.out.println("работаем с кэшированным методом " + m.getName() + "; ttl="+ttl+";");

            // есть у нас кэш мапа для этого метода?
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

    // метод чистки устаревших версий кэша
    public void collectGarbage() {
        Long now = System.currentTimeMillis();
        System.out.println("collectGarbage() cache.size() = "+cache.size()+" ; now="+now+";");

        // пройдем по всем методам
        for(Method m : cache.keySet()) {
            if (!mTtl.containsKey(m) || mTtl.get(m) == 0) {
                // время жизни кэша не задано ничего делать не надо
                System.out.println("collectGarbage() m= "+m.getName()+" ; ttl="+mTtl.get(m)+"; now="+now+";");
            }
            else {
                System.out.println("collectGarbage() m= "+m.getName()+" ; ttl="+mTtl.get(m)+"; now="+now+";");
                // в каждом методе пройдем по всем состояним которые у нас сейчас в кэше
                for (String st : cache.get(m).keySet()){
                    if (now - cache.get(m).get(st).getLastUsage() > mTtl.get(m)) {
                        // наше время вышло == кэш "протух"
                        // удаляем из кэша эту запись
                        cache.get(m).remove(st);
                        // в учебных целях позволим себе "отладку"
                        System.out.println("collectGarbage() - удалили из кэша значение для метода "+m.getName()+" состояния "+st+";");
                    }
                }
            }
        }
    }
}
