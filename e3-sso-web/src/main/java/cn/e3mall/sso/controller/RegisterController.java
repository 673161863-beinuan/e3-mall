package cn.e3mall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbUser;
import cn.e3mall.sso.service.RegisterService;

/**
 * 注册功能Controller
 * @author Administrator
 *
 */

@Controller
public class RegisterController {
	
	@Reference
	private RegisterService registerService;

	@RequestMapping("page/register")
	public String showRegister(){
		return "register";
	}
	
	//用户注册信息校验
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3mallResult checkData(@PathVariable String param,@PathVariable Integer type){
		E3mallResult e3mallResult = registerService.checkData(param, type);
		return e3mallResult;
		
	}
	
	//用户注册信息插入到数据库
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult register(TbUser user){
		E3mallResult register = registerService.register(user);
		return E3mallResult.ok();
		
	}
}
