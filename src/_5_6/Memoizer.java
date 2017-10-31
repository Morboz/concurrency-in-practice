package _5_6;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Memoizer<A, V> implements Computable<A, V> {
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public Memoizer(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if (f == null) {
            Callable<V> eval = () -> c.compute(arg);
            FutureTask<V> ft = new FutureTask<>(eval);
            f = cache.putIfAbsent(arg, ft);
            if (f == null) {
                f = ft;
                ft.run(); // 启动task 执行compute.
            }
        }
        try {
            return f.get();
        } catch (ExecutionException e) {
            launderThrowable(e.getCause());
        }
        // 异常后，这里应该执行不到
        return null;
    }

}
