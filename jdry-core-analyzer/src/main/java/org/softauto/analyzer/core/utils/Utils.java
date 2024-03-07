package org.softauto.analyzer.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.core.system.config.DefaultConfiguration;
import org.softauto.analyzer.model.argument.Argument;
import org.softauto.analyzer.model.request.Request;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.config.Configuration;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;



public class Utils {

    private static Logger logger = LogManager.getLogger(Utils.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public static void toJson(String filename, String path, List<GenericItem> trees)throws Exception{
        String json = new ObjectMapper().writeValueAsString(trees);
        save(json, path + "/" + filename + ".json");
    }

    public static void toJson(String filename,List<GenericItem> trees)throws Exception{
        String json = new ObjectMapper().writeValueAsString(trees);
        save(json, filename );
    }


    public static String getRequestAsArrayTypesString(List<Argument> request){
        if(request != null) {
            List<String> types = new ArrayList<>();
            for (int i = 0; i < request.size(); i++) {
                types.add(request.get(i).getType() + ".class");
            }

            String joinedString = StringUtils.join(types, ",");
            return "new Class[]{" + joinedString + "}";
        }
        return "new Class[]{}";
    }

    public static String getListStringAsArrayTypesString(List<String> request){
        if(request != null) {
            List<String> types = new ArrayList<>();
            for (int i = 0; i < request.size(); i++) {
                types.add(request.get(i) + ".class");
            }

            String joinedString = StringUtils.join(types, ",");
            return "new Class[]{" + joinedString + "}";
        }
        return "new Class[]{}";
    }


    public static String getRequestNamesAsArrayString(List<String> request){
        if(request != null) {
            List<String> names = new ArrayList<>();
            for (int i = 0; i < request.size(); i++) {
                String name = null;
                if(request.get(i) != null ){
                    name = request.get(i);
                }else {
                    name = "arg" + i;
                }
                names.add(name);
            }
            String joinedString = StringUtils.join(names, ",");
            return "new Object[]{" + joinedString + "}";
        }
        return "new Object[]{}";
    }


    public static String getRequestNamesAsArrayString(String args){
        if(args != null) {
            return "new Object[]{" + args + "}";
        }
        return "new Object[]{}";
    }



    public static void save(String json,String path)throws Exception{
        FileWriter file = null;
        File f = new File(path.substring(0,path.lastIndexOf("/")));
        try{
            f.mkdirs();
            file = new FileWriter(path);

            file.write(json);
        }catch (Exception e){
          logger.error("fail save file "+ path,e);
        }finally {
            file.close();
        }

    }

    public static void loadDefaultConfiguration()  {
        try {
            Configuration.setConfiguration(DefaultConfiguration.getConfiguration());
            logger.debug(JDRY,"Default Configuration load successfully");
        }catch(Exception e){
            logger.error(JDRY,"fail load Default Configuration ",e);
        }
    }

    public static void loadConfiguration(String path){
        try {
            Yaml yaml = new Yaml();
            if(path == null || path.isEmpty()){
                path = System.getProperty("user.dir") + "/analyzer.yaml";
            }
            if (new File(path).isFile()) {
                HashMap<String, Object> map = (HashMap<String, Object>) yaml.load(new FileReader(path));
                HashMap<String, Object> defaultConfiguration = Configuration.getConfiguration();
                defaultConfiguration.putAll(map);
                Configuration.setConfiguration(defaultConfiguration);
            }
            logger.debug(JDRY,"Configuration load successfully");
        }catch (Exception e){
            logger.error(JDRY,"fail loadConfiguration ",e);
        }
    }


    public static String javaEscape(String o) {
        return o.replace("\\", "\\\\").replace("\"", "\\\"");
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

    public static void addJarToClasspath(List<String> f) {
        try {
            for(String file : f) {
                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                addURL(file, classLoader);
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail add Jar To Classpath ",e);
        }
    }

    public static void addURL(String path,ClassLoader sysloader)  {
        try {
            Method method = sysloader.getClass()
                    .getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
            method.setAccessible(true);
            method.invoke(sysloader, path);
        } catch (Throwable t) {
            logger.error(JDRY,"fail add jar to classpath",t);
        }
    }



    public static Request buildRequest(GenericItem genericItem){
        LinkedList<Argument> arguments = new LinkedList<>();
        Request request = new Request();
        if(genericItem.getArgumentsNames() != null && genericItem.getArgumentsNames().size() > 0) {
            try {
                for (int i = 0; i < genericItem.getArgumentsNames().size(); i++) {
                    Argument argument = new Argument();
                    argument.setType(genericItem.getParametersTypes().get(i).replace("$", "."));
                    argument.setName(genericItem.getArgumentsNames().get(i));
                    arguments.add(argument);
                }

            } catch (Exception e) {
                logger.error(JDRY,"fail build request ", e);
            }
            request.setArguments(arguments);
        }
        return request;
    }


    public static String buildFullName(GenericItem genericItem){
        String fullName = genericItem.getNamespce() + "." + genericItem.getName();
        if(genericItem.getName().equals("<init>")){
            fullName = genericItem.getNamespce();
        }
        return fullName.replace(".","_");
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
            logger.error(JDRY,"fail build Short Name ",e);
        }
        return fullName;
    }


    public static boolean isPrimitive(String type){
        try {
            if(type != null) {
                Class c = ClassUtils.getClass(type);
                return ClassUtils.isPrimitiveOrWrapper(c);

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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












}

