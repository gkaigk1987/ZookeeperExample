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

/**
 * Zookeeper异步删除节点
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月12日下午4:32:03 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperAsyncDeleteNode implements Watcher {
	
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
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperAsyncDeleteNode());
		try {
			connectedLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//建立永久节点
		zk.create(path, "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("success create znode: " + path);
		
		zk.create(path+"/n1", "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("success create znode: " + path+"/n1");
		
		
		zk.delete(path, -1, new IVoidCallback(), null);//此处删除会出错，必须先删除子节点后才能删除父节点
		zk.delete(path+"/n1", -1, new IVoidCallback(), null);
		zk.delete(path, -1, new IVoidCallback(), null);
		
		Thread.sleep(Integer.MAX_VALUE);
	}
}

class IVoidCallback implements AsyncCallback.VoidCallback {
	//删除信息在此处打印
	public void processResult(int rc, String path, Object ctx) {
		System.out.println(rc + ", " + path + ", " + ctx);
	}
}
