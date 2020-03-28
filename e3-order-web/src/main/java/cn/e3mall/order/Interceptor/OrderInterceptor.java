package cn.e3mall.order.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.entity.TbItem;
import cn.e3mall.entity.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * 用户登录拦截器
 * 
 * @author 赵明磊 2019年10月12日下午9:15:28
 */
public class OrderInterceptor implements HandlerInterceptor {

	@Value("${SSO_USER_REQ_URL}")
	private String SSO_USER_REQ_URL;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// cookie中取token
		String token = CookieUtils.getCookieValue(request, "token", true);
		// 判断token是否为空
		if (StringUtils.isBlank(token)) {
			// 如果为空，token不存在，跳转到用户登录界面。用户登录成功后，跳转到当前请求的URL
			response.sendRedirect(SSO_USER_REQ_URL +"/page/login?redirect="+request.getRequestURL());
			// 拦截器拦截
			return false;
		}
		// 如果token存在，需要调用sso系统的服务查询token的用户信息
		E3mallResult result = tokenService.getUserByToken(token);
		// 判断用户登录信息是否过期
		if (result.getStatus() != 200) {
			// 如果为空，token不存在，跳转到用户登录界面。用户登录成功后，跳转到当前请求的URL
			response.sendRedirect(SSO_USER_REQ_URL+"/page/login?redirect="+request.getRequestURL());
			// 拦截器拦截 
			return false;
		}
		// 如果取到用户信息，将用户信息放进request
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
		// 判断cookie中是否有购物车数据如果有，需要与用户登录后合并
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isNotBlank(json)) {
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(json, TbItem.class));
		}
		// 放行
		return true;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
			throws Exception {
	}

}
