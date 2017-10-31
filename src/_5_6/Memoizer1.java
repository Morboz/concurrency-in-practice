package _5_6;

import net.jcip.annotations.GuardedBy;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用HashMap来实现的缓存，通过对方法的同步来保证正确性，但是由于同步的原因，
 * 每次只能有一个线程执行compute方法，这样将导致调用其他线程的方法会被阻塞很长时间，
 * 可能添加缓存后的效率还没有不添加来的快。
 * @param <A>
 * @param <V>
 */
public class Memoizer1<A, V> implements Computable<A, V> {
    @GuardedBy("this")
    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A, V> c;

    public Memoizer1(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if(result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
