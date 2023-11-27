package org.softauto.service;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Protocol;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.core.Analyze;

import java.lang.reflect.Method;
import java.util.HashMap;

public class JdryUtils {

    public static String getServiceName(Class iface) {
        Analyze analyze = getAnalyze(iface);
        return analyze.getNamespace() + "." + analyze.getName();
    }

    public static Analyze getAnalyze(Class iface) {
        try {
            Analyze p = (Analyze) (iface.getDeclaredField("analyze").get(null));
            return p;
        } catch (NoSuchFieldException e) {
            throw new AvroRuntimeException("Not a Specific Analyze: " + iface);
        } catch (IllegalAccessException e) {
            throw new AvroRuntimeException(e);
        }
    }

    public static GenericItem getStep(Method method,Analyze analyze){
        for(GenericItem tree : analyze.getSteps()){
            if((tree.getNamespce()+"."+tree.getName()).replace(".","_").equals(method.getName())){
                if(tree.getParametersTypes().size() == method.getParameterTypes().length) {
                    boolean found = true;
                    for (int i = 0; i < tree.getParametersTypes().size(); i++) {
                        if(!tree.getParametersTypes().get(i).equals(method.getParameterTypes()[i].getTypeName())){
                            found = false;
                        }
                    }
                    if(found){
                        return tree;
                    }
                }
            }
        }
        return null;
    }

    public static String getTransceiver(GenericItem tree){
        HashMap<String,Object> properties = tree.getProperties();
        if(properties.containsKey("protocol")){
            return properties.get("protocol").toString();
        }
        return null;
    }

    public static HashMap<String,Object> getCallOption(GenericItem tree){
        return tree.getProperties();
    }
}
