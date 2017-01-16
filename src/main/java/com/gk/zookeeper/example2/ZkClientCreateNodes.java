package com.gk.zookeeper.example2;

import org.I0Itec.zkclient.ZkClient;

/**
 * Zookeeper使用ZkClient创建节点
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月14日下午4:01:12 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZkClientCreateNodes {

	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		String node = "/gk/gk-1";
//		zkClient.createPersistent(node);//存在子节点时，此种创建方法会出异常
		
		//ZkClient可以递归的先创建父节点，再创建子节点。原生javaapi不可以
		zkClient.createPersistent(node,true);//第二个参数设为true表示需要创建父节点
		System.out.println("success create znodes.");
	}

}
