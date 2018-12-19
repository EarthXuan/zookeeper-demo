package com.ex.zkdemo.main;


import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ConnectionWatch implements Watcher {
    private static final int SESSION_TIMEOUT = 5000;

    protected ZooKeeper zk;
    private CountDownLatch connectedSignal = new CountDownLatch(1);

    public void connect(String hosts) throws IOException, InterruptedException {
        //第一个参数是Zookeeper服务主机地址，可指定端口号，默认为2181；第二个参数以毫秒为单位的会话超时参数；
        // 第三个参数是一个Watcher对象的实例。Watcher对象接收来自于Zookeeper的回调，以获得各种事件通知，
        // 本例中CreateGroup是一个Watcher对象，因此参数为this
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
        //当一个ZooKeeper的实例被创建时，会启动一个线程连接到Zookeeper服务。
        // 由于对构造函数的调用是立即返回的，因此在使用新建的Zookeeper对象之前一定要等待其与Zookeeper服务之间的连接建立成功。
        // 使用CountDownLatch使当前线程等待，直到Zookeeper对象准备就绪
        System.out.println("await......");
        connectedSignal.await();
        System.out.println("主线程继续执行");

    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("zookeeper准备就绪");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connectedSignal.countDown();
        }
    }
    public void close() throws InterruptedException {
        zk.close();
    }
}
