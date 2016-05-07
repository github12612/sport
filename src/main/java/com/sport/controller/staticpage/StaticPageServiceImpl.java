package com.sport.controller.staticpage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 生成静态页面的service
 * 
 * @author chenguanhua
 *
 */
public class StaticPageServiceImpl implements StaticPageService, ServletContextAware {
	// 配置对象
	private Configuration conf;

	private ServletContext servletContext;

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.conf = freeMarkerConfigurer.getConfiguration();
	}

	/**
	 * 获取路径
	 * 
	 * @param name
	 * @return
	 */
	public String getPath(String name) {
		String realPath = servletContext.getRealPath(name);
		return realPath;
	}

	/**
	 * 静态化方法
	 */
	public void productIndex(Map<String, Object> rootMap, Integer id) {

		// 输出流
		Writer out = null;

		try {
			
			//生成模板路径
			String path = getPath("/html/product/" + id + ".html");
			File f = new File(path);
			File parentFile = f.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}

			out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			
			// 获取模板名字 从内存中读进来
			Template template = conf.getTemplate("productDetail.html");
			// 放入数据 处理模板
			template.process(rootMap, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
