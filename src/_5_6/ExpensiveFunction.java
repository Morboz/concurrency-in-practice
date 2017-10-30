package _5_6;

import java.math.BigInteger;

public class ExpensiveFunction implements Computable<String, BigInteger> {

    @Override
    public BigInteger compute(java.lang.String arg) throws InterruptedException {
        // after long time computation
        return new BigInteger(arg);
    }
}
