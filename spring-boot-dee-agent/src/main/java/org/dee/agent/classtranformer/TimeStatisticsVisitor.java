package org.dee.agent.classtranformer;


import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class TimeStatisticsVisitor extends ClassVisitor {

    public TimeStatisticsVisitor(int api, ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        System.out.println("desc"+desc);
        if(name.equals("<init>")){
            return mv;
        }
        return new TimeStatisticsAdapter(api, mv, access, name, desc);
    }

}
