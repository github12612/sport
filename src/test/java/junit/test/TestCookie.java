package junit.test;


import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.sport.bean.user.Buyer;

public class TestCookie {
	
	//测试
	@Test
	public void testCookie() throws IOException, JsonMappingException{
		
		Buyer buyer = new Buyer();
		buyer.setUsername("aaa");
		
		//springmvc 转json
		ObjectMapper om =new ObjectMapper();
		
		om.setSerializationInclusion(Include.NON_NULL);
		
		
		StringWriter s = new StringWriter();
		//对象转json 写的过程
		om.writeValue(s, buyer);
		System.out.println(s.toString());
	}
}
