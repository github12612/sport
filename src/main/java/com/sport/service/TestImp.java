package com.sport.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sport.bean.TestTable;
import com.sport.mapper.TestTB;
@Service
@Transactional
public class TestImp implements TestSeervice {

	@Resource
	private TestTB tb;
	
	@Override
	public void add(TestTable t) {
		tb.add(t);
		
		throw new RuntimeException();
	}

}
