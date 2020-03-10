package task;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FileScanTask {
    //文件扫描类
    private  final ExecutorService pool = Executors.newFixedThreadPool(4);

    private  final AtomicInteger count = new AtomicInteger();

    private  final CountDownLatch latch = new CountDownLatch(1);

    private FileScanCallback callback;

    public FileScanTask(FileScanCallback callback){
        this.callback = callback;
    }

    public void startScan(File file) {
        count.incrementAndGet();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                list(file);
            }
        });
    }


    public  void list(File dir) {
        if(!Thread.interrupted()) {
            try {
                callback.execute(dir);
                if (dir.isDirectory()) {
                    File[] children = dir.listFiles();
                    if (children != null && children.length > 0) {
                        for (File child : children) {
                            if (child.isDirectory()) {
                                count.incrementAndGet();
                                pool.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        list(child);
                                    }
                                });
                            } else {
                                callback.execute(child);
                                System.out.println(child.getPath());
                            }
                        }
                    }
                }
            } finally {
                //所有线程执行完毕
                if (count.decrementAndGet() == 0) {
                    //通知
                    latch.countDown();
                }
            }
        }
    }
    public void waitFinish() throws InterruptedException {
       try {
           latch.await();//阻塞等待，直到计数器为0再往下执行
           //中断所有线程
       }finally {
           pool.shutdown();//调用每个线程的interrupt()中断

       }
    }
}