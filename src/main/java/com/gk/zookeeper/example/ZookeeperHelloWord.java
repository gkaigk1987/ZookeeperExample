package com.gk.zookeeper.example;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 第一个Zookeeper的JavaAPI程序
 * Java连接Zookeeper测试
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月12日下午2:19:43 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperHelloWord implements Watcher {

	private static CountDownLatch connectedLatch = new CountDownLatch(1);

	public void process(WatchedEvent event) {
		System.out.println("Receive watched event:" + event);
		if(KeeperState.SyncConnected == event.getState()) {
			connectedLatch.countDown();
		}
	}
	
	public static void main(String[] args) throws IOException {
		ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperHelloWord());
		System.out.println(zooKeeper.getState());
		try {
			connectedLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Zookeeper session established");
	}
	
}
