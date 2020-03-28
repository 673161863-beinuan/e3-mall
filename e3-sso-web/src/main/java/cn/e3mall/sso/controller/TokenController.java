package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.sso.service.TokenService;

/**
 * 根据token取用户信息controller
 * @author Administrator
 *
 */
@Controller
public class TokenController {

	@Reference
	private TokenService tokenService;
	
	@RequestMapping("user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback){
		E3mallResult result = tokenService.getUserByToken(token);
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
		
	} 
}
