package _5_5;


public class Proloader {
    private final FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
        @Override
        public String call() throws Exception {
            return "load-product-info";
        }
    });
    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    public String get() {
        try {
            return future.get();
        } catch (ExecutionException e) {

        }
    }
}
