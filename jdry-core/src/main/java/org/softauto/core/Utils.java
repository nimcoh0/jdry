package org.softauto.core;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Utils {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Utils.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    /**
     * get local Machine Ip
     * @return
     */
    public static String getMachineIp(){
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        }catch (Exception e){
           logger.error(JDRY,"fail get Machine Ip",e);
        }
        return null;
    }

    public static String javaEscape(String o) {
        return o.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    /**
     * get local Machine Name
     * @return
     */
    public static String getMachineName(){
        try {
            return Inet4Address.getLocalHost().getHostName();
        }catch (Exception e){
            logger.error(JDRY,"fail get Machine name",e);
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
            logger.error(JDRY,"fail get method "+ fullMethodName,e);
        }
        return  null;
    }

    public static Object[] getConstructorArgsValues(HashMap<String,Object> callOption,Class[] types){
        List<Object> objs = new ArrayList<>();
        try {
            List<HashMap<String,String>> args = (List<HashMap<String,String>>) callOption.get("constructor");
            if(args != null && args.size() > 0) {
                for(int i=0;i<args.size();i++) {
                   for (Map.Entry entry : args.get(i).entrySet()) {
                        try {
                            String value = entry.getValue().toString();
                            String s = new ObjectMapper().writeValueAsString(value);
                            Object o = new ObjectMapper().readValue(s, types[i]);
                            objs.add(o);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail Constructor Args Values",e);
        }
        return objs.toArray();
    }



    public static Class[] extractConstructorArgsTypes(String fullClassName , List<String> constructorTypes){
        try {
            Class c = Class.forName(fullClassName);
            Constructor[] constructors = c.getDeclaredConstructors();
            for(Constructor constructor: constructors){
                boolean found = true;
                if(constructorTypes != null &&constructor.getParameters().length >0 && constructor.getParameters().length == constructorTypes.size()){
                    for(int i=0;i<constructor.getParameters().length;i++){
                        if(!constructor.getParameters()[i].getType().getTypeName().contains(constructorTypes.get(i))){
                            found = false;
                        }
                    }
                    if(found)
                        return constructor.getParameterTypes();
                }
            }
        }catch (Exception e){
            logger.error(JDRY,"fail extract Args Types for "+fullClassName,e);
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
            logger.warn(JDRY,"result to String fail on  ",e.getMessage());
        }
        return "";
    }




    public static Class findClass(String fullClassName){
        try{
            Class c =   Thread.currentThread().getContextClassLoader().loadClass(fullClassName);
            return c;
        }catch (Exception e){
            logger.error(JDRY,"find Class fail " + fullClassName,e);
        }
        return null;
    }

    public static Class getPrimitiveClass(String clazz){
        if(clazz != null) {
            String name = null;
            if (clazz.contains(".")) {
                name = clazz.substring(clazz.lastIndexOf(".") + 1);
            }else{
                name = clazz;
            }
            return builtInMap.get(name);
        }

       return null;
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
            if (PRIMITIVES.contains(name)) {
                return true;
            }
        }
        return false;
    }

    static final List<String> PRIMITIVES = new ArrayList<>();
    static {
        PRIMITIVES.add("bytes");
        PRIMITIVES.add("int");
        PRIMITIVES.add("long");
        PRIMITIVES.add("float");
        PRIMITIVES.add("double");
        PRIMITIVES.add("boolean");

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
                if (name.equals("integer")) return Integer.valueOf(jsonType.asText());
                if (name.equals("int")) return jsonType.asInt();
                if (name.equals("long")) return jsonType.asLong();
                if (name.equals("float")) return jsonType.floatValue();
                if (name.equals("double")) return jsonType.asDouble();
                if (name.equals("string")) return jsonType.asText();
            }
        } catch (IOException e) {
            logger.error(JDRY,"find json Node To Java Type" ,e);
        }
        return jsonType;
    }


    public static String toObject( String clazz, String value ) {

        String str = clazz.toLowerCase();
        if(clazz.contains(".")){
            str = clazz.substring(clazz.lastIndexOf(".")+1).toLowerCase();
        }
        if(value.endsWith(";")){
            value = value.substring(0,value.length()-1);
        }
        if(NumberUtils.isCreatable(value)){
            return value;
        }
        if(value.toLowerCase().equals("false") || value.toLowerCase().equals("true")){
            return value;
        }


        if( str.equals("boolean") ) return "Boolean.parseBoolean("+ value +");";
        if( str.equals("byte") ) return "Byte.parseByte("+ value +");";
        if( str.equals("short") ) return "Short.parseShort("+ value +");";
        if( str.equals("integer") ) return "Integer.parseInt("+ value + ");";
        if( str.equals("long" ) ) return "Long.parseLong("+ value +");";
        if( str.equals("float") ) return "Float.parseFloat(" +  value +");";
        if( str.equals("double") ) return "Double.parseDouble("+ value + ");";
        if( str.equals("string") ) return "String.valueOf("+ value + ");";
        return value+";";
    }

    public static String capitalizeFirstLetter(String str){
        if(str != null && !str.isEmpty()) {
            return str.toUpperCase().charAt(0) + str.substring(1, str.length());
        }
        return str;
    }

    public static String unCapitalizeFirstLetter(String str){
        if(str != null && !str.isEmpty()) {
            return str.toLowerCase().charAt(0) + str.substring(1, str.length());
        }
        return str;
    }

    public static String getShortName(String fullName){
        try {
            if(fullName.contains(".")){
                return fullName.substring(fullName.lastIndexOf(".")+1);
            }
            if(fullName.contains("_")){
                return fullName.substring(fullName.lastIndexOf("_")+1);
            }
        } catch (Exception e) {
            logger.error("fail build Short Name ",e);
        }
        return fullName;
    }


    public static String propertiesToJson(Object string) {
        try {
            if (string.toString().startsWith("{")) {
                string = StringUtils.substringBetween(string.toString(), "{", "}");
            }
            ObjectNode node = new ObjectMapper().createObjectNode();
            String[] propertis = string.toString().split(",");
            for(String prop : propertis){
                String[] props = prop.split("=");
                node.put(props[0].trim(),props[1].trim());
            }
            return (new ObjectMapper()).writeValueAsString(node);
        } catch (JsonProcessingException var6) {
            var6.printStackTrace();
        }

        return null;
    }


    public static Object mergeObjects(Object source, Object target) throws Exception {
        Field[] allFields = source.getClass().getDeclaredFields();
        for (Field field : allFields) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            if (!field.isAccessible() && Modifier.isPrivate(field.getModifiers()))
                field.setAccessible(true);
            if (!field.isAccessible() && Modifier.isProtected(field.getModifiers()))
                field.setAccessible(true);
            if (field.get(source) != null) {
                field.set(target, field.get(source));
            }
        }

        return target;
    }
}
