package com.sport.web.aop;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.danga.MemCached.MemCachedClient;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.common.web.aop.MemCachedUtil;

/**
 * 缓存memcached 环绕 aop 切面对象
 * 
 * @author chenguanhua
 *
 */
public class CachedInterceptor {

	@Autowired
	private MemCachedClient memCachedClient;
	// 时间缓存时间
	private static final int TIME = 360000;// 秒

	private int expiry = TIME;

	// 配置环绕
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		// 去memcached查看有没有数据
		String key = getCachedKey(pjp);
		// 返回值
		if (null == memCachedClient.get(key)) {
			// 会service层去取
			Object proceed = pjp.proceed();
			memCachedClient.set(key, proceed, expiry);
		}

		return memCachedClient.get(key);
	}

	// 后置由于数据库变更 清理get*
	public void doAfter(JoinPoint jp) {

		// 获取全类
		String packageName = jp.getTarget().getClass().getName();

		Map<String, Object> keySet = MemCachedUtil.getKeySet(memCachedClient);
		Set<Entry<String, Object>> entrySet = keySet.entrySet();
		for (Entry<String, Object> entry : entrySet) {

			if (entry.getKey().startsWith(packageName)) {
				// 清理
				memCachedClient.delete(entry.getKey());
			}
		}
	}

	// key 全类名
	public String getCachedKey(ProceedingJoinPoint pjp) {

		StringBuilder key = new StringBuilder();
		// 包名+类名
		String packageName = pjp.getTarget().getClass().getName();
		key.append(packageName);

		// 方法名
		String methodName = pjp.getSignature().getName();
		key.append(".").append(methodName);

		// 参数多个
		Object[] params = pjp.getArgs();
		// springmvc 转json
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);

		for (Object object : params) {

			StringWriter s = new StringWriter();
			// 对象转json 写的过程
			try {
				om.writeValue(s, object);
			} catch (IOException e) {
				e.printStackTrace();
			}
			key.append(".").append(s.toString());
		}
		return key.toString();
	}

	public int getExpiry() {
		return expiry;
	}

	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}
}
