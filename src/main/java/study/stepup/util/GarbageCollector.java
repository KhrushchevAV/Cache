package study.stepup.util;

import java.util.ArrayList;
import java.util.List;

public class GarbageCollector implements Runnable {
    // список объектов для сборки мусора
    private List<GCable> lst = new ArrayList<>();
    private boolean active = true;

    public GarbageCollector() {
        (new Thread(this)).start();
    }

    public void stop() {
        active = false;
    }

    public void addObj(GCable gcObj) {
        lst.add(gcObj);
    }

    @Override
    public void run() {
        try {
            while(active) {
                Thread.sleep(1000L);
                System.out.println("GC run(); lst.size()="+lst.size()+";");
                for (GCable gcObj: lst) {
                    gcObj.collectGarbage();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
