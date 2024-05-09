//package org.dee.processor.test.classtranformer;
//
//import aj.org.objectweb.asm.Type;
//import jdk.internal.org.objectweb.asm.MethodVisitor;
//import jdk.internal.org.objectweb.asm.Opcodes;
//import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;
//
//public class MyAdapter extends AdviceAdapter {
//
//    private String descriptor;
//
//    private String name;
//
//    private int parameterCount;
//
//    protected MyAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
//        super(api, methodVisitor, access, name, descriptor);
//        this.name = name;
//        this.descriptor = descriptor;
//        this.parameterCount = Type.getArgumentTypes(descriptor).length;
//    }
//
//    @Override
//    protected void onMethodEnter() {
//        //进入函数时调用TimeStatistics的静态方法start
//        super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/dee/processor/test/classtranformer/TimeStatistics", "start", "()V", false);
////        super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/dee/classtranformer/TimeStatistics", "printArguments", "([Ljava/lang/Object;)V", false);
//        super.onMethodEnter();
//    }
//
//    @Override
//    protected void onMethodExit(int opcode) {
//        //退出函数时调用TimeStatistics的静态方法end
//        super.onMethodExit(opcode);
//        super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/dee/processor/test/classtranformer/TimeStatistics", "end", "()V", false);
//    }
//
//}
