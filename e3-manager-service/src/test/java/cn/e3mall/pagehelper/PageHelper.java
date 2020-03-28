package cn.e3mall.pagehelper;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageInfo;

import cn.e3mall.entity.TbItem;
import cn.e3mall.entity.TbItemExample;
import cn.e3mall.mapper.TbItemMapper;

public class PageHelper {
	
	@Test
	public void testPageHelper(){
		//初始化spring容器
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//在容器中获取mapper的代理对象
		TbItemMapper mapper = ac.getBean(TbItemMapper.class);
		//执行sql语句之前设置设置分页信息使用pageHelper的startPage方法
		com.github.pagehelper.PageHelper.startPage(1, 20);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = mapper.selectByExample(example);
		//取分页信息 PageInfo   1、总记录数  2、总页面数  3，当前页码等
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(pageInfo.getSize());
		for (TbItem tbItem : list) {
			System.out.println(tbItem);
			
		}
	}

}
