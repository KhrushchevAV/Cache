package study.stepup;

import lombok.SneakyThrows;
import study.stepup.util.GarbageCollector;
import study.stepup.util.Util;
import study.stepup.util.Utilcache;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {

//        // 1. Обычная дробь без кэширования
//        Fraction fr = new Fraction(3, 8);
//        System.out.println(fr.doubleValue());
//        System.out.println(fr.doubleValue());
//        System.out.println(fr.getCallCount());
//        System.out.println("---\n");
//
//        // 2. Дробь с кэшированием
//        Fractionable frCached = Util.cache(new Fraction(3, 8));
//        frCached.setNum(13);
//        System.out.println(frCached.doubleValue());
//        System.out.println(frCached.doubleValue());
//        System.out.println(frCached.getCallCount());
//        frCached.setDenum(32);
//        System.out.println(frCached.doubleValue());
//        System.out.println(frCached.doubleValue());
//        System.out.println(frCached.getCallCount());
//
        // 3. Task 3. Кэширование нескольких методов + сборка мусора в многопотоке
        System.out.println("");
        System.out.println("Task 3. Кэширование нескольких методов + сборка мусора в многопотоке");
        Fraction fr3 = new Fraction(3, 11);
        GarbageCollector gc = new GarbageCollector();

        // создаем прокси с многопоточным сборщиком мусора
        Fractionable frCache3 = Util.cache(fr3, gc);
        frCache3.setNum(1);
        frCache3.setDenum(3);
        System.out.println(frCache3.toString());
        System.out.println(frCache3.toString());
        System.out.println(frCache3.doubleValue());
        frCache3.setDenum(4);
        System.out.println(frCache3.doubleValue());
        System.out.println(frCache3.doubleValue());
        System.out.println(frCache3.getCallCount());
        frCache3.setDenum(3);
        System.out.println(frCache3.doubleValue());
        System.out.println(frCache3.getCallCount());

        // подождем пока кэш протухнет
        System.out.println("Подождем...");
        Thread.sleep(2500L);
        System.out.println("Подождали. Ожидаем что кэш протух.");

        System.out.println(frCache3.doubleValue());
        System.out.println(frCache3.doubleValue());
        System.out.println("Ожидаем что количество вызовов увеличится на 1, т.к. каш протух");
        System.out.println(frCache3.getCallCount());

        gc.stop();
    }
}