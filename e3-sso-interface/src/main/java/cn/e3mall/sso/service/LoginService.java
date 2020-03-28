package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3mallResult;

public interface LoginService {
	
	E3mallResult login(String username,String password);

}
