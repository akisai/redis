package com.bercut.sa.redis;

import com.bercut.sa.redis.model.InitMessage;
import com.bercut.sa.redis.model.Message;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by haimin-a on 09.07.2019.
 */
public class RedisImpl {
    public static void main(String[] args) {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.12.215:6379");

        final RedissonClient client = Redisson.create(config);

        final CountDownLatch latch = new CountDownLatch(5000);
        final RLock lock = client.getLock("lock");


        final long start = System.currentTimeMillis();

        AtomicInteger count = new AtomicInteger();
        for(int j=0; j<1000; j++) {
            final Long id = 10000000L + (long) (Math.random() * (50000000L));
            for (int i = 0; i < 5; i++) {
                final int finalI = i;
                Thread thread = new Thread(() -> {
                    lock.lock();
                    try {
                        InitMessage message = new InitMessage(id, finalI, 5, "test" + (finalI + 1), "UTF-8");
                        RBucket<Message> bucket = client.getBucket(String.valueOf(id));
                        Message content = bucket.get();
                        if (content == null) {
                            bucket.set(new Message(message));
                            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " complete first");
                        } else {
                            if(content.getChunk() > 1) {
                                content.setMessage(message.getMessage(), message.getChunk());
                                bucket.set(content);
                                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " complete");
                            } else {
                                content.setMessage(message.getMessage(), message.getChunk());
                                bucket.set(content);
                                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " complete last");
                                count.getAndIncrement();
                            }
                        }
                    } finally {
                        lock.unlock();
                        latch.countDown();
                    }
                });
                thread.start();
            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start));
        System.out.println(count.get());
        //RBucket<Message> bucket = client.getBucket(String.valueOf(id));

        System.out.println();
    }
}
