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
 * Zookeeper异步获取节点数据信息
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月13日下午1:41:38 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperAsyncGetNodeData implements Watcher {
	
	private static CountDownLatch connectedLatch = new CountDownLatch(1);
	
	private static ZooKeeper zk;

	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
            if (EventType.None == event.getType() && null == event.getPath()) {
            	connectedLatch.countDown();
            } else if (event.getType() == EventType.NodeDataChanged) {
            	//节点状态变化重新获取节点信息
                try {
                	zk.getData(event.getPath(), true, new IDataCallback(), null);
                } catch (Exception e) {
                	
                }
            }
        }
	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		String path = "/zk-book";
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperAsyncGetNodeData());
		try {
			connectedLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		zk.create(path, "gk".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);//如果需要创建子节点，则父节点需要是CreateMode.PERSISTENT模式
		System.out.println("success create znode: " + path);
		
		zk.getData(path, true, new IDataCallback(), null);
		
		zk.setData(path, "qq".getBytes(), -1);
		
		Thread.sleep(Integer.MAX_VALUE);
	}
}

class IDataCallback implements AsyncCallback.DataCallback {

	public void processResult(int rc, String path, Object ctx, byte[] data,
			Stat stat) {
		System.out.println("rc: " + rc + ", path: " + path + ", data: " + new String(data));
        System.out.println("czxID: " + stat.getCzxid() + ", mzxID: " + stat.getMzxid() + ", version: " + stat.getVersion());
	}
	
}
