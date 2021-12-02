package com.dabaicai.framework.common.utils.index;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * hash索引测试
 *
 * @author zhangyanbing
 * Date: 2021/12/2 15:14
 */
public class HashIndexTest {




    public static void main(String[] args) {
        Index<TestBean> index = new HashIndex<>();
        // 创建两个索引
        index.createIndex(TestBean::getName);
        index.createIndex(TestBean::getCard);
        TestBean user1 = new TestBean(1L, "用户1", 111);
        TestBean user2 = new TestBean(2L, "用户2", 222);
        TestBean user3 = new TestBean(3L, "用户3", 333);
        TestBean user4 = new TestBean(4L, "用户4", 444);
        index.put(user1);
        index.put(user2);
        index.put(user3);
        index.put(user4);
        boolean b = index.removeByProperty(TestBean::getCard, 444);
        boolean a = index.removeByProperty(TestBean::getName, "用户1");
        System.out.println(index);
    }


    @Data
    @AllArgsConstructor
    public static class TestBean implements IndexEntry{

        private Long id;

        private String name;

        private Integer card;

    }

}
