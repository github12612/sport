package junit.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sport.bean.TestTable;
import com.sport.service.TestSeervice;
/**
 * spring测试环境
 * @author chenguanhua
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContxet.xml")
public class TestDB {

	@Autowired
	private TestSeervice ts;
	
	@Test
	public void testAdd() {
		TestTable tb=new TestTable();
		tb.setName("aa");
		tb.setBirthday(new Date());
		ts.add(tb);
	}

}
