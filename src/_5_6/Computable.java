package _5_6;

public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;

    default void launderThrowable(Throwable t) {
        if (t instanceof RuntimeException)
            throw (RuntimeException) t;
        if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("Not unchecked", t);
    }
}
