package com.gk.zookeeper.example;

import java.io.IOException;
import java.util.List;
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
 * Zookeeper异步获取子节点
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月12日下午8:12:54 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperAsyncGetChildrenNodes implements Watcher {

	private static CountDownLatch connectedLatch = new CountDownLatch(1);
	
	private static ZooKeeper zk;
	
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

	public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
		String path = "/zk-book";
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperAsyncGetChildrenNodes());
		try {
			connectedLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		zk.create(path, "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);//如果需要创建子节点，则父节点需要是CreateMode.PERSISTENT模式
		System.out.println("success create znode: " + path);
		
		zk.create(path+"/n1", "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("success create znode: " + path+"/n1");
		
		zk.getChildren(path,true,new IChildren2Callback(),null);
		
		zk.create(path+"/n2", "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("success create znode: " + path+"/n2");
		
		zk.create(path+"/n3", "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("success create znode: " + path+"/n3");
		Thread.sleep(Integer.MAX_VALUE);
	}
}

class IChildren2Callback implements AsyncCallback.Children2Callback {

	public void processResult(int rc, String path, Object ctx,
			List<String> children, Stat stat) {
		System.out.println("Get Children znode result: [response code: " + rc + ", param path: " + path + ", ctx: "
                + ctx + ", children list: " + children + ", stat: " + stat);
	}
	
}
