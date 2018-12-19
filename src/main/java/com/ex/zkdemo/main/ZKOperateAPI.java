package com.ex.zkdemo.main;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.util.List;

public class ZKOperateAPI extends ConnectionWatch {

    //创建组
    public void create(String groupName, String data) throws KeeperException, InterruptedException {
        String path = "/" + groupName;
        //创建znode节点，第一个参数为路径；第二个参数为znode内容，字节数组；
        // 第三个参数访问控制列表（简称ACL，此处使用完全开放的ACL，允许任何客户端对znode进行读写）；
        // 第四个为创建znode类型，此处是持久的（两种类型，短暂的和持久的，短暂类型会在客户端与zk服务断开连接后，被zk服务删掉，而持久的不会）
        String createPath = zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("Created " + createPath);
    }

    //加入组（添加临时节点）
    public void joinEphemeral(String groupName, String memberName, String data) throws KeeperException, InterruptedException {
        String path = "/" + groupName + "/" + memberName;
        //创建短暂znode，会在客户端断开连接后删掉
        String createPath = zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Create " + createPath);
    }
    //加入组（添加持久节点）
    public void joinPersistent(String groupName, String memberName, String data) throws KeeperException, InterruptedException {
        String path = "/" + groupName + "/" + memberName;
        //创建短暂znode，会在客户端断开连接后删掉
        String createPath = zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("Create " + createPath);
    }

    //列出组成员
    public void list(String groupName) {
        String path = "/" + groupName;
        try {
            //第一个参数为组名，即znode路径；第二个参数是否设置观察标识，如果为true，那么一旦znode状态改变，当前对象的Watcher会被触发
            List<String> children = zk.getChildren(path, false);
            if (children.isEmpty()) {
                System.out.printf("No members in group %s\n", groupName);
                System.exit(1);
            }
            for (String child : children) {
                System.out.println(child);
            }
        } catch (KeeperException.NoNodeException ex) {
            System.out.printf("Group %s does not exist\n", groupName);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    //删除组
    public void delete(String groupName) throws KeeperException, InterruptedException {
        String path = "/" + groupName;
        try {
            List<String> children = zk.getChildren(path, false);
            for (String child : children) {
                //删除方法第一个参数指定路径，第二个参数是版本号；这是一种乐观锁机制，如果指定的版本号和对应znode版本号一致才可删除；
                // 如果设置为-1，不校验可直接删除
                zk.delete(path + "/" + child, -1);
            }
            zk.delete(path, -1);
        } catch (KeeperException.NoNodeException ex) {
            System.out.printf("Group %s does not exist\n", groupName);
            System.exit(1);
        }
    }

}