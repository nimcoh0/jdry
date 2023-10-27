package org.softauto.analyzer.core.handlers;

import org.apache.commons.lang3.ClassUtils;
import org.softauto.analyzer.model.test.Test;
import org.softauto.espl.Espl;

public class AbstractHandler {

    protected void setEnv(String name,Object obj){
        Espl.getInstance().addProperty(name,obj);
    }

    protected void setEnv(Test test){
        try {
            Class c = ClassUtils.getClass(test.getNamespace());
            Espl.getInstance().addProperty("class",c);
            Espl.getInstance().addProperty("test",test);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
