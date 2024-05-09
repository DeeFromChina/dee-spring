//package org.dee.processor.test.demo;
//
//import com.google.auto.service.AutoService;
//import com.google.common.io.ByteStreams;
//import com.sun.source.util.TreePath;
//import com.sun.tools.javac.api.JavacTrees;
//import com.sun.tools.javac.code.Symbol;
//import com.sun.tools.javac.processing.JavacProcessingEnvironment;
//import com.sun.tools.javac.tree.JCTree;
//import com.sun.tools.javac.tree.TreeMaker;
//import com.sun.tools.javac.tree.TreeTranslator;
//import com.sun.tools.javac.util.Context;
//import com.sun.tools.javac.util.List;
//import com.sun.tools.javac.util.Name;
//import com.sun.tools.javac.util.Names;
//import jdk.internal.org.objectweb.asm.AnnotationVisitor;
//import jdk.internal.org.objectweb.asm.ClassReader;
//import jdk.internal.org.objectweb.asm.ClassVisitor;
//import jdk.internal.org.objectweb.asm.ClassWriter;
//import org.springframework.asm.Opcodes;
//
//import javax.annotation.processing.*;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.ElementKind;
//import javax.lang.model.element.ExecutableElement;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//import javax.tools.JavaFileObject;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@SupportedAnnotationTypes("org.dee.processor.test.demo.HelloWorld")
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@AutoService(Processor.class)
//public class TestAnnotationProcessor extends AbstractProcessor {
//
//    //打印日志
//    private Messager messager;
//
//    //文件生成器，类/资源等最后需要生成的文件
//    private Filer filer;
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        System.out.println("===============init=================");
//
//        this.messager = processingEnv.getMessager();
//        this.filer = processingEnv.getFiler();
//
//        this.trees = JavacTrees.instance(processingEnv);
//        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
//        this.treeMaker = TreeMaker.instance(context);
//        this.names = Names.instance(context);
//    }
//
//    public final static String CLZ = "org.dee.processor.test.demo.HelloWorld";
//    private static Map<String, String> tis = new ConcurrentHashMap<>();
//    private JavacTrees trees;
//    private TreeMaker treeMaker;
//    private Names names;
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        String pkg = "org.springframework.web.bind.annotation";
//        String clz = "RequestMapping";
//
//
//        for (Element element : roundEnv.getElementsAnnotatedWith(HelloWorld.class)) {
//            javax.lang.model.element.Name classSimpleName = element.getSimpleName();
//            messager.printMessage(Diagnostic.Kind.WARNING, "classSimpleName:" + classSimpleName);
//            //TypeElement typeElement = (TypeElement) element.getEnclosingElement();
//            Element typeElement = element.getEnclosingElement();
//
//            if(element.getEnclosingElement() instanceof Symbol.PackageSymbol){
//                typeElement = element;
//            }
//
//            //String typeName = typeElement.getSimpleName().toString();
//            //String list = tis.get(typeName);
//            //if(null == list) {
//                JCTree.JCIdent jcIdent = treeMaker.Ident(names.fromString(pkg));
//                Name className = names.fromString(clz);
//                JCTree.JCFieldAccess jcFieldAccess = treeMaker.Select(jcIdent, className);
//                JCTree.JCImport anImport = treeMaker.Import(jcFieldAccess, false);
//
//                messager.printMessage(Diagnostic.Kind.WARNING, typeElement.getSimpleName());
//                //导入注解类
//                TreePath path = trees.getPath(typeElement);
//                JCTree.JCCompilationUnit jccu = (JCTree.JCCompilationUnit) path.getCompilationUnit();
//                jccu.defs = jccu.defs.prepend(anImport);
//
//                //tis.put(typeName, "Scheduled");
//            //}
//
//            JCTree jcTree = trees.getTree(typeElement);
//
//            jcTree.accept(new TreeTranslator(){
//                @Override
//                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
//                    if(classSimpleName.toString().equals(jcClassDecl.getSimpleName().toString())){
//                        if(jcClassDecl.mods.annotations.toString().contains("HelloWorld")){
//                            List<JCTree.JCAnnotation> annotations = jcClassDecl.mods.annotations;
//                            List<JCTree.JCAnnotation> nil = List.nil();
//                            for (int i = 0; i < annotations.size(); i++){
//                                JCTree.JCAnnotation anno = annotations.get(i);
//                                if(CLZ.equals(anno.type.toString())){
//                                    JCTree.JCAnnotation e;
//                                    e = treeMaker.Annotation(treeMaker.Ident(names.fromString(clz)),//注解名称
//                                            List.of(treeMaker.Exec(treeMaker.Assign(treeMaker.Ident(names.fromString("value")),//注解属性
//                                                    treeMaker.Literal("/b"))).expr)//注解属性值
//                                    );
//                                    nil = nil.append(e);
//                                }
//                            }
//                            //修改方法注解
//                            jcClassDecl.mods.annotations = nil;
//                        }
//                    }
//
//                    super.visitClassDef(jcClassDecl);
//                }
//            });
//        }
//        return false;
//    }
//
//    //@Override
//    public boolean process5(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        String pkg = "org.springframework.web.bind.annotation";
//        String clz = "RequestMapping";
//
//
//        for (Element element : roundEnv.getElementsAnnotatedWith(HelloWorld.class)) {
//            javax.lang.model.element.Name methodName = element.getSimpleName();
//            messager.printMessage(Diagnostic.Kind.WARNING, "methodName:" + methodName);
//            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
//
//            String typeName = typeElement.getQualifiedName().toString();
//            String list = tis.get(typeName);
//            if(null == list) {
//                JCTree.JCIdent jcIdent = treeMaker.Ident(names.fromString(pkg));
//                Name className = names.fromString(clz);
//                JCTree.JCFieldAccess jcFieldAccess = treeMaker.Select(jcIdent, className);
//                JCTree.JCImport anImport = treeMaker.Import(jcFieldAccess, false);
//
//                messager.printMessage(Diagnostic.Kind.WARNING, typeElement.getSimpleName());
//                //导入注解类
//                TreePath path = trees.getPath(typeElement);
//                JCTree.JCCompilationUnit jccu = (JCTree.JCCompilationUnit) path.getCompilationUnit();
//                jccu.defs = jccu.defs.prepend(anImport);
//
//                tis.put(typeName, "Scheduled");
//            }
//
//            JCTree jcTree = trees.getTree(typeElement);
//
//            jcTree.accept(new TreeTranslator(){
//                @Override
//                public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
//                    if(methodName.toString().equals(jcMethodDecl.getName().toString())){
//                        if(jcMethodDecl.mods.annotations.toString().contains("HelloWorld")){
//                            List<JCTree.JCAnnotation> annotations = jcMethodDecl.mods.annotations;
//                            List<JCTree.JCAnnotation> nil = List.nil();
//                            for (int i = 0; i < annotations.size(); i++){
//                                JCTree.JCAnnotation anno = annotations.get(i);
//                                if(CLZ.equals(anno.type.toString())){
//                                    JCTree.JCAnnotation e;
//                                    e = treeMaker.Annotation(treeMaker.Ident(names.fromString(clz)),//注解名称
//                                            List.of(treeMaker.Exec(treeMaker.Assign(treeMaker.Ident(names.fromString("value")),//注解属性
//                                                    treeMaker.Literal("/b"))).expr)//注解属性值
//                                            );
//                                    nil = nil.append(e);
//                                }
//                            }
//                            //修改方法注解
//                            jcMethodDecl.mods.annotations = nil;
//                        }
//                    }
//
//
//                    super.visitMethodDef(jcMethodDecl);
//                }
//            });
//        }
//        return false;
//    }
//
//    //@Override
//    public boolean process3(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        for (Element element : roundEnv.getElementsAnnotatedWith(HelloWorld.class)) {
//            if(element.getKind() != ElementKind.METHOD){
//                continue;
//            }
//            ExecutableElement method = (ExecutableElement) element;
//
//            //获取方法所在类的信息
//            TypeElement classElement = (TypeElement) method.getEnclosingElement();
//            String packageName = processingEnv.getElementUtils().getPackageOf(classElement).toString();
//            String className = classElement.getSimpleName().toString();
//
//            try {
//                JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + className);
//                PrintWriter writer = new PrintWriter(sourceFile.openWriter());
//                writer.println("package " + packageName + ";\n");
//                writer.println("public class " + className + "{\n");
//                writer.println("private " + className + "(){}\n");
//                writer.println("}\n");
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//    //@Override
//    public boolean process2(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        System.out.println("===============开始了=================");
//        for (Element element : roundEnv.getRootElements()) {
//            String className = element.getSimpleName().toString();
//            if(!className.equals("Main")){
//                continue;
//            }
//            messager.printMessage(Diagnostic.Kind.WARNING, element.getSimpleName().toString());
//            try {
//                // 获取类文件的字节码
//                byte[] bytecode = getBytecode(processingEnv, element);
//
//                messager.printMessage(Diagnostic.Kind.WARNING, "bytecode"+String.valueOf(bytecode.length));
//
//                // 使用 ASM 读取字节码
//                ClassReader classReader = new ClassReader(bytecode);
//                ClassWriter classWriter = new ClassWriter(classReader, 0);
//
//                // 创建 ClassVisitor 来修改注解的属性
//                TestAnnotationClassVisitor classVisitor = new TestAnnotationClassVisitor(classWriter);
//                classReader.accept(classVisitor, 0);
//
//                // 将修改后的字节码写入新类文件
//                JavaFileObject fileObject = filer.createClassFile((CharSequence) element);
//                try (OutputStream out = fileObject.openOutputStream()) {
//                    out.write(classWriter.toByteArray());
//                }
//            } catch (IOException e) {
//                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
//            }
//        }
//        return true;
//    }
//
//    private byte[] getBytecode(ProcessingEnvironment processingEnv, Element element) throws IOException {
//        // 确保element是类
//        if (!(element instanceof TypeElement)) {
//            throw new IllegalArgumentException("The element must be a class");
//        }
//
//        TypeElement typeElement = (TypeElement) element;
//        // 获取类的全限定名作为CharSequence
//        CharSequence className = typeElement.getQualifiedName();
//
//        // 创建JavaFileObject，这将触发类的编译
//        JavaFileObject classFile = processingEnv.getFiler().createClassFile(className);
//
//        // 读取字节码
//        byte[] bytecode;
//        try (InputStream inputStream = classFile.openInputStream()) {
//            bytecode = ByteStreams.toByteArray(inputStream);
//        }
//
//        return bytecode;
//    }
//
//    private class TestAnnotationClassVisitor extends ClassVisitor {
//        public TestAnnotationClassVisitor(ClassWriter cw) {
//            super(Opcodes.ASM9, cw); // 根据使用的ASM版本修改这里的ASM版本号
//        }
//
//        @Override
//        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//            // 检查是否是我们想要修改的注解org.dee.processor
//            if (desc.equals("Lorg/dee/processor/HelloWorld;") || desc.equals("org/dee/processor/HelloWorld")) {
//                // 返回一个自定义的 AnnotationVisitor 来修改注解的属性
//                return new AnnotationVisitor(Opcodes.ASM9) {
//                    @Override
//                    public void visit(String name, Object value) {
//                        if ("value".equals(name)) {
//                            // 修改注解的属性
//                            super.visit(name, "/b");
//                        } else {
//                            super.visit(name, value);
//                        }
//                    }
//                };
//            }
//            return super.visitAnnotation(desc, visible);
//        }
//    }
//}
