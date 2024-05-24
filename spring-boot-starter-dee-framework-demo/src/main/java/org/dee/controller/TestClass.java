package org.dee.controller;

import org.dee.entity.TestEntity;

public class TestClass {
    public static void main(String[] args) {
        try {
            TestEntityController controller = new TestEntityController();
            TestEntity param = new TestEntity();
            param.setCode("123");
            controller.vaildPage(param);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
