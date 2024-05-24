package org.dee.processor;

import cn.hutool.core.util.StrUtil;
import com.google.auto.service.AutoService;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;
import lombok.SneakyThrows;
import org.dee.processor.strategy.FeignClientStrategy;
import org.dee.utils.JCTreeUtil;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Set;

@SupportedAnnotationTypes("org.springframework.cloud.openfeign.FeignClient")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FeignClientProcessor extends AbstractProcessor {

    private final static boolean enable = true;

    public final static String CLZ = "org.springframework.cloud.openfeign.FeignClient";
    private final static String pkg = "org.springframework.cloud.openfeign";
    private final static String clz = "FeignClient";
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
        messager.printMessage(Diagnostic.Kind.WARNING, "#FeignClientProcessor-start============");
        //获取使用@Feign注解的元素
        try {
            for (Element element : FeignClientStrategy.getFeignClientClasses(roundEnv, (Class<? extends Annotation>) Class.forName(CLZ))) {
                javax.lang.model.element.Name classSimpleName = element.getSimpleName();
                messager.printMessage(Diagnostic.Kind.WARNING, "#FeignClientProcessor-start============"+classSimpleName);

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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
                        List<JCTree.JCExpression> expressions = jcClassDecl.getImplementsClause();
                        if(expressions == null || expressions.size() == 0){
                            return;
                        }
                        //类是否继承了extendClass
                        String entityName = JCTreeUtil.getImplementClassArgument(jcClassDecl, "org.dee.framework.client.IClient", 0);
                        if(StrUtil.isNotEmpty(entityName)){
                            String bean = StrUtil.lowerFirst(entityName);
                            List<JCTree.JCAnnotation> annotations = jcClassDecl.mods.annotations;
                            List<JCTree.JCAnnotation> nil = List.nil();
                            for (int i = 0; i < annotations.size(); i++){
                                JCTree.JCAnnotation anno = annotations.get(i);
                                if(CLZ.equals(anno.type.toString())){
                                    List<Pair<Symbol.MethodSymbol, Attribute>> pairs = anno.attribute.values;
                                    if(pairs == null || pairs.size() == 0) {
                                        continue;
                                    }
                                    String name = "";
                                    String contextId = bean + "Client";
                                    String path = "/rest/" + bean;
                                    for(Pair<Symbol.MethodSymbol, Attribute> pair : pairs){
                                        if("name".equals(pair.fst.name.toString())){
                                            name = pair.snd.getValue().toString();
                                        }
                                        else if("contextId".equals(pair.fst.name.toString())){
                                            contextId = pair.snd.getValue().toString();
                                        }
                                        else if("path".equals(pair.fst.name.toString())){
                                            path = pair.snd.getValue().toString();
                                        }
                                    }
                                    //设置新的setNewFeignClientValue
                                    JCTree.JCAnnotation e = setNewFeignClientValue(name, contextId, path);
                                    nil = nil.append(e);
                                }else {
                                    nil = nil.append(anno);
                                }
                            }
                            jcClassDecl.mods.annotations = nil;
                        }
                    }
                }
                super.visitClassDef(jcClassDecl);
            }
        };
    }

    /**
     * 设置新的newControllerRequestMapping参数
     * @param name
     * @param contextId
     * @param path
     * @return
     */
    public JCTree.JCAnnotation setNewFeignClientValue(String name, String contextId, String path) {
        return treeMaker.Annotation(treeMaker.Ident(names.fromString(clz)),//注解名称
                List.of(
                        treeMaker.Exec(
                                treeMaker.Assign(
                                        //注解属性
                                        treeMaker.Ident(names.fromString("name")),
                                        //注解属性值
                                        treeMaker.Literal(name)
                                )
                        ).expr,
                        treeMaker.Exec(
                                treeMaker.Assign(
                                        //注解属性
                                        treeMaker.Ident(names.fromString("contextId")),
                                        //注解属性值
                                        treeMaker.Literal(contextId)
                                )
                        ).expr,
                        treeMaker.Exec(
                                treeMaker.Assign(
                                        //注解属性
                                        treeMaker.Ident(names.fromString("path")),
                                        //注解属性值
                                        treeMaker.Literal(path)
                                )
                        ).expr
                )
        );
    }

}
