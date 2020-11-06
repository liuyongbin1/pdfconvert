package test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @description:
 * @FileName: Test
 * @Author: haxi
 * @Date: 2020/10/22 10:06
 **/
public class Test {

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Queue<Future> queue = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            queue.add(executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        for (Future f = null; (f = queue.poll()) != null; ) {
            try {
                f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(System.currentTimeMillis() - l);

        executorService.shutdown();
    }
}
