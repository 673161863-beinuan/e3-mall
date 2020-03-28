package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3mallResult;

public interface TokenService {

	E3mallResult getUserByToken(String token);

}
