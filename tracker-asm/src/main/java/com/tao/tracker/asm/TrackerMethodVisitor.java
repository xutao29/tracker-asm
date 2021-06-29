package com.tao.tracker.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

class TrackerMethodVisitor extends AdviceAdapter {

    protected TrackerMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();

        visitVarInsn(Opcodes.ALOAD, 1);
        visitMethodInsn(Opcodes.INVOKESTATIC, "com/taoyixun/tracker/Tracker", "onClick", "(Landroid/view/View;)V", false);
    }
}