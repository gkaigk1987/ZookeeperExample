package com.gk.zookeeper.example;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

/**
 * Zookeeper同步方式创建节点
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月12日下午2:23:14 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperSyncCreateNode implements Watcher {
	
	private static CountDownLatch connectedLatch = new CountDownLatch(1);
	
	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()) {
			connectedLatch.countDown();
		}
	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperSyncCreateNode());
		System.out.println(zooKeeper.getState());
		try {
			connectedLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//创建临时节点
		String path1 = zooKeeper.create("/zk-test-ephemeral-", "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Success create znode: " + path1);
		//创建临时顺序节点，系统会在后面自动增加一串数字
		String path2 = zooKeeper.create("/zk-test-ephemeral-", "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("Success create znode: " + path2);
	}
}
