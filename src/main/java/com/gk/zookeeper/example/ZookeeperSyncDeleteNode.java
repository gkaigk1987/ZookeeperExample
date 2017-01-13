package com.gk.zookeeper.example;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * Zookeeper同步删除节点
 * 只允许删除叶子节点，即一个节点如果有子节点，
 * 那么该节点将无法直接删除，必须先删掉其所有子节点。
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月12日下午3:42:27 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperSyncDeleteNode implements Watcher {
	
	private static CountDownLatch connectedLatch = new CountDownLatch(1);
	
	private static ZooKeeper zk;
	

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		String path = "/zk-book";
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperSyncDeleteNode());
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
		
		try {
			zk.delete(path, -1);//存在子节点时，该节点无法删除，必须先要删除子节点
		} catch (Exception e) {
			System.out.println("fail to delete znode: " + path);
			e.printStackTrace();
		}
		
		zk.delete(path+"/n1", -1);//先删除子节点
		System.out.println("success delete znode: " + path+"/n1");
		
		zk.delete(path, -1);//再删除父节点
        System.out.println("success delete znode: " + path);

        Thread.sleep(Integer.MAX_VALUE);
	}


	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()) {
			if(EventType.None == event.getType() && null == event.getPath()) {
				connectedLatch.countDown();
			}
		}
	}

}
