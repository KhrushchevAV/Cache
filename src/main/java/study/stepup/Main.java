package study.stepup;

public class Main {
    public static void main(String[] args) {
        // 1. Обычная дробь без кэширования
        Fraction fr = new Fraction(3, 8);
        System.out.println(fr.doubleValue());
        System.out.println(fr.doubleValue());
        System.out.println(fr.getCallCount());
        System.out.println("---\n");

        // 2. Дробь с кэшированием
        Fractionable frCached = Util.cache(fr);
        frCached.setNum(13);
        System.out.println(frCached.doubleValue());
        System.out.println(frCached.doubleValue());
        System.out.println(frCached.getCallCount());
        frCached.setDenum(32);
        System.out.println(frCached.doubleValue());
        System.out.println(frCached.doubleValue());
        System.out.println(frCached.getCallCount());
    }
}