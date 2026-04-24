package com.ll.framework.ioc;

import com.ll.domain.testPost.testPost.repository.TestPostRepository;
import com.ll.domain.testPost.testPost.service.TestPostService;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private final Map<String, Object> beans = new HashMap<>();

    public ApplicationContext() {
        TestPostRepository testPostRepository = new TestPostRepository();

        beans.put("testPostRepository", testPostRepository);
        beans.put("testPostService", new TestPostService(testPostRepository));
    }

    public <T> T genBean(String beanName) {
        return (T) beans.get(beanName);
    }
}
