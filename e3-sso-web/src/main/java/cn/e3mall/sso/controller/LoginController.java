package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.sso.service.LoginService;

/**
 * 用户登录处理controller
 * @author 赵明磊
 *
 */
@Controller
public class LoginController {
	
	@Reference
	private LoginService loginService;
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	//展示用户登录页面
	@RequestMapping("/page/login")
	public String showLogin(String redirect ,Model model){
		model.addAttribute("redirect", redirect);
		return "login";
	}
	
	//用户登录
	@RequestMapping(value="/user/login",method = RequestMethod.POST)
	@ResponseBody
	public E3mallResult login(String username,String password,
			HttpServletRequest request ,HttpServletResponse response){
		E3mallResult login = loginService.login(username, password);
		//判断登录是否成功
		if(login.getStatus() == 200) {
			String token = login.getData().toString();
			//如果登录成功把token写入cookie
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}
		
		//返回结果
		return login;
	}
}
