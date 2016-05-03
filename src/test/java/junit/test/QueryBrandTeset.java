package junit.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sport.bean.product.Brand;
import com.sport.query.product.BrandQuery;
import com.sport.service.product.BrandService;

public class QueryBrandTeset extends CommonTest {

	@Autowired
	private BrandService brandService;
	
	@Test
	public void getTest(){
		
		BrandQuery query=new BrandQuery();
		//query.setFields("id");
		//query.setNameLike(false);
		//query.setName("金");
		query.orderById(false);
		query.setPageNo(2);
		query.setPageSize(2);
		
		List<Brand> list = brandService.getBrandList(query);
		for (Brand brand : list) {
			System.out.println(brand);
		}
	}
}
