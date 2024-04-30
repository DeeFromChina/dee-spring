# demo
https://blog.csdn.net/a__int__/article/details/127447945

#### Q1:TreeMaker treeMaker = TreeMaker.instance(context);是什么意思

在Java注解处理（Annotation Processing）的上下文中，特别是当使用`javax.annotation.processing`和`javax.lang.model`包时，`TreeMaker`是一个工具类，用于在编译时构建抽象语法树（Abstract Syntax Tree, AST）的节点。AST是源代码的语法结构的树形表示，它对于许多编译器和代码分析工具来说是一个重要的中间表示。

`TreeMaker.instance(context)`是获取`TreeMaker`实例的一种常见方式，其中`context`是一个`ProcessingEnvironment`对象，它是注解处理器框架提供的一个接口，允许注解处理器访问关于正在处理的Java源代码的元信息和工具。

具体来说，`TreeMaker`允许你创建各种AST节点，比如表达式、语句、类定义等。这对于需要生成新的源代码或修改现有源代码的注解处理器来说非常有用。

这里是一个简化的例子，说明如何使用`TreeMaker`来创建一个简单的打印语句：

```java
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.tree.*;
import javax.lang.model.util.TreeMaker;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("your.package.HelloWorld")
public class HelloWorldProcessor extends AbstractProcessor {

    private TreeMaker treeMaker;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        treeMaker = TreeMaker.instance(processingEnv.getTypeUtils()); // 注意这里可能需要传递不同的参数，如context.getTreeUtilities()
    }

    // ... 其他注解处理器代码 ...

    private ExecutableElement findAnnotatedMethod(Set<? extends Element> elements) {
        // 遍历被注解的元素并查找方法（如果有的话）
        // ...
        return null; // 示例代码，返回null
    }

    private void generatePrintStatement(ExecutableElement method) {
        // 假设我们有方法的TreePath或我们可以从其他方式获取到方法的Tree
        // Tree methodTree = ...; // 这需要从其他地方获取

        // 使用TreeMaker创建一个打印语句
        Expression printlnExpr = treeMaker.Apply(
                null, // type arguments
                treeMaker.Select(
                    treeMaker.Select(
                        treeMaker.Ident("java.lang.System"),
                        "out"
                    ),
                    "println"
                ),
                List.of(treeMaker.Literal("Hello, World!"))
        );

        // 在这里，我们需要将printlnExpr插入到methodTree的某个位置，
        // 但这通常涉及到更复杂的AST操作，可能需要使用TreeScanner或TreePathScanner。

        // 注意：这个例子没有显示如何将新的表达式插入到现有方法中，
        // 因为这通常涉及更复杂的AST变换和可能的错误处理。
    }

    // ... 其他注解处理器代码 ...
}
```

**注意**：在上面的代码中，`TreeMaker.instance(processingEnv.getTypeUtils())`可能不是获取`TreeMaker`实例的正确方式。`TreeMaker`通常是通过`ProcessingEnvironment`的`getTreeUtilities()`方法获取的，但具体的API可能会根据Java版本和使用的注解处理工具（如javac或Eclipse JDT）而有所不同。你应该查阅你正在使用的工具的文档以获取正确的API使用方法。

在Javac中，你可能需要这样做：

```java
treeMaker = TreeMaker.instance(processingEnv.getJavacTrees().getTreeMaker());
```

而在Eclipse JDT中，API可能完全不同。

#### Q2:treeMaker.Select()是什么意思
在Java的编译器树API（通常与注解处理相关）中，`TreeMaker`是一个用于创建和修改抽象语法树（AST）的工具类。`TreeMaker`中的`Select`方法用于创建一个`MemberSelectTree`节点，这代表了源代码中的点（`.`）选择表达式。

具体来说，当你看到`treeMaker.Select(expression, name)`这样的调用时，它通常意味着你想要创建一个表示`expression.name`这样的表达式的AST节点。

其中：

- `expression`是一个已经存在的AST节点，代表了你想要从中选择成员（字段、方法、内部类等）的表达式。
- `name`是一个`Name`对象，代表了你想要选择的成员的名称。

例如，假设你有一个`ExpressionTree`代表`java.lang.System`，并且你想要从这个表达式中选择`out`字段，你可以这样做：

```java
ExpressionTree systemExpr = ...; // 假设这是代表"java.lang.System"的表达式
Name outName = processingEnv.getElementUtils().getName("out");
MemberSelectTree systemOutExpr = treeMaker.Select(systemExpr, outName);
```

现在，`systemOutExpr`代表`java.lang.System.out`这个表达式。

注意：`treeMaker`的获取方式可能依赖于你使用的编译器实现（如Javac或Eclipse JDT）。在Javac中，你通常会通过`ProcessingEnvironment`的`getJavacTrees().getTreeMaker()`来获取`TreeMaker`实例。

此外，使用`TreeMaker`创建或修改AST节点后，你通常还需要将这些节点正确地插入到现有的AST中，这可能需要更复杂的操作，如使用`TreeScanner`或`TreePathScanner`来遍历和修改AST。在修改AST时，请确保你了解所做更改的潜在影响，并妥善处理可能出现的错误和异常。

#### Q3:treeMaker.Apply()是什么意思
在Java的编译器树API（特别是在Javac的注解处理中）中，`TreeMaker`的`Apply`方法用于创建表示方法调用的`MethodInvocationTree`节点。这个方法调用可以是静态的、非静态的、构造器调用或数组访问。

`Apply`方法的基本形式如下：

```java
MethodInvocationTree Apply(
    List<? extends TypeMirror> typeArguments,
    ExpressionTree methodSelect,
    List<? extends ExpressionTree> arguments
)
```

参数说明：

- `typeArguments`（可选）：表示泛型方法调用的类型参数列表。如果方法调用不涉及泛型类型参数，则可以传递`null`或空列表。
- `methodSelect`：表示被调用的方法或构造器的选择器。这通常是一个`MemberSelectTree`（如`obj.method()`中的`obj.method`）或`IdentifierTree`（如`method()`中的`method`）。
- `arguments`：表示传递给方法或构造器的参数列表。

使用`Apply`方法的一个例子是创建一个表示`System.out.println("Hello, World!")`的方法调用：

```java
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.tree.*;
import javax.lang.model.util.TreeMaker;
import javax.tools.JavaFileObject;
import java.util.Arrays;

// ... 其他必要的注解和类定义 ...

@Override
public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    TreeMaker treeMaker = TreeMaker.instance(processingEnv.getTreeUtilities());

    // 创建 System.out
    ExpressionTree systemOutExpr = treeMaker.Select(
        treeMaker.Ident("java.lang.System"), // java.lang.System
        processingEnv.getElementUtils().getName("out") // out
    );

    // 创建 println 方法调用
    MethodInvocationTree printlnCall = treeMaker.Apply(
        null, // 没有类型参数
        treeMaker.Select(systemOutExpr, processingEnv.getElementUtils().getName("println")), // System.out.println
        Arrays.asList(treeMaker.Literal("Hello, World!")) // 传递一个字符串字面量作为参数
    );

    // 此时 printlnCall 代表 System.out.println("Hello, World!") 这个方法调用
    // ... 可以将 printlnCall 添加到 AST 的某个位置 ...
}

// ... 其他必要的注解处理器代码 ...
```

请注意，这个示例中的`TreeMaker.instance(processingEnv.getTreeUtilities())`可能不是获取`TreeMaker`的正确方式。在Javac中，你应该使用`processingEnv.getJavacTrees().getTreeMaker()`来获取`TreeMaker`实例。上面的代码是为了演示`Apply`方法的使用而简化的。

另外，一旦你创建了新的AST节点，你通常需要将这些节点正确地插入到现有的AST中，这可能需要使用`TreeScanner`、`TreePathScanner`或其他工具来遍历和修改AST。

