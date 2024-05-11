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
import lombok.SneakyThrows;
import org.dee.processor.strategy.RequestMappingClassStrategy;
import org.dee.utils.ControllerUtil;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
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

    @SneakyThrows
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(!enable){
            return false;
        }
        messager.printMessage(Diagnostic.Kind.WARNING, "#RequestMappingClassProcessor-start============");
        //获取使用@RequestMapping注解的元素
        for (Element element : RequestMappingClassStrategy.getRequestMappingClasses(roundEnv, (Class<? extends Annotation>) Class.forName(CLZ))) {
            //只处理在类上的注解
            if(RequestMappingClassStrategy.isClass(element)) {
                //String packageName = processingEnv.getElementUtils().getPackageOf(element).toString();
                javax.lang.model.element.Name classSimpleName = element.getSimpleName();

                JCTree.JCIdent jcIdent = treeMaker.Ident(names.fromString(pkg));
                Name className = names.fromString(clz);
                JCTree.JCFieldAccess jcFieldAccess = treeMaker.Select(jcIdent, className);
                JCTree.JCImport anImport = treeMaker.Import(jcFieldAccess, false);

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
                            //是否存在修改情况
                            boolean hasModify = false;
                            for (int i = 0; i < annotations.size(); i++){
                                JCTree.JCAnnotation anno = annotations.get(i);
                                if(CLZ.equals(anno.type.toString())){
                                    //@RequestMapping注解没有传入参数，才赋予它新的注解参数
                                    if(!RequestMappingClassStrategy.annotationHasParam(anno)){
                                        //设置新的newControllerRequestMapping参数
                                        JCTree.JCAnnotation e = setNewControllerRequestMappingValue(newControllerRequestMapping);
                                        nil = nil.append(e);
                                        hasModify = true;
                                    }
                                }else {
                                    nil = nil.append(anno);
                                }
                            }
                            //修改方法注解
                            if(hasModify){
                                jcClassDecl.mods.annotations = nil;
                            }
                        }
                    }
                }
                super.visitClassDef(jcClassDecl);
            }
        };
    }

    /**
     * 设置新的newControllerRequestMapping参数
     * @param newControllerRequestMapping
     * @return
     */
    public JCTree.JCAnnotation setNewControllerRequestMappingValue(String newControllerRequestMapping) {
        return treeMaker.Annotation(treeMaker.Ident(names.fromString(clz)),//注解名称
                List.of(treeMaker.Exec(treeMaker.Assign(treeMaker.Ident(names.fromString("value")),//注解属性
                        treeMaker.Literal(newControllerRequestMapping))).expr)//注解属性值
        );
    }

}
