package com.gk.zookeeper.example2;

import org.I0Itec.zkclient.ZkClient;

/**
 * Zookeeper使用ZkClient创建zk会话
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月14日下午3:56:21 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZkClientHelloWorld {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		System.out.println("Zookeeper session established!");
	}

}
