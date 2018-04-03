package com.d1m.wechat.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类描述
 *
 * @author Yuan Zhen on 2016-12-02.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-test.xml")
@ActiveProfiles("test")
@Transactional()
public class SpringTest {
}
