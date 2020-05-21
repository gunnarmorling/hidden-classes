package dev.morling.demos.hiddenclass;

import static java.lang.invoke.MethodType.methodType;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Very basic property accessor generator; doesn't take care of primitive boxing etc.
 */
public class PropertyAccessorFactory implements Opcodes {

    public static <T> PropertyAcessor<T> getPropertyAccessor(Class<T> clazz) {
        Lookup lookup;
        try {
            lookup = MethodHandles.privateLookupIn(clazz, MethodHandles.lookup())
                    .defineHiddenClass(PropertyAccessorFactory.generatePropertyAccessor(clazz), false);

            MethodHandle ctor = lookup.findConstructor(lookup.lookupClass(), methodType(void.class));
            return (PropertyAcessor<T>) ctor.invoke();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static byte[] generatePropertyAccessor(Class<?> clazz) throws Exception {
        String className = clazz.getCanonicalName().replaceAll("\\.", "/");
        String descriptor = clazz.descriptorString();
        String accessorClassName = clazz.getCanonicalName().replaceAll("\\.", "/") + "PropertyAccessor";
        String accessorDescriptor = "L" + clazz.getCanonicalName().replaceAll("\\.", "/") + "PropertyAccessor" + ";";

        PropertyDescriptor[] properties = Introspector.getBeanInfo(clazz).getPropertyDescriptors();

        ClassWriter classWriter = new ClassWriter(0);
        MethodVisitor methodVisitor;

        classWriter.visit(V13, ACC_PUBLIC | ACC_SUPER, accessorClassName,
                "Ljava/lang/Object;Ldev/morling/demos/hiddenclass/PropertyAcessor<" + descriptor + ">;",
                "java/lang/Object", new String[] { "dev/morling/demos/hiddenclass/PropertyAcessor" });

        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(3, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLocalVariable("this", accessorDescriptor, null,
                    label0, label1, 0);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "getValue",
                    "(" + descriptor + "Ljava/lang/String;)Ljava/lang/Object;", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);

            for (PropertyDescriptor propertyDescriptor : properties) {
                if (propertyDescriptor.getName().equals("class")) {
                    continue;
                }

                methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                methodVisitor.visitVarInsn(ALOAD, 2);
                methodVisitor.visitLdcInsn(propertyDescriptor.getName());
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                Label label1 = new Label();
                methodVisitor.visitJumpInsn(IFEQ, label1);
                Label label2 = new Label();
                methodVisitor.visitLabel(label2);
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, className, propertyDescriptor.getReadMethod().getName(),
                        "()" + propertyDescriptor.getReadMethod().getReturnType().descriptorString(), false);
                methodVisitor.visitInsn(ARETURN);
                methodVisitor.visitLabel(label1);
            }

//            // name
//            methodVisitor.visitLineNumber(7, label0);
//            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
//            methodVisitor.visitVarInsn(ALOAD, 2);
//            methodVisitor.visitLdcInsn("name");
//            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
//            Label label1 = new Label();
//            methodVisitor.visitJumpInsn(IFEQ, label1);
//            Label label2 = new Label();
//            methodVisitor.visitLabel(label2);
//            methodVisitor.visitLineNumber(8, label2);
//            methodVisitor.visitVarInsn(ALOAD, 1);
//            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, className, "getName",
//                    "()Ljava/lang/String;", false);
//            methodVisitor.visitInsn(ARETURN);
//            methodVisitor.visitLabel(label1);
//
//            // age
//            methodVisitor.visitLineNumber(11, label1);
//            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
//            methodVisitor.visitVarInsn(ALOAD, 2);
//            methodVisitor.visitLdcInsn("age");
//            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
//            Label label3 = new Label();
//            methodVisitor.visitJumpInsn(IFEQ, label3);
//            Label label4 = new Label();
//            methodVisitor.visitLabel(label4);
//            methodVisitor.visitLineNumber(12, label4);
//            methodVisitor.visitVarInsn(ALOAD, 1);
//            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, className, "getAge", "()I",
//                    false);
//            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;",
//                    false);
//            methodVisitor.visitInsn(ARETURN);
//            methodVisitor.visitLabel(label3);
//
//            // address
//            methodVisitor.visitLineNumber(15, label3);
//            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
//            methodVisitor.visitVarInsn(ALOAD, 2);
//            methodVisitor.visitLdcInsn("address");
//            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
//            Label label5 = new Label();
//            methodVisitor.visitJumpInsn(IFEQ, label5);
//            Label label6 = new Label();
//            methodVisitor.visitLabel(label6);
//            methodVisitor.visitLineNumber(16, label6);
//            methodVisitor.visitVarInsn(ALOAD, 1);
//            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, className, "getAddress",
//                    "()Ljava/lang/String;", false);
//            methodVisitor.visitInsn(ARETURN);
//            methodVisitor.visitLabel(label5);

//            methodVisitor.visitLineNumber(19, label5);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitLdcInsn("Unknown property: ");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V",
                    false);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;",
                    false);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>",
                    "(Ljava/lang/String;)V", false);
            methodVisitor.visitInsn(ATHROW);
            Label label7 = new Label();
            methodVisitor.visitLabel(label7);
            methodVisitor.visitLocalVariable("this", accessorDescriptor, null,
                    label0, label7, 0);
            methodVisitor.visitLocalVariable("instance", descriptor, null, label0,
                    label7, 1);
            methodVisitor.visitLocalVariable("property", "Ljava/lang/String;", null, label0, label7, 2);
            methodVisitor.visitMaxs(5, 3);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_BRIDGE | ACC_SYNTHETIC, "getValue",
                    "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(1, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, className);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, accessorClassName,
                    "getValue", "(" + descriptor + "Ljava/lang/String;)Ljava/lang/Object;",
                    false);
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitMaxs(3, 3);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }
}
