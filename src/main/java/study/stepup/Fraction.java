package study.stepup;

import lombok.Getter;
import lombok.ToString;
import study.stepup.util.Cache;
import study.stepup.util.Mutator;

@ToString
public class Fraction implements Fractionable {
    private int num;
    private int denum;
    @Getter
    private int callCount;

    @Mutator
    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;

        this.callCount = 0;
    }

    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(ttl = 1000)
    public double doubleValue() {
        System.out.println("invoke double value");
        // для подсчета реального количества вызовов
        callCount++;
        return (double) num/denum;
    }

    @Cache
    public String toString() {
        return "{num=" + num + "; denum="+ denum +"}";
    }
}

