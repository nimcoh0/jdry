package org.softauto.service;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Protocol;
import org.apache.avro.ipc.Callback;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.core.Analyze;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

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
                Class[] types = getRealParametersType(method);
                if(tree.getParametersTypes().size() == types.length) {
                    if (tree.getParametersTypes().size() > 0) {
                        boolean found = true;
                        for (int i = 0; i < tree.getParametersTypes().size(); i++) {
                            if (!tree.getParametersTypes().get(i).equals(method.getParameterTypes()[i].getTypeName())) {
                                found = false;
                            }
                        }
                        if (found) {
                            return tree;
                        }
                    }else {
                        return tree;
                    }
                }
            }
        }
        for(GenericItem tree : analyze.getListeners()){
            if((tree.getNamespce()+"."+tree.getName()).replace(".","_").equals(method.getName())){
                return tree;
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

    public static Class[] getRealParametersType(Method method) {
        Type[] parameterTypes = method.getParameterTypes();
        if(parameterTypes.length > 0) {
            if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                    && org.apache.avro.ipc.CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
                Type[] finalTypes = Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
                return Arrays.stream(finalTypes).map(t -> (Class) t).collect(Collectors.toList()).toArray(new Class[finalTypes.length]);
            }
            return Arrays.stream(parameterTypes).map(t -> (Class) t).collect(Collectors.toList()).toArray(new Class[parameterTypes.length]);
        }
        return new Class[]{};
    }

}
