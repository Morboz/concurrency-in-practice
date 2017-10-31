package _5_6;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用ConcurrentHashMap替换HashMap,ConcurrentHashMap是线程安全的，所以疾苦不需要同步。
 * Memoizer2的问题在于，当某个线程启动了一个开销很大的计算，其他线程并不知道这个线程正在计算，
 * 可能导致重复的计算。
 * 而FutureTask这个类可以基本实现这个功能，FutureTask表示一个计算的过程，这个过程可能已经完成也可能正在进行。
 * @param <A>
 * @param <V>
 */
public class Memoizer2<A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public Memoizer2(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }


}
