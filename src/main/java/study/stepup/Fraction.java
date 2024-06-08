package study.stepup;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Fraction implements Fractionable {
    private int num;
    private int denum;
    @Getter
    private int callCount;

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
    @Cache
    public double doubleValue() {
        System.out.println("invoke double value");
        // для подсчета реального количества вызовов
        callCount++;
        return (double) num/denum;
    }
}

