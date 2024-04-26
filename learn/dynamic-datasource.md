```markdown
当使用 `spring-dynamic-datasource` 结合 `mybatis-plus` 时，你可以很容易地配置多个数据源，并在运行时动态切换它们。以下是一个简单的示例，演示了如何设置和使用多个数据源：

### 1. 添加依赖

首先，确保你的 `pom.xml` 文件中包含了 `mybatis-plus-boot-starter` 和 `dynamic-datasource-spring-boot-starter` 的依赖：

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- MyBatis Plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>最新版本</version>
    </dependency>
    <!-- Dynamic DataSource Spring Boot Starter -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
        <version>最新版本</version>
    </dependency>
    <!-- 数据库连接池依赖，比如 HikariCP -->
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
    </dependency>
    <!-- MySQL 驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    <!-- 其他依赖... -->
</dependencies>
```

### 2. 配置多个数据源

在 `application.yml` 文件中配置多个数据源：

```yaml
spring:
  datasource:
    dynamic:
      primary: master # 设置默认的数据源名称
      datasource:
        master:
          url: jdbc:mysql://localhost:3306/master_db?useSSL=false&serverTimezone=UTC
          username: root
          password: password
          driver-class-name: com.mysql.cj.jdbc.Driver
        slave1:
          url: jdbc:mysql://localhost:3306/slave1_db?useSSL=false&serverTimezone=UTC
          username: root
          password: password
          driver-class-name: com.mysql.cj.jdbc.Driver
        # ... 可以继续添加其他数据源配置

mybatis-plus:
  mapper-locations: classpath:/mapper/*.xml # Mapper XML 文件位置
  type-aliases-package: com.example.demo.entity # 实体类所在的包路径
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 打印 SQL 语句到控制台
```

### 3. 创建实体类和 Mapper 接口

为每个数据源创建对应的实体类和 Mapper 接口。确保 Mapper 接口继承自 `BaseMapper<T>`，其中 `T` 是对应的实体类。

```java
// Master 数据源的实体类
@Data
public class MasterUser {
    private Long id;
    private String name;
    // ... 其他字段
}

// Master 数据源的 Mapper 接口
public interface MasterUserMapper extends BaseMapper<MasterUser> {
    // ... 自定义方法
}

// Slave1 数据源的实体类
@Data
public class Slave1User {
    private Long id;
    private String name;
    // ... 其他字段
}

// Slave1 数据源的 Mapper 接口
public interface Slave1UserMapper extends BaseMapper<Slave1User> {
    // ... 自定义方法
}
```

### 4. 使用数据源

在 Service 或 Controller 中，你可以使用 `@DS` 注解来指定使用哪个数据源。

```java
@Service
public class UserService {

    @Autowired
    private MasterUserMapper masterUserMapper;

    @Autowired
    private Slave1UserMapper slave1UserMapper;

    @DS("master") // 使用 master 数据源
    public List<MasterUser> findAllUsersFromMaster() {
        return masterUserMapper.selectList(null);
    }

    @DS("slave1") // 使用 slave1 数据源
    public Slave1User findUserByIdFromSlave1(Long id) {
        return slave1UserMapper.selectById(id);
    }
    
    // ... 其他方法
}
```

### 5. 动态切换数据源（可选）

如果你需要在运行时动态切换数据源，可以使用 `DynamicDataSourceContextHolder`。
```