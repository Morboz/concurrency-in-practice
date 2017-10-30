package _5_5;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Proloader {
    private final FutureTask<String> future = new FutureTask<>(new Callable<String>() {
        @Override
        public String call() throws Exception {
            return "load-product-info";
        }
    });
    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    public String get() throws InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            launderThrowable(cause);
        }
        return null;
    }

    public static RuntimeException launderThrowable (Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        else if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("Not unchecked", t);
    }
}
