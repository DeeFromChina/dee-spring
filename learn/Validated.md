# @Validated怎么分组校验
`@Validated` 是 Spring 框架中用于校验的方法级别的注解，它通常与 JSR-303 规范中的校验注解（如 `@NotNull`、`@Size`、`@Pattern` 等）一起使用。分组校验是指在不同的场景下，对同一个对象的不同属性进行不同的校验。

以下是使用 `@Validated` 进行分组校验的基本步骤：

1. **定义校验组接口**：

首先，你需要定义一些接口作为校验组。这些接口是空的，但它们用于标识不同的校验场景。


```java
public interface ValidationGroups {
    interface Insert {};
    interface Update {};
    interface Detail {};
    interface Delete {};
}
```
2. **在实体类中添加校验注解和分组**：

在你的实体类中，为需要校验的属性添加 JSR-303 校验注解，并使用 `groups` 属性指定它们所属的校验组。


```java
public class MyEntity {
    @NotNull(message = "ID 不能为空", groups = {ValidationGroups.Insert.class})
    private Long id;

    @Size(min = 5, max = 100, message = "名称长度必须在5到100之间", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String name;

    // 其他属性和方法...
}
```
3. **在 Controller 或 Service 中使用 `@Validated` 和分组**：

在你的 Controller 或 Service 方法中，使用 `@Validated` 注解，并通过 `value` 或 `groups` 属性指定要使用的校验组。


```java
@RestController
public class MyEntityController {

    @PostMapping("/add")
    public ResponseEntity<?> addEntity(@RequestBody @Validated(ValidationGroups.Insert.class) MyEntity entity) {
        // 处理插入逻辑...
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEntity(@RequestBody @Validated(ValidationGroups.Update.class) MyEntity entity) {
        // 处理更新逻辑...
        return ResponseEntity.ok().build();
    }
}
```
4. **处理校验异常**：

当校验失败时，Spring 会抛出一个 `MethodArgumentNotValidException` 异常。你可以使用 `@ControllerAdvice` 和 `@ExceptionHandler` 来全局处理这个异常，并返回友好的错误提示给用户。

这样，在不同的场景下，你就可以对同一个对象的不同属性进行不同的校验了。


```java
//如果不配置groups，则是全局适配
@NotNull(message = "ID 不能为空")

//在controller中不需要指定groups
@PostMapping("/add")
public ResponseEntity<?> addEntity(@RequestBody @Validated MyEntity entity) {
    // 处理插入逻辑...
    return ResponseEntity.ok().build();
}
```