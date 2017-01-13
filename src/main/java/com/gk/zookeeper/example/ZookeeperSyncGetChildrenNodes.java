package com.gk.zookeeper.example;

import java.io.IOException;
import java.util.List;
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
 * Zookeeper同步获取子节点
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月12日下午4:57:42 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperSyncGetChildrenNodes implements Watcher {
	
	private static CountDownLatch connectedLatch = new CountDownLatch(1);
	
	private static ZooKeeper zk;

	/**
	 * Watcher通知是一次性的，即一旦触发一次通知后，该Watcher就失效了，因此客户端需要反复注册Watcher，
	 * 即程序中在process里面又注册了Watcher，否则，将无法获取c3节点的创建而导致子节点变化的事件。
	 */
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
            if (EventType.None == event.getType() && null == event.getPath()) {
            	connectedLatch.countDown();
            } else if (event.getType() == EventType.NodeChildrenChanged) {
                try {
                    System.out.println("ReGet Child:" + zk.getChildren(event.getPath(), true));
                } catch (Exception e) {
                	
                }
            }
        }
	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		String path = "/zk-book";
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperSyncGetChildrenNodes());
		try {
			connectedLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		zk.create(path, "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//如果需要创建子节点，则父节点需要是CreateMode.PERSISTENT模式
		System.out.println("success create znode: " + path);
		
		zk.create(path+"/n1", "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("success create znode: " + path+"/n1");
		
		List<String> childrenList = zk.getChildren(path, true);
		System.out.println(childrenList);
		
		zk.create(path+"/n2", "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("success create znode: " + path+"/n2");
		Thread.sleep(1000);
		
		zk.create(path+"/n3", "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("success create znode: " + path+"/n3");
		Thread.sleep(Integer.MAX_VALUE);
	}

}
