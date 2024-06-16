package study.stepup.util;


public class CacheValue {
    private Object obj;
    private long lastUsage;

    CacheValue(Object o) {
        this.obj = o;
        this.lastUsage = System.currentTimeMillis();
    }

    public Object getObj() {
        // кто-то получил значение - обновим время последнего использования
        this.lastUsage = System.currentTimeMillis();
        return obj;
    }

    public void setObj(Object obj) {
        // кто-то установил значение - обновим время последнего использования
        this.lastUsage = System.currentTimeMillis();
        this.obj = obj;
    }


}
