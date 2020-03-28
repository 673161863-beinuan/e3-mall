package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.entity.TbUser;
import cn.e3mall.entity.TbUserExample;
import cn.e3mall.entity.TbUserExample.Criteria;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.sso.service.LoginService;

/**
 * 用户登录service
 * @author Administrator
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private TbUserMapper  userMapper;
	
	//redis缓存
	@Autowired
	private JedisClient jedisClient;
	
	//取配置文件session的过期时间
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3mallResult login(String username, String password) {
		//1.判断用户名是否正确
		  //根据用户名查询用户信息
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
			//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size()  == 0){
			//返回登录失败
			return E3mallResult.build(400, "用户名或密码错误！");
		}
			//取用户信息
		TbUser user = list.get(0);
			//判断密码是否正确。
			//因为密码在注册时使用MD5加密，所以这里也要使用MD5进行加密后比较
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			//如果不正确 返回登录失败
			return E3mallResult.build(400, "用户名或密码错误!");
		}
			//如果正确，生成token。存入redis
		String token = UUID.randomUUID().toString();
			//把用户信息写入redis， key：token  value：用户信息
				//设置密码为空之后存入redis（安全）
		user.setPassword(null);
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		//设置session的过期时间
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		//返回token
		return E3mallResult.ok(token);
	}

}
