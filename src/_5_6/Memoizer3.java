package _5_6;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 使用FutureTask实现，如果已经有结果就返回FutureTask.get，如果有其他线程正在计算，那么将一直阻塞到结果计算出返回。
 * Memoizer3的实现已经很完美了：它表现出很好的并发性。但是它也有一个缺陷：复合操作“若没有则添加”并不是原子性的，
 * 所以可能多个线程同时判断f(x)不存在，然后同时计算。
 * @param <A>
 * @param <V>
 */
public class Memoizer3<A, V> implements Computable<A, V> {
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if (f == null) {
            Callable<V> eval = () -> c.compute(arg);
            FutureTask<V> ft = new FutureTask<>(eval);
            cache.put(arg, ft);
            ft.run(); // 启动task 执行compute.
        }
        try {
            return f.get();
        } catch (ExecutionException e) {
            launderThrowable(e.getCause());
        }
        // 异常后，这里应该执行不到
        return null;
    }

    /*private void launderThrowable(Throwable t) {
        if (t instanceof RuntimeException)
            throw (RuntimeException) t;
        if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("Not unchecked", t);
    }*/
}
