package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class JedisTest {
	
	
	//测试单机
	@Test
	public  void jedisTest() {
		//创建一个jedis对象  传入host port参数
		Jedis jedis = new Jedis("192.168.25.131", 6379);
		//直接使用jedis操作redis 每个命令都对应一个方法
//		jedis.set("test1", "my jedis test1");
		String str = jedis.get("test1");
		System.out.println(str);
		jedis.close();
		
	}
	//测试集群
	@Test
	public void testJedisCluster() throws Exception{
		//创建set集合
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.131", 7001));
		nodes.add(new HostAndPort("192.168.25.131", 7002));
		nodes.add(new HostAndPort("192.168.25.131", 7003));
		nodes.add(new HostAndPort("192.168.25.131", 7004));
		nodes.add(new HostAndPort("192.168.25.131", 7005));
		nodes.add(new HostAndPort("192.168.25.131", 7006));
		//创建一个jedisCluster对象  参数是一个set类型 nodes包含若干个hostAndport
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("test121", "集群测试111111111");
		String str = jedisCluster.get("test121");
		System.out.println(str);
		jedisCluster.close();
	}
	

}
