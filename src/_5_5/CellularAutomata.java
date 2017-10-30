package _5_5;

import javax.print.attribute.standard.Finishings;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public CellularAutomata(Board board) {
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count, () -> mainBoard.commitNewValues());
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++) {
            // 通过这里子board和下文的x，y坐标，可以猜测getSubBoard的方法返回的根据count划分的几个子board中的一个。
            // 这样通过计算子board就可以将问题拆分成Ncpu个的子问题，分别计算，通过栅栏汇总然后提交，继续下一个循环。
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
        }
    }

    private class Worker implements Runnable {
        private final Board board;

        public Worker(Board board) {
            this.board = board;
        }

        public void run() {
            while (!board.hasConverged()) {
                for (int x = 0; x < board.getMaxX; x++) {
                    for (int y = 0; y < board.getMaxY; y++) {
                        board.setNewValue(x, y, computeValue(x, y));
                    }
                }
            }
            try {
                 barrier.await();
            } catch (InterruptedException e) {
                return;
            } catch (BrokenBarrierException e) {
                return;
            }
        }

    }
    public void start() {
        for (int i = 0; i < workers.length; i++) {
            new Thread(workers[i]).start();
        }
        mainBoard.waitForConvergence();
    }
}
