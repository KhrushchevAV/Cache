import org.junit.jupiter.api.Test;
import study.stepup.Fraction;
import study.stepup.Fractionable;
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
}
