package com.gk.zookeeper.example;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
/**
 * Zookeeper异步更新节点的数据
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月13日下午4:56:28 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperAsyncSetNodeData implements Watcher {
	
	private static CountDownLatch connectedLatch = new CountDownLatch(1);
	
	private static ZooKeeper zk;

	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()) {
			if(EventType.None == event.getType() && null == event.getPath()) {
				connectedLatch.countDown();
			}
		}
	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		String path = "/zk-book";
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperAsyncSetNodeData());
		try {
			connectedLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		zk.create(path, "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);//如果需要创建子节点，则父节点需要是CreateMode.PERSISTENT模式
		System.out.println("success create znode: " + path);
		
		zk.setData(path, "qq".getBytes(), -1, new IStatCallback(), null);
		
		Thread.sleep(Integer.MAX_VALUE);
	}
}

class IStatCallback implements AsyncCallback.StatCallback {
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		System.out.println("rc: " + rc + ", path: " + path + ",ctx:" + ctx + ", stat: " + stat);
	}
	
}
