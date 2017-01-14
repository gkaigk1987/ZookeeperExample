package com.gk.zookeeper.example;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * Zookeeper权限控制
 * 使用无权限信息的Zookeeper会话访问含权限信息的数据节点
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月14日下午2:19:50 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperAuthGet {

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		String path = "/zk-book";
		ZooKeeper zk1 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zk1.addAuthInfo("digest", "gk:true".getBytes());//第一个参数不是随便写的,此处指的是digest模式
		zk1.create(path, "qq".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
		System.out.println(new String(zk1.getData(path, false, null)));
	
		ZooKeeper zk2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
//		zk2.getData(path, false, null);//无权限访问
		
		zk2.addAuthInfo("digest", "gk:true".getBytes());
		System.out.println(new String(zk2.getData(path, false, null)));//此处可以访问
	}
}
