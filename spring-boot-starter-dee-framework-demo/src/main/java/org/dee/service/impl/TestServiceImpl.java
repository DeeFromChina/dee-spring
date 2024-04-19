package org.dee.service.impl;

import org.dee.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public void test() {
        System.out.println("test");
    }

}
