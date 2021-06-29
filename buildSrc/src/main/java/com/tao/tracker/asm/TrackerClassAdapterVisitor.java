package com.tao.tracker.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

import groovyjarjarasm.asm.Opcodes;

public class TrackerClassAdapterVisitor extends ClassVisitor {

    private final List<String> nameList;
    private final List<String> descriptorList;

    public TrackerClassAdapterVisitor(ClassVisitor classVisitor, List<String> nameList, List<String> descriptorList) {
        super(Opcodes.ASM7, classVisitor);
        this.nameList = nameList;
        this.descriptorList = descriptorList;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (isTransformVisitMethod(name, descriptor)){
            return new TrackerMethodVisitor(Opcodes.ASM7, methodVisitor, access, name, descriptor);
        }
        return methodVisitor;
    }

    public boolean isTransformVisitMethod(String name, String descriptor) {
        return nameList.contains(name) && descriptorList.contains(descriptor);
    }
}
