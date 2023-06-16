package org.softauto.core;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import org.softauto.espl.Espl;

import java.io.IOException;

public class ClassCloner {

    ClassPool pool = ClassPool.getDefault();
    CtClass originalClass;
    CtClass clonedClass;
    String annotationToFilter;
    String condition;

    public ClassCloner clone(String classFullName){
        try {

           clonedClass = pool.getAndRename(classFullName, classFullName+"Clone");
           //originalClass = pool.get(classFullName);
          // clonedClass.setSuperclass(originalClass);
           // clonedClass.writeFile();
           // originalClass = pool.get(classFullName);
           // clonedClass = pool.makeClass(classFullName+"Clone");

            if(clonedClass.isFrozen()){
                clonedClass.defrost();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Class getClonedClass(String type) throws CannotCompileException, NotFoundException, IOException {
        try {
            Class o = Class.forName(type,false,ClassLoader.getSystemClassLoader());
            Class c = clonedClass.toClass(ClassLoader.getSystemClassLoader(),o.getProtectionDomain());
            return c;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ClassCloner build(){
        try {
            filterAnnotation();
            //copyMethods();
            clonedClass.writeFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private CtClass copyMethods(){
        try {
            CtMethod[] methods = clonedClass.getDeclaredMethods();
            for (CtMethod method : methods) {
                // Clone the method and add it to the cloned class
                if (this.clonedClass.isFrozen()) {
                    this.clonedClass.defrost();
                }
                CtMethod clonedMethod = CtNewMethod.copy(method, clonedClass, null);
                clonedClass.addMethod(clonedMethod);
            }
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return clonedClass;
    }

    public ClassCloner setannotationToFilter(String annotation){
        annotationToFilter = annotation;
        return this;
    }

    public ClassCloner setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    private ClassCloner filterAnnotation(){
        try {
            if (this.clonedClass.isFrozen()) {
                this.clonedClass.defrost();
            }

            CtField[] fields = clonedClass.getDeclaredFields();
            for (CtField field : fields) {
                // Filter fields based on annotations
                if(!Modifier.isFinal(field.getModifiers())) {
                    AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) field.getFieldInfo().getAttribute(AnnotationsAttribute.visibleTag);
                    if (annotationsAttribute != null) {
                        Annotation annotation = annotationsAttribute.getAnnotation(annotationToFilter);
                        if (annotation != null) {
                            if ((Boolean) Espl.getInstance().addProperty("annotation", annotation).evaluate(condition)) {
                                // Clone the field and add it to the cloned class

                                CtField clonedField = new CtField(field.getType(), field.getName(), clonedClass);
                                clonedField.setModifiers(field.getModifiers());
                                clonedClass.removeField(field);
                                clonedClass.addField(clonedField);
                            } else {
                               // cloneField(field.getName());
                            }
                        } else {
                           // cloneField(field.getName());
                        }
                    } else {
                      //  cloneField(field.getName());
                    }
                }
            }
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    private void cloneField(String fieldName){
        try {
            CtField sourceField = clonedClass.getDeclaredField(fieldName);
            CtField clonedField = new CtField(sourceField.getType(), sourceField.getName(), clonedClass);
            if (this.clonedClass.isFrozen()) {
                this.clonedClass.defrost();
            }
            clonedField.setModifiers(sourceField.getModifiers());
            AnnotationsAttribute attr = (AnnotationsAttribute) sourceField.getFieldInfo().getAttribute(AnnotationsAttribute.visibleTag);
            if(attr != null) {
               // clonedField.getFieldInfo().addAttribute(attr);
            }
            clonedClass.addField(clonedField);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
