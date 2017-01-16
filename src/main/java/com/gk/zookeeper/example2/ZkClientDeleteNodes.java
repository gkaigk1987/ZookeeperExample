package com.gk.zookeeper.example2;

import org.I0Itec.zkclient.ZkClient;

/**
 * Zookeeper使用ZkClient删除节点
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月16日上午10:59:35 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZkClientDeleteNodes {

	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		String parentNode = "/gk";
		String childNode = "/gk/gk-1";
		zkClient.createPersistent(parentNode, "gk");
		zkClient.createPersistent(childNode, "qq");
		System.out.println(zkClient.readData(parentNode));//读取节点数据
		System.out.println(zkClient.readData(childNode));
		zkClient.deleteRecursive(parentNode);//递归删除节点
		System.out.println("success delete znode.");
	}

}
