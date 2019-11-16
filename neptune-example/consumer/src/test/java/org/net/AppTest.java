package org.net;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void executorTest() throws InterruptedException, ExecutionException {

        Executor executor = Executors.newFixedThreadPool(3);
        CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(executor);
        //List<Future<Integer>> result = new ArrayList<Future<Integer>>(10);
        for (int i = 0; i < 10; i++) {
            cs.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Random r = new Random();
                    int init = 0;
                    for (int i = 0; i < 100; i++) {
                        init += r.nextInt();
                        Thread.sleep(100);
                    }
                    return Integer.valueOf(init);
                }
            });
        }
        Future<Integer> future = cs.take();
        if (future != null) {
            System.out.println(future.get());
        }
    }
}
