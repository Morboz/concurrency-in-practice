package _5_5;

import javax.jws.Oneway;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * 使用信号量构造有界阻塞容器
 */
public class BoundedHashSet<T> {
    private final Semaphore sem;
    private final Set<T> set;

    public BoundedHashSet(int bound) {
        this.sem = new Semaphore(bound);
        this.set = Collections.synchronizedSet(new HashSet<T>());
    }

    public boolean add(T o) throws InterruptedException {
        sem.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded)
                sem.release();
        }
    }

    public boolean remove (Object o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved)
            sem.release();
        return wasRemoved;
    }
}
