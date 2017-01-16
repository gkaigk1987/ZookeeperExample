package com.gk.zookeeper.example2;

import org.I0Itec.zkclient.ZkClient;
/**
 * Zookeeper使用ZkClient判断节点是否存在
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月16日下午1:52:52 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZkClientExistNode {

	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		String node = "/gk";
		zkClient.createEphemeral(node);
		boolean flag = zkClient.exists(node);
		System.out.println(flag);
		
		zkClient.delete(node);
		flag = zkClient.exists(node);
		System.out.println(flag);
		
	}

}
