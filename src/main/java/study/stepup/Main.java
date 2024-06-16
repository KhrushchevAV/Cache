package study.stepup;

import study.stepup.util.Util;

public class Main {
    public static void main(String[] args) {
        // 1. Обычная дробь без кэширования
        Fraction fr = new Fraction(3, 8);
        System.out.println(fr.doubleValue());
        System.out.println(fr.doubleValue());
        System.out.println(fr.getCallCount());
        System.out.println("---\n");

        // 2. Дробь с кэшированием
        Fractionable frCached = Util.cache(new Fraction(3, 8));
        frCached.setNum(13);
        System.out.println(frCached.doubleValue());
        System.out.println(frCached.doubleValue());
        System.out.println(frCached.getCallCount());
        frCached.setDenum(32);
        System.out.println(frCached.doubleValue());
        System.out.println(frCached.doubleValue());
        System.out.println(frCached.getCallCount());

        System.out.println(frCached.toString());
        System.out.println(frCached.toString());
    }
}