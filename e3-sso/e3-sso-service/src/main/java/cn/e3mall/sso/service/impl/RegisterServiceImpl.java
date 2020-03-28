package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbUser;
import cn.e3mall.entity.TbUserExample;
import cn.e3mall.entity.TbUserExample.Criteria;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	
	@Autowired
	private TbUserMapper userMapper;
	@Override
	public E3mallResult checkData(String param, Integer type) {
		//根据不同的type生成不同的查询条件
		TbUserExample example = new  TbUserExample();
		Criteria criteria = example.createCriteria();
		//1.用户名  2.手机  3.邮箱
		if(type == 1){
			criteria.andUsernameEqualTo(param);
		}else if(type == 2){
			criteria.andPhoneEqualTo(param);
		}else if(type == 3){
			criteria.andEmailEqualTo(param);
		}else{
			return E3mallResult.build(400, "数据类型错误");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		//判断结果中是否包含数据
		if(list != null &&list.size()>0){
			return E3mallResult.ok(false);
		}else{
			return E3mallResult.ok(true);
		}
	}
	@Override
	public E3mallResult register(TbUser user) {
		//校验获取到的用户注册数据
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPhone()) ||
				StringUtils.isBlank(user.getPassword())){
			return E3mallResult.build(400, "用户注册信息不完整,注册失败!");
		}//1.用户名  2.手机  
		E3mallResult result = checkData(user.getUsername(), 1);
		if(!(boolean) result.getData()){
			return E3mallResult.build(400, "此用户名已被占用！");
		}
		 result = checkData(user.getPhone(), 2);
		 if(!(boolean) result.getData()){
			 return E3mallResult.build(400, "此手机号码已被占用！");
		 }
		 //补全user对象属性
		 user.setCreated(new Date());
		 user.setUpdated(new Date());
		 //使用md5加密密码
		 String digest = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		 user.setPassword(digest);
		 //把注册信息插入到数据库
		 userMapper.insert(user);
		return E3mallResult.ok();
	}

}
