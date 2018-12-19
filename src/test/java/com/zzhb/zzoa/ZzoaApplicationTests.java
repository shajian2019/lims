package com.zzhb.zzoa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONArray;
import com.zzhb.zzoa.service.MenuService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZzoaApplicationTests {

	
	@Autowired
	MenuService menuService;
	@Test
	public void contextLoads() {
		
	}

}

