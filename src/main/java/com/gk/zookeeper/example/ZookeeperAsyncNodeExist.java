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
 * Zookeeper异步检测节点是否存在
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月14日下午1:38:37 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperAsyncNodeExist implements Watcher {
	
	private static CountDownLatch connectedLatch = new CountDownLatch(1);
	
	private static ZooKeeper zk;

	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()) {
			if(EventType.None == event.getType() && null == event.getPath()) {
				connectedLatch.countDown();
			}else if (EventType.NodeCreated == event.getType()) {
				//节点创建
		        System.out.println("success create znode: " + event.getPath());
		        zk.exists(event.getPath(), true,new MyStatCallback(),null);
		    } else if (EventType.NodeDeleted == event.getType()) {
		    	//节点删除
		        System.out.println("success delete znode: " + event.getPath());
		        zk.exists(event.getPath(), true,new MyStatCallback(),null);
		    } else if (EventType.NodeDataChanged == event.getType()) {
		    	//节点数据变更
		        System.out.println("data changed of znode: " + event.getPath());
		        zk.exists(event.getPath(), true,new MyStatCallback(),null);
		    }
		}
	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		String path = "/zk-book";
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperAsyncNodeExist());
		try {
			connectedLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		zk.exists(path, true,new MyStatCallback(),null);
		
		zk.create(path, "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.setData(path, "gy".getBytes(), -1);
		
		//对于指定节点的子节点的各种变化，不会通知客户端
		zk.create(path+"/n1", "qq".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.setData(path+"/n1", "gy".getBytes(), -1);
		
		
		zk.delete(path+"/n1", -1);
		zk.delete(path, -1);
		
		Thread.sleep(Integer.MAX_VALUE);
	}
}

class MyStatCallback implements AsyncCallback.StatCallback {
	//节点不存在时，rc值为-101，存在则为0
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		System.out.println("rc: " + rc + ", path: " + path + ", stat: " + stat);
	}
}
