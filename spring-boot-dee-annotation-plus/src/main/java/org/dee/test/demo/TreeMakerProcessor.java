//package org.dee.processor.test.demo;
//
//import com.google.auto.service.AutoService;
//import com.sun.tools.javac.model.JavacElements;
//import com.sun.tools.javac.processing.JavacProcessingEnvironment;
//import com.sun.tools.javac.tree.JCTree;
//import com.sun.tools.javac.tree.TreeMaker;
//import com.sun.tools.javac.util.Context;
//import com.sun.tools.javac.util.List;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import java.util.Iterator;
//import java.util.Set;
//
//@SupportedAnnotationTypes("org.dee.processor.test.demo.HelloWorld" )
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@AutoService(Processor.class)
//public class TreeMakerProcessor extends AbstractProcessor {
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        Context context = ((JavacProcessingEnvironment) this.processingEnv).getContext();
//        JavacElements elementUtils = (JavacElements) this.processingEnv.getElementUtils();
//        //获取语法树
//        TreeMaker treeMaker = TreeMaker.instance(context);
//        JCTree.JCMethodDecl jcMethodDecl;
//        for (
//                Iterator var7 = roundEnv.getElementsAnnotatedWith(HelloWorld.class).iterator();
//                var7.hasNext();
//                jcMethodDecl.body = treeMaker.Block(
//                        0L,
//                        List.of(
//                                treeMaker.Exec(
//                                        treeMaker.Apply(
//                                                List.nil(),
//                                                treeMaker.Select(
//                                                        //创建一个MemberSelectTree,参数1是存在的AST节点，参数2是要创建一个节点
//                                                        treeMaker.Select(
//                                                                treeMaker.Ident(
//                                                                        elementUtils.getName("System")
//                                                                ),
//                                                                elementUtils.getName("out")
//                                                        ),
//                                                        elementUtils.getName("println")
//                                                ),
//                                                List.of(
//                                                        treeMaker.Literal("这是HelloWorld打印的")
//                                                )
//                                        )
//                                ),
//                                jcMethodDecl.body
//                        )
//                )
//        ) {
//            Element element = (Element) var7.next();
//            jcMethodDecl = (JCTree.JCMethodDecl) elementUtils.getTree(element);
//            treeMaker.pos = jcMethodDecl.pos;
//        }
//        return false;
//    }
//}
