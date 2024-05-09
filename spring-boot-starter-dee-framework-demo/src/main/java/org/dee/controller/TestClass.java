package org.dee.controller;

public class TestClass {
    public static void main(String[] args) {
        try {
            Class.forName("org.dee.controller.TestController");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
