package org.softauto.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.*;

public class ExtBeanSerializerModifier extends BeanSerializerModifier {

    //private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExtBeanSerializerModifier.class);
    private List<BeanPropertyWriter> beanProperties;
    private int maxDepth = 2048;
    private boolean depthLimitEnabled = true;
    private boolean loopPreventionEnabled = true;
    private boolean serializeBigObjects = false;
    private List<String> serializationExcludeClassList = new ArrayList<>();
    //private List<String> serializationSpecificIncludeClassList = new ArrayList<>();
    private List<HashMap<String,String>> serializationExcludeProperties = new ArrayList<>();


    public ExtBeanSerializerModifier setSerializationExcludePropertyList(List<String> serializationExcludePropertyList) {
        for(String p : serializationExcludePropertyList){
            String clazz = p.substring(0,p.lastIndexOf("."));
            String property = p.substring(p.lastIndexOf(".")+1,p.length());
            HashMap<String,String> hm = new HashMap<>();
            hm.put(clazz,property);
            serializationExcludeProperties.add(hm);
        }

        return this;
    }



    public ExtBeanSerializerModifier setSerializationExcludeClassList(List<String> serializationExcludeClassList) {
        this.serializationExcludeClassList = serializationExcludeClassList;
        return this;
    }

    public ExtBeanSerializerModifier setSerializeBigObjects(boolean serializeBigObjects) {
        this.serializeBigObjects = serializeBigObjects;
        return this;
    }

    public ExtBeanSerializerModifier setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    public ExtBeanSerializerModifier setDepthLimitEnabled(boolean depthLimitEnabled) {
        this.depthLimitEnabled = depthLimitEnabled;
        return this;
    }

    public ExtBeanSerializerModifier setLoopPreventionEnabled(boolean loopPreventionEnabled) {
        this.loopPreventionEnabled = loopPreventionEnabled;
        return this;
    }





    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
       try {

               if (serializer instanceof BeanSerializer && beanProperties != null) {
                   ExtBeanSerializer extBeanSerializer = new ExtBeanSerializer(beanDesc.getType(), new BeanSerializerBuilder(beanDesc), beanProperties.toArray(new BeanPropertyWriter[beanProperties.size()]), null);
                   extBeanSerializer.setDepthLimitEnabled(depthLimitEnabled);
                   extBeanSerializer.setLoopPreventionEnabled(loopPreventionEnabled);
                   extBeanSerializer.setMaxDepth(maxDepth);
                   extBeanSerializer.setSerializationExcludeClassList(serializationExcludeClassList);
                   extBeanSerializer.setSerializationExcludeProperties(serializationExcludeProperties);
                   //extBeanSerializer.setThreadlocalDepth(new ThreadLocal<>());
                   //extBeanSerializer.setThreadlocallifo(new ThreadLocal<>());
                   //extBeanSerializer.setRecs(new LinkedHashSet<>());
                   //extBeanSerializer.setSerializeBigObjects(serializeBigObjects);
                   //logger.debug("successfully initialize serializer for  "+beanDesc.getType());
                   return extBeanSerializer;
               }

        }catch (Exception e){
           e.printStackTrace();
           //logger.error("fail initialize serializer ",e);
        }

        return serializer;
    }

    public static void close(){
       ExtBeanSerializer.setDepth(0);
       ExtBeanSerializer.setHashCodeList(new LinkedList<>());
    }

    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties){
        this.beanProperties = beanProperties;
        return beanProperties;
    }


}
