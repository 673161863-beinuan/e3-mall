package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbUser;

public interface RegisterService {

	//校验用户注册的信息是否可用
	public E3mallResult checkData(String param,Integer type);
	
	//执行用户注册
	public E3mallResult register(TbUser user);
}
