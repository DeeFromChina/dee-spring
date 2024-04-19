https://blog.csdn.net/kandroid/article/details/130793293

# 揭秘mybatis-plus的真相

## 代理模式
代理模式主要分为**JDK动态代理**和**CGlib动态代理**;创建mybatis-plus的Mapper是接口类，采用的是JDK动态代理
* JDK动态代理是通过反射机制生成一个实现代理接口的匿名类，要求目标对象必须是一个接口
* CGlib动态代理则使用的继承机制，对目标类（父类）进行继承，生成一个代理类（子类），从而对父类中的方法增强


