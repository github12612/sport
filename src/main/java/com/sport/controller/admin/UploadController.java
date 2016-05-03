package com.sport.controller.admin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sport.common.ResponUtils;
import com.sport.web.Constants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import net.fckeditor.response.UploadResponse;

/**
 * 上传图片
 * 
 * @author chenguanhua
 *
 */
@Controller
public class UploadController {

	@RequestMapping(value = "/upload/imgUpload.do")
	public void upload(@RequestParam(required = false) MultipartFile pic,HttpServletResponse response) {
		
		//图片生成策略
		DateFormat df=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
		String format = df.format(new Date());
		
		//随机三位数
		Random random=new Random();
		for( int i=0;i<=2;i++){
			format += random.nextInt(10);
		}
		
		//扩展名
		String extension = FilenameUtils.getExtension(pic.getOriginalFilename());
		
		//保存数据库的imgpath
		String path="upload/"+format + "." + extension;
		
		//实例化一个jersey
		Client client = new Client();
		
		String url =Constants.IMG_URL+path;
		// 设置请求路径
		WebResource resource = client.resource(url);

		// 发送
		try {
			resource.put(String.class, pic.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//返回2个路径
		JSONObject js=new JSONObject();
		js.put("url", url);
		js.put("path",path);
		//发送
		ResponUtils.renderJson(response, js.toString());
	}
	/**
	 * fck上传 不知道name属性的做法
	 */
	@RequestMapping("/upload/fckUpload.do")
	public void fckUpload(HttpServletRequest reuqest,HttpServletResponse response){
		//强转request
		MultipartHttpServletRequest mr=(MultipartHttpServletRequest)reuqest;
		//获取值
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		//遍历
		Set<Entry<String,MultipartFile>> entrySet = fileMap.entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			//获取上传的value
			MultipartFile pic = entry.getValue();
			
			//图片生成策略
			DateFormat df=new SimpleDateFormat("yyyyMMddHHmmssSSS");
			
			String format = df.format(new Date());
			
			//随机三位数
			Random random=new Random();
			for( int i=0;i<=2;i++){
				format += random.nextInt(10);
			}
			
			//扩展名
			String extension = FilenameUtils.getExtension(pic.getOriginalFilename());
			
			//保存数据库的imgpath
			String path="upload/"+format + "." + extension;
			
			//实例化一个jersey
			Client client = new Client();
			
			String url =Constants.IMG_URL+path;
			// 设置请求路径
			WebResource resource = client.resource(url);

			// 发送 post get
			try {
				resource.put(String.class, pic.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			//返回url即可
			UploadResponse ok = UploadResponse.getOK(url);
			/*
			 * write 字符流
			 * 
			 * print 字节流
			 */
			try {
				response.getWriter().print(ok);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
