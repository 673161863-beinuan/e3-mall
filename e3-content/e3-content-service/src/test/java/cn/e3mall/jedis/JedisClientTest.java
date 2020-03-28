package cn.e3mall.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;
public class JedisClientTest {
	
	/*@Test
	public void jedisClientTest(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient jedisClient = ac.getBean(JedisClient.class);
		jedisClient.set("test4", "spring 配置测试");
		String string = jedisClient.get("test4");
		System.out.println(string);
	}
*/
}
