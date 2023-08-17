package org.softauto.core;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Utils {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Utils.class);

    /**
     * get local Machine Ip
     * @return
     */
    public static String getMachineIp(){
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get local Machine Name
     * @return
     */
    public static String getMachineName(){
        try {
            return Inet4Address.getLocalHost().getHostName();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Object o, String fullMethodName, Class[] types)throws Exception{
        try {
            Method m = null;
            if(o instanceof Class){
                m = ((Class)o).getDeclaredMethod(getMethodName(fullMethodName), types);
            }else {
                m = o.getClass().getDeclaredMethod(getMethodName(fullMethodName), types);
            }
            m.setAccessible(true);
            return m;
        }catch (Exception e){
            logger.error("fail get method "+ fullMethodName,e);
        }
        return  null;
    }

    public static Object[] getConstructorArgsValues(HashMap<String,Object> callOption,Class[] types){
        List<Object> objs = new ArrayList<>();
        try {
            List<String> args = (List<String>) callOption.get("constructor");
            for(int i=0;i<args.size();i++){
               String s = new ObjectMapper().writeValueAsString(args.get(i));
               Object o =  new ObjectMapper().readValue(s,types[i]);
               objs.add(o);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return objs.toArray();
    }

    public static Class[] extractConstructorArgsTypes(String fullClassName){
        try {
            Class c = Class.forName(fullClassName);
            Constructor[] constructors = c.getDeclaredConstructors();
            for(Constructor constructor: constructors){
                if(constructor.getParameters().length >0 ){
                    return constructor.getParameterTypes();
                }
            }
        }catch (Exception e){
            logger.error("fail extract Args Types for "+fullClassName,e);
        }
        return new Class[0];
    }



    public static String getFullClassName(String descriptor){
        return  descriptor.substring(0,descriptor.lastIndexOf("_")).replace("_",".");
    }


    public static String getMethodName(String descriptor){
        return descriptor.substring(descriptor.lastIndexOf("_")+1,descriptor.length());
    }


    public static String toString(Object obj){
        return ToStringBuilder.reflectionToString(obj, new MultipleRecursiveToStringStyle());
    }


    public static String result2String(Object result){
        try{

            if(result != null){
                if(result instanceof List){
                    return ToStringBuilder.reflectionToString(((List)result).toArray(), new MultipleRecursiveToStringStyle());
                }else {
                    return ToStringBuilder.reflectionToString(result, new MultipleRecursiveToStringStyle());
                }
            }
        }catch(Exception e){
            logger.warn("result to String fail on  ",e.getMessage());
        }
        return "";
    }




    public static Class findClass(String fullClassName){
        try{
            Class c =   Thread.currentThread().getContextClassLoader().loadClass(fullClassName);
            return c;
        }catch (Exception e){
            logger.error("find Class fail " + fullClassName,e);
        }
        return null;
    }

    public static Class getPrimitiveClass(String clazz){
       return builtInMap.get(clazz);
    }


    static Map<String,Class> builtInMap = new HashMap<String,Class>();
    static {
        builtInMap.put("int", Integer.TYPE );
        builtInMap.put("long", Long.TYPE );
        builtInMap.put("double", Double.TYPE );
        builtInMap.put("float", Float.TYPE );
        builtInMap.put("boolean", Boolean.TYPE );
        builtInMap.put("char", Character.TYPE );
        builtInMap.put("byte", Byte.TYPE );
        builtInMap.put("void", Void.TYPE );
        builtInMap.put("short", Short.TYPE );

    }

    public static boolean isJson(String str){
        try {
            if(str.startsWith("{") || str.startsWith("[{")) {
                new ObjectMapper().readTree(str);
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }





    public static boolean isPrimitive(String type){
        if(type != null) {
            String name = null;
            if (type.contains(".")) {
                name = type.substring(type.lastIndexOf(".") + 1);
            }else{
                name = type;
            }
            if (PRIMITIVES.contains(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    static final List<String> PRIMITIVES = new ArrayList<>();
    static {
        PRIMITIVES.add("string");
        PRIMITIVES.add("bytes");
        PRIMITIVES.add("int");
        PRIMITIVES.add("integer");
        PRIMITIVES.add("long");
        PRIMITIVES.add("float");
        PRIMITIVES.add("double");
        PRIMITIVES.add("boolean");
        PRIMITIVES.add("null");
        PRIMITIVES.add("void");
        PRIMITIVES.add("com.fasterxml.jackson.databind.node.IntNode");
        PRIMITIVES.add("com.fasterxml.jackson.databind.node.NullNode");
    }


    public static Object jsonNodeToJavaType(JsonNode jsonType) {
        if (jsonType instanceof JsonNode && jsonType != MissingNode.getInstance()) {
            if (jsonType instanceof IntNode) {
                return jsonType.asInt();
            }
            if (jsonType instanceof TextNode) {
                return jsonType.asText();
            }
            if (jsonType instanceof LongNode) {
                return jsonType.asLong();
            }
            if (jsonType instanceof BooleanNode) {
                return jsonType.asBoolean();
            }
            if (jsonType instanceof DoubleNode) {
                return jsonType.asDouble();
            }
         }
        return jsonType;
    }

    public static Object jsonNodeToJavaType(JsonNode jsonType,String javaType){
        try {
            if(javaType != null) {
                String name = null;
                if (javaType.contains(".")) {
                    name = javaType.substring(javaType.lastIndexOf(".") + 1);
                } else {
                    name = javaType;
                }
                if (name.equals("boolean")) return jsonType.asBoolean();
                if (name.equals("byte")) return jsonType.binaryValue();
                //if( javaType.equals("short") ) return jsonType.as;
                if (name.equals("integer")) return Integer.valueOf(jsonType.asText());
                if (name.equals("int")) return jsonType.asInt();
                if (name.equals("long")) return jsonType.asLong();
                if (name.equals("float")) return jsonType.floatValue();
                if (name.equals("double")) return jsonType.asDouble();
                if (name.equals("string")) return jsonType.asText();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonType;
    }





}
