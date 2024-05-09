package org.dee.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("org.springframework.web.bind.annotation.RequestMapping")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RequestMappingMethodProcessor extends AbstractProcessor {

    private final static boolean enable = false;

    public final static String CLZ = "org.springframework.web.bind.annotation.RequestMapping";
    private final static String pkg = "org.springframework.web.bind.annotation";
    private final static String clz = "RequestMapping";
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    //打印日志
    private Messager messager;

    //文件生成器，类/资源等最后需要生成的文件
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);

        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(!enable){
            return false;
        }
        messager.printMessage(Diagnostic.Kind.WARNING, "#RequestMappingMethodProcessor-start============");
        for (Element element : roundEnv.getElementsAnnotatedWith(RequestMapping.class)) {
            //只处理在方法上的注解
            if(element.getKind() == ElementKind.METHOD){
                javax.lang.model.element.Name methodName = element.getSimpleName();
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();

                JCTree.JCIdent jcIdent = treeMaker.Ident(names.fromString(pkg));
                Name className = names.fromString(clz);
                JCTree.JCFieldAccess jcFieldAccess = treeMaker.Select(jcIdent, className);
                JCTree.JCImport anImport = treeMaker.Import(jcFieldAccess, false);

                messager.printMessage(Diagnostic.Kind.WARNING, typeElement.getSimpleName());
                //导入注解类
                TreePath path = trees.getPath(typeElement);
                JCTree.JCCompilationUnit jccu = (JCTree.JCCompilationUnit) path.getCompilationUnit();
                jccu.defs = jccu.defs.prepend(anImport);

                JCTree jcTree = trees.getTree(typeElement);

                jcTree.accept(createNewAnnotation(methodName.toString()));
            }
        }
        return false;
    }

    /**
     * 创建新的注解
     * @param methodName
     * @return
     */
    private TreeTranslator createNewAnnotation(String methodName) {
        return new TreeTranslator(){
            @Override
            public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                if(methodName.equals(jcMethodDecl.getName().toString())){
                    if(jcMethodDecl.mods.annotations.toString().contains("RequestMapping")){
                        List<JCTree.JCAnnotation> annotations = jcMethodDecl.mods.annotations;
                        List<JCTree.JCAnnotation> nil = List.nil();
                        for (int i = 0; i < annotations.size(); i++){
                            JCTree.JCAnnotation anno = annotations.get(i);
                            if(CLZ.equals(anno.type.toString())){
                                JCTree.JCAnnotation e;
                                e = treeMaker.Annotation(treeMaker.Ident(names.fromString(clz)),//注解名称
                                        List.of(treeMaker.Exec(treeMaker.Assign(treeMaker.Ident(names.fromString("value")),//注解属性
                                                treeMaker.Literal("/b"))).expr)//注解属性值
                                );
                                nil = nil.append(e);
                            }
                        }
                        //修改方法注解
                        jcMethodDecl.mods.annotations = nil;
                    }
                }

                super.visitMethodDef(jcMethodDecl);
            }
        };
    }

}
