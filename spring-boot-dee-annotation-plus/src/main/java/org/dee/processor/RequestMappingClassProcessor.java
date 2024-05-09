package org.dee.processor;

import cn.hutool.core.util.StrUtil;
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
import org.dee.test.utils.ControllerUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("org.springframework.web.bind.annotation.RequestMapping")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RequestMappingClassProcessor extends AbstractProcessor {

    private final static boolean enable = true;

    public final static String CLZ = "org.springframework.web.bind.annotation.RequestMapping";
    private final static String pkg = "org.springframework.web.bind.annotation";
    private final static String clz = "RequestMapping";
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    private Elements elementUtils;
    private Types typeUtils;

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

        this.elementUtils = processingEnv.getElementUtils();
        this.typeUtils = processingEnv.getTypeUtils();
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(!enable){
            return false;
        }
        messager.printMessage(Diagnostic.Kind.WARNING, "#RequestMappingClassProcessor-start============");
        for (Element element : roundEnv.getElementsAnnotatedWith(RequestMapping.class)) {
            //只处理在类上的注解
            if(element.getKind().isClass()) {
                String packageName = processingEnv.getElementUtils().getPackageOf(element).toString();
                javax.lang.model.element.Name classSimpleName = element.getSimpleName();
                messager.printMessage(Diagnostic.Kind.WARNING, "classSimpleName:" + packageName+"."+classSimpleName.toString());

                JCTree.JCIdent jcIdent = treeMaker.Ident(names.fromString(pkg));
                Name className = names.fromString(clz);
                JCTree.JCFieldAccess jcFieldAccess = treeMaker.Select(jcIdent, className);
                JCTree.JCImport anImport = treeMaker.Import(jcFieldAccess, false);

                messager.printMessage(Diagnostic.Kind.WARNING, element.getSimpleName());
                //导入注解类
                TreePath path = trees.getPath(element);
                JCTree.JCCompilationUnit jccu = (JCTree.JCCompilationUnit) path.getCompilationUnit();
                jccu.defs = jccu.defs.prepend(anImport);

                JCTree jcTree = trees.getTree(element);

                //写入新注解
                jcTree.accept(createNewAnnotation(classSimpleName.toString()));
            }
        }
        return false;
    }

    private String getControllerRequestMappingName(String controllerClassPath) {
        String baseWebControllerPath = "org.dee.framework.controller.BaseWebController";
        return ControllerUtil.createControllerRequestMappingName(baseWebControllerPath, controllerClassPath);
    }

    /**
     * 创建新的注解
     * @param className
     * @return
     */
    private TreeTranslator createNewAnnotation(String className) {
        return new TreeTranslator(){
            @Override
            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                //判断操作的是当前类
                if(className.equals(jcClassDecl.getSimpleName().toString())){
                    //判断有包含目标注解
                    if(jcClassDecl.mods.annotations.toString().contains(clz)){
                        String newControllerRequestMapping = ControllerUtil.createControllerRequestMappingName(jcClassDecl);
                        //判断能创建新的RequestMapping地址
                        if(StrUtil.isNotEmpty(newControllerRequestMapping)){
                            List<JCTree.JCAnnotation> annotations = jcClassDecl.mods.annotations;
                            List<JCTree.JCAnnotation> nil = List.nil();
                            for (int i = 0; i < annotations.size(); i++){
                                JCTree.JCAnnotation anno = annotations.get(i);
                                if(CLZ.equals(anno.type.toString())){
                                    JCTree.JCAnnotation e;
                                    e = treeMaker.Annotation(treeMaker.Ident(names.fromString(clz)),//注解名称
                                            List.of(treeMaker.Exec(treeMaker.Assign(treeMaker.Ident(names.fromString("value")),//注解属性
                                                    treeMaker.Literal(newControllerRequestMapping))).expr)//注解属性值
                                    );
                                    nil = nil.append(e);
                                }
                            }
                            //修改方法注解
                            jcClassDecl.mods.annotations = nil;
                        }
                    }
                }
                super.visitClassDef(jcClassDecl);
            }
        };
    }

}
