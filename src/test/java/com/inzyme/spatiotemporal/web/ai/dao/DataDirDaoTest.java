package com.inzyme.spatiotemporal.web.ai.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.inzyme.spatiotemporal.web.ai.domain.entity.DataDir;;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DataDirDaoTest {
	
//	@Autowired
//	private DataDirDao dataDirDao;
//	
//	@Test
//	public void testInsert() {
//        System.out.println(("----- insert method test ------"));
//        DataDir dir = new DataDir();
//        dir.setDir("root1");
//        dir.setName("根目录2");
//        dir.setDescription("测试新增3");
//        int ret = dataDirDao.insert(dir);
//        Assert.assertEquals(1, ret);
//        System.out.println(dir);		
//	}
//
//	@Test
//	public void testSelect() {
//        System.out.println(("----- selectAll method test ------"));
//        List<DataDir> dirList = dataDirDao.selectList(null);
//        Assert.assertEquals(12, dirList.size());
//        dirList.forEach(System.out::println);		
//	}
//	
//	@Test
//	public void testSelectPage() {
//        System.out.println(("----- selectPage method test ------"));
//        IPage<DataDir> page = new Page<DataDir>();
//        page.setCurrent(2);
//        page.setSize(2);
//        page = dataDirDao.selectPage(page, new QueryWrapper());
//        Assert.assertEquals(2, page.getPages());
//        page.getRecords().forEach(System.out::println);		
//	}
}
