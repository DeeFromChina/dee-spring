package org.dee.agent.classtranformer;

import aj.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

public class TimeStatisticsAdapter extends AdviceAdapter {

    private String descriptor;

    private String name;

    private int parameterCount;

    protected TimeStatisticsAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
        this.name = name;
        this.descriptor = descriptor;
        this.parameterCount = Type.getArgumentTypes(descriptor).length;
    }

    @Override
    protected void onMethodEnter() {
        //进入函数时调用TimeStatistics的静态方法start
        super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/dee/classtranformer/TimeStatistics", "start", "()V", false);
//        super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/dee/classtranformer/TimeStatistics", "printArguments", "([Ljava/lang/Object;)V", false);
        super.onMethodEnter();
    }

    @Override
    protected void onMethodExit(int opcode) {
        //退出函数时调用TimeStatistics的静态方法end
        super.onMethodExit(opcode);
        super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/dee/classtranformer/TimeStatistics", "end", "()V", false);
    }

//    @Override
//    public void visitCode() {
//        super.visitCode();
//        // 插入调用printArguments的代码
//        // 首先，将参数压入操作数栈
//        super.visitVarInsn(Opcodes.ILOAD, 0); // 加载第一个参数
//        super.visitVarInsn(Opcodes.ILOAD, 1); // 加载第二个参数
//
//        // 创建数组来存储参数
//        super.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
//
//        // 数组长度为2
//        super.visitInsn(2);
//
//        // 复制数组引用
//        super.visitInsn(Opcodes.DUP);
//
//        // 将第一个参数放入数组的第一个位置
//        super.visitInsn(0);
//        super.visitVarInsn(Opcodes.ILOAD, 0);
//        super.visitInsn(Opcodes.AASTORE);
//
//        // 将第二个参数放入数组的第二个位置
//        super.visitInsn(1);
//        super.visitVarInsn(Opcodes.ILOAD, 1);
//        super.visitInsn(Opcodes.AASTORE);
//
//        // 调用printArguments方法
//        super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/dee/classtranformer/TimeStatistics", "printArguments", "([Ljava/util/Map;)Lcn/sunline/pcmc/rpc/entity/ResponseResult;", false);
//    }

//    @Override
//    public void visitVarInsn(int opcode, int var) {
//        System.out.println("visitVarInsn name:" + name);
//        System.out.println("visitVarInsn descriptor:" + descriptor);
//        System.out.println("opcode:" + opcode + " var:" + var);
//        super.visitVarInsn(opcode, var);



//        if (opcode == Opcodes.ILOAD && var == 0) {
//            // 假设var == 0 表示第一个参数，这里可以根据实际情况调整
//            // 这里可以执行一些操作，比如打印参数值
//            System.out.println("The first parameter is: " + var);
//        }
//        super.visitVarInsn(opcode, var);
//    }

}
