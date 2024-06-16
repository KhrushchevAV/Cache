import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import study.stepup.Fraction;
import study.stepup.Fractionable;
import study.stepup.util.GarbageCollector;
import study.stepup.util.Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MainTest {

    @Test
    public void TestFraction() {
        // проверим что не сломали дробь
        Fractionable frCached = Util.cache(new Fraction(3, 8));
        double val = frCached.doubleValue();
        assertEquals(val, (double) 3/8);
    }

    @Test
    public void TestCache() {
        // проверим что реально работает @Cache
        Fractionable frCached = Util.cache(new Fraction(3, 8));
        double val1 = frCached.doubleValue();
        double val2 = frCached.doubleValue();
        // значение то же (закэшированное)
        assertEquals(val1, val2);
        // а реально вызов Fraction.doubleValue() был только один
        assertEquals(frCached.getCallCount(), 1);
    }

    @Test
    public void TestMutator() {
        // проверим что реально работает @Mutator
        Fractionable frCached = Util.cache(new Fraction(3, 8));
        double val1 = frCached.doubleValue();
        double val2 = frCached.doubleValue();
        // а реально вызов Fraction.doubleValue() был только один
        assertEquals(frCached.getCallCount(), 1);
        frCached.setDenum(10);
        double val3 = frCached.doubleValue();
        // реально был второй вызов не из кэша
        assertEquals(frCached.getCallCount(), 2);
        assertNotEquals(val2, val3);
    }

    @SneakyThrows
    @Test
    public void TestMultithreadingGC() {
        Fraction fr3 = new Fraction(3, 11);
        GarbageCollector gc = new GarbageCollector();
        // создаем прокси с многопоточным сборщиком мусора
        Fractionable frCache3 = Util.cache(fr3, gc);
        frCache3.setNum(1);
        frCache3.setDenum(3);
        double val1 = frCache3.doubleValue();
        double val2 = frCache3.doubleValue();
        // значение то же (закэшированное)
        assertEquals(val1, val2);
        // а реально вызов Fraction.doubleValue() был только один
        assertEquals(frCache3.getCallCount(), 1);

        // подождем пока кэш протухнет
        System.out.println("Подождем...");
        Thread.sleep(2500L);
        System.out.println("Подождали. Ожидаем что кэш протух.");

        double val3 = frCache3.doubleValue();
        assertEquals(val1, val3);
        // ожидаем что кэш устарел поэтому реально был +1 вызов
        assertEquals(frCache3.getCallCount(), 2);

        // остановим отдельный поток GarbageCollector
        gc.stop();
    }

}
