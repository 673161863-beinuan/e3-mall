package cn.e3mall.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ActivemqTest {
	/*
	
	@Test
	public void  testActivemqSpring(){
		//初始化一个spring容器
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//获取JmsTemplate对象
		JmsTemplate jmsTemplate = ac.getBean(JmsTemplate.class);
		//获取destination 对象
		Destination destination = (Destination) ac.getBean("queueDestination");
		//使用JsmTemplate发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("测试111");
			}
		});
	}
*/
}
