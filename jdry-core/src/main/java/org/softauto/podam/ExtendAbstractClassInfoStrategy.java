package org.softauto.podam;

import org.softauto.annotations.DataForTesting;
import org.softauto.annotations.JdryExclude;
import org.softauto.espl.Espl;
import uk.co.jemos.podam.api.AbstractClassInfoStrategy;
import uk.co.jemos.podam.api.ClassAttribute;
import uk.co.jemos.podam.api.ClassAttributeApprover;
import uk.co.jemos.podam.api.ClassInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class ExtendAbstractClassInfoStrategy extends AbstractClassInfoStrategy {

    Espl espl = Espl.getInstance();

    List<String> _excludedFields = new ArrayList<>();

    public ClassInfo getClassInfo(Class<?> clazz,
                                  Set<Class<? extends Annotation>> excludeFieldAnnotations,
                                  Set<String> excludedFields,
                                  ClassAttributeApprover attributeApprover,
                                  Collection<Method> extraMethods) {

        ClassInfo classInfo = super.getClassInfo(clazz,excludeFieldAnnotations,excludedFields,attributeApprover,extraMethods);
        Set<ClassAttribute> attributes = classInfo.getClassAttributes();
        Set<ClassAttribute> newAttributes = new HashSet<>();
        for(ClassAttribute classAttribute : attributes){
            Annotation[] annotations = classAttribute.getAttribute().getDeclaredAnnotations();
            if(annotations != null && annotations.length > 0) {
                for (Annotation annotation : annotations) {
                    if(annotation instanceof JdryExclude){
                        if(((JdryExclude) annotation).value() != null && !((JdryExclude) annotation).value().isEmpty()){
                            _excludedFields.add(((JdryExclude) annotation).value());
                        }else {
                            _excludedFields.add(classAttribute.getName());
                        }
                    }else {
                        if (!_excludedFields.contains(classAttribute.getAttribute().getName())) {
                            newAttributes.add(classAttribute);
                        }
                    }
                }
            }else {
                if (!this._excludedFields.contains(classAttribute.getAttribute().getName())) {
                    newAttributes.add(classAttribute);
                }
            }
        }
        Set<ClassAttribute> newAttributes1 = new HashSet<>();
        newAttributes1.addAll(newAttributes);
        for(String s : _excludedFields){
            for(ClassAttribute classAttribute :newAttributes ){
                if(classAttribute.getName().equals(s)){
                    newAttributes1.remove(classAttribute);
                }
            }
        }
        ClassInfo classInfo1 = new ClassInfo(classInfo.getClassName(),newAttributes1);
        return classInfo1;
    }


}
