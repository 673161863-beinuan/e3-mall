package cn.e3mall.manager.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

/**
 * 上传图片controller
 * 
 * @author Administrator
 *
 */
@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;

	@RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
	@ResponseBody
	public String upLoadFile(MultipartFile uploadFile) {
		try {
			// 创建一个FastDFS的客户端,读取配置文件
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			// 获取文件名字
			String originalFilename = uploadFile.getOriginalFilename();
			// 获取文件扩展名
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			// 进行上传处理
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			// 拼接url 解决硬编码
			url = IMAGE_SERVER_URL + url;
			// 返回Map
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}

	}

}
