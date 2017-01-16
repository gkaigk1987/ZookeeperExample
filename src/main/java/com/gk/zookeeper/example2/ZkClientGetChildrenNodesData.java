package com.gk.zookeeper.example2;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * Zookeeper使用ZkClient获取节点数据
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月16日上午11:21:42 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZkClientGetChildrenNodesData {

	public static void main(String[] args) throws InterruptedException {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		String node = "/gk";
		zkClient.createEphemeral(node,"gk");
		
		zkClient.subscribeDataChanges(node, new IZkDataListener() {
			//节点数据删除监听
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("Node " + dataPath + " deleted.");
			}
			//节点数据更新监听
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println("Node " + dataPath + " changed, new data: " + data);
			}
		});
		
		System.out.println(zkClient.readData(node));
		zkClient.writeData(node, "qq");
		Thread.sleep(1000);
        zkClient.delete(node);
        Thread.sleep(Integer.MAX_VALUE);
	}

}
