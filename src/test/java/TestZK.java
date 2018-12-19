import com.ex.zkdemo.main.ZKOperateAPI;
import org.junit.Test;

public class TestZK {

    //集群
    private static final String HOSTS="10.2.152.241:2181,10.2.152.241:2182,10.2.152.241:2181";
    //单机（不带端口的话默认2181）
    //private static final String HOSTS="10.2.152.241";

    @Test
    public void create() throws Exception {
        ZKOperateAPI operateAPI = new ZKOperateAPI();
        //端口号不写，默认是2181
//        operateAPI.connect("192.168.7.128:2181");
        operateAPI.connect(HOSTS);
        operateAPI.create("testAPI", "aaa");
        operateAPI.close();
    }

    @Test
    //新增持久节点
    public void joinPersistent() throws Exception {
        ZKOperateAPI operateAPI = new ZKOperateAPI();
        operateAPI.connect(HOSTS);
        operateAPI.joinPersistent("testAPI", "testNode1", "aaaChild");
        //模拟正在某种操作，休眠20秒后，断开zk服务连接，可查看zk服务中短暂znode被删除
        operateAPI.close();
    }

    @Test
    //新增临时节点
    public void joinEphemeral() throws Exception {
        ZKOperateAPI operateAPI = new ZKOperateAPI();
        operateAPI.connect(HOSTS);
        operateAPI.joinEphemeral("testAPI", "testNode1", "aaaChild");
        //模拟正在某种操作，休眠20秒后，断开zk服务连接，可查看zk服务中短暂znode被删除
        Thread.sleep(20000);
        operateAPI.close();
    }

    @Test
    public void list() throws Exception {
        ZKOperateAPI operateAPI = new ZKOperateAPI();
        operateAPI.connect(HOSTS);
        operateAPI.list("testAPI");
        operateAPI.close();
    }

    @Test
    public void delete() throws Exception{
        ZKOperateAPI operateAPI = new ZKOperateAPI();
        operateAPI.connect(HOSTS);
        operateAPI.delete("testAPI");
        operateAPI.close();
    }

}