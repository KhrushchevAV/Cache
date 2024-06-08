package study.stepup;

public class Main {
    public static void main(String[] args) {
        // 1. Обычная дробь без кэширования
        Fraction fr = new Fraction(3, 8);
        System.out.println(fr.doubleValue());
        System.out.println(fr.doubleValue());
        System.out.println(fr.getCallCount());
        System.out.println("---\n");
    }
}