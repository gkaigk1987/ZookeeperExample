package com.gk.zookeeper.example2;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
/**
 * Zookeeper使用ZkClient获取子节点
 * 1、客户端可以对一个不存在的节点进行子节点变更的监听。
 * 2、一旦客户端对一个节点注册了子节点列表变更监听之后，那么当该节点的子节点列表发生变更时（包括创建或删除），
 * 	服务端都会通知客户端，并将最新的子节点列表发送给客户端
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月16日上午11:16:45 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZkClientGetChildrenNodes {

	public static void main(String[] args) throws InterruptedException {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		String node = "/gk";
		zkClient.subscribeChildChanges(node, new IZkChildListener() {
			//监听子节点变化
			public void handleChildChange(String parentPath, List<String> currentChilds)
					throws Exception {
				System.out.println(parentPath + "'s child changed, currentChilds:" + currentChilds);
			}
		});
		zkClient.createPersistent(node);
		Thread.sleep(1000);
		zkClient.createPersistent(node+"/gk-1");
		Thread.sleep(1000);
		zkClient.delete(node + "/gk-1");
		Thread.sleep(1000);
		zkClient.delete(node);
		Thread.sleep(Integer.MAX_VALUE);
	}

}
