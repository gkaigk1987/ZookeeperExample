package com.gk.zookeeper.example;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
/**
 * Zookeeper删除带有权限的节点
 * Project Name:ZookeeperExample 
 * @author gk
 * TODO
 * Date:2017年1月14日下午3:15:37 
 * Copyright (c) 2017, gkaigk@126.com All Rights Reserved. 
 * @Version 1.0
 */
public class ZookeeperAuthDelete {

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		String path = "/zk-book";
		String childPath = "/zk-book/n1";
		ZooKeeper zk1 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zk1.addAuthInfo("digest", "gk:true".getBytes());//第一个参数不是随便写的,此处指的是digest模式
		zk1.create(path, "qq".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
		zk1.create(childPath, "qq".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
		
		ZooKeeper zk2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		try {
			zk2.delete(childPath, -1);
		} catch (Exception e) {
			System.out.println("Fail to delete:"+e.getMessage());
		}
		
		ZooKeeper zookeeper3 = new ZooKeeper("127.0.0.1:2181", 5000, null);
        zookeeper3.addAuthInfo("digest", "gk:true".getBytes());
        zookeeper3.delete(childPath, -1);
        System.out.println("success delete znode: " + childPath);
        
        //说明zk在进行删除操作的时候,其权限的作用范围是其子节点。也就是说,当我们对一个节点添加了权限之后，
        //我们依然可以随意删除该节点但是对于这个节点的子节点,就必须拥有相应的权限才能删除。
        //注：zk原生api不支持递归删除,即在存在子节点的情况下,不允许删除其父节点
        ZooKeeper zookeeper4 = new ZooKeeper("127.00.1:2181", 5000, null);
        zookeeper4.delete(path, -1);
        System.out.println("success delete znode: " + path);
		
	}
	
}
