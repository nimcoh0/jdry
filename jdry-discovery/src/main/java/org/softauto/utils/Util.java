package org.softauto.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.config.Context;
import org.softauto.config.DefaultConfiguration;
import org.softauto.config.Configuration;
import org.softauto.flow.FlowObject;
import org.softauto.spel.SpEL;
import org.yaml.snakeyaml.Yaml;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootClass;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;
import soot.util.dot.DotGraph;
import soot.util.queue.QueueReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class Util {

    private static Logger logger = LogManager.getLogger(Util.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");


    public static void save(JsonNode items,String output){
        try {
            Util.toJson(output, items);
            logger.debug(JDRY,"save discovery items to "+output);
        } catch (Exception e) {
            logger.error(JDRY,"fail save discovery items ",e.getMessage());
        }
    }

    public static void save(JsonNode items){
        try {
            Util.toJson(Configuration.get(Context.FILE_NAME).asString(), Configuration.get(Context.OUTPUT_FILE_PATH).asString(), items);
            logger.debug(JDRY,"save discovery items to "+Configuration.get(Context.FILE_NAME).asString(), Configuration.get(Context.OUTPUT_FILE_PATH).asString());
        } catch (Exception e) {
            logger.error(JDRY,"fail save discovery items ",e.getMessage());
        }
    }



    /**
     * convert object to json and save to file
     * @param filename
     * @param path
     * @param obj
     * @throws Exception
     */
    public static void toJson(String filename, String path, Object obj)throws Exception{
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
            String schema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            save(schema, path + filename);
            logger.debug(JDRY,"obj save to json file "+ path + filename+".json successfully");
        } catch (Exception e) {
            logger.error(JDRY,"fail to save obj to json file ",e.getMessage());
        }
    }

    public static void toJson(String output, Object obj)throws Exception{
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
            String schema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            save(schema, output);
            logger.debug(JDRY,"obj save to json file "+ output+".json successfully");
        } catch (Exception e) {
            logger.error(JDRY,"fail to save obj to json file ",e.getMessage());
        }
    }
    /**
     * save json to file
     * @param json
     * @param path
     * @throws Exception
     */
    public static void save(String json,String path)throws Exception{
        FileWriter file = null;
        File f = new File(path.substring(0,path.lastIndexOf("/")));
        try{
            f.mkdirs();
            file = new FileWriter(path);

            file.write(json);
            logger.debug(JDRY,"json save to file "+ path + " successfully");
        }catch (Exception e){
            logger.error(JDRY,"fail to save json to file " + path,e.getMessage());
        }finally {
            file.close();
        }

    }



    public static void loadDefaultConfiguration()  {
        try {
            Configuration.setConfiguration(DefaultConfiguration.getConfiguration());
        }catch(Exception e){
            logger.error(JDRY,"fail to load defaultConfiguration ",e.getMessage());
        }
    }

    public static void loadConfiguration(String path){
        try {
            Yaml yaml = new Yaml();
            if(path == null || path.isEmpty()){
                path = System.getProperty("user.dir") + "/discovery.yaml";
            }
            if (new File(path).isFile()) {
                HashMap<String, Object> map = (HashMap<String, Object>) yaml.load(new FileReader(path));
                HashMap<String, Object> defaultConfiguration = Configuration.getConfiguration();
                defaultConfiguration.putAll(map);
                Configuration.setConfiguration(defaultConfiguration);
            }
            logger.debug(JDRY,"configuration load successfully");
        }catch (Exception e){
           logger.error(JDRY,"fail to load configuration ",e.getMessage());
        }
    }


    public static void toDot(List<FlowObject> trees){
        try{
            for(FlowObject tree : trees) {
                String filename = Configuration.get(Context.OUTPUT_FILE_PATH) + tree.getClazz()+"."+tree.getName() + ".dot";
                CallGraph cg = (CallGraph) tree.getCg();
                DotGraph canvas = new DotGraph("call-graph");
                QueueReader<Edge> listener = cg.listener();
                while (listener.hasNext()) {
                    Edge next = listener.next();
                    MethodOrMethodContext src = next.getSrc();
                    MethodOrMethodContext tgt = next.getTgt();
                    String srcString = src.toString();
                    String tgtString = tgt.toString();
                    if(srcString.contains(Configuration.get(Context.DOMAIN).asString()) && tgtString.contains(Configuration.get(Context.DOMAIN).asString())){
                        canvas.drawNode(src.toString());
                        canvas.drawNode(tgt.toString());
                        canvas.drawEdge(src.toString(), tgt.toString());
                        System.out.println("src = " + srcString);
                        System.out.println("tgt = " + tgtString);

                    }
                }
                canvas.plot(filename);
            }

        }catch (Exception e){
            logger.error(JDRY,"fail todot",e);
        }
    }
    public static void addJarToClasspath(List<String> f) {
        try {
            for(String file : f) {
                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                addURL(file, classLoader);
            }
        } catch (Exception e) {
            logger.error(JDRY,"fail add jar to classpath",e);
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

    public static List<SootClass> getSootClassListFromJar(String jarFilePath) {
        List<SootClass> files = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if(entry.getName().endsWith(".class")&& entry.getName().contains(Configuration.get(Context.DOMAIN).asString().replace(".","/"))) {
                    String name = entry.getName().replace(".class","");
                    if(!name.equals(Configuration.get(Context.MAIN_CLASS).asString().replace(".","/"))) {
                            SootClass ret = Scene.v().forceResolve(name.replace("/", "."), SootClass.BODIES | SootClass.HIERARCHY | SootClass.SIGNATURES);
                            if (!ret.isPhantom()) {
                                ret = Scene.v().loadClass(name.replace("/", "."), SootClass.BODIES);
                            }
                            files.add(ret);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }


    public static boolean isSingleton(SootClass sc){
        Object result = ApplyRule.setRule("singleton_definition").addContext("class",sc).apply().getResult();
        if(result != null){
            if(result instanceof Boolean){
                return (Boolean)result;
            }else {
                return false;
            }
        }
        if(sc.getMethodUnsafe(sc.getName() + " getInstance()") != null){
            return true;
        }
        return false;
    }

    public static boolean isGeneric(SootClass sc){
        Object result = ApplyRule.setRule("generic_definition").addContext("class",sc).apply().getResult();
        if(result != null){
            if(result instanceof Boolean){
                return (Boolean)result;
            }else {
                return false;
            }
        }

        List<Tag> tags = sc.getTags();
        for(Tag tag :tags ){
            if (tag instanceof SignatureTag) {
                SignatureTag t = (SignatureTag) tag;
                String string =  StringUtils.substringBetween(t.getSignature(),"<",">");
                if(string.startsWith("T")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isEntity(SootClass sc){
        try {
            Object result =  ApplyRule.setRule("entity_identify").addContext("type", sc).apply().getResult();
            if(result != null && result instanceof Boolean){
                return (boolean) result;
            }
            if(sc.getName().contains(Configuration.get("domain").asString())) {
                if(sc.getFieldUnsafe("java.lang.String id") != null
                        || sc.getFieldUnsafe("java.lang.Long id") != null
                        || sc.getFieldUnsafe("int id") != null
                        || sc.getFieldUnsafe(Util.getShortName("java.lang.String "+sc.getName()).toLowerCase()+"id") != null
                        || sc.getFieldUnsafe(Util.getShortName("java.lang.Long "+sc.getName()).toLowerCase()+"id") != null
                        || sc.getFieldUnsafe(Util.getShortName("int "+sc.getName()).toLowerCase()+"id") != null){
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getShortName(String fullName) {
        try {
            if (fullName.contains(".")) {
                return fullName.substring(fullName.lastIndexOf(".") + 1);
            }
            if (fullName.contains("_")) {
                String s = fullName.substring(fullName.lastIndexOf("_") + 1);
                if(s.matches("[a-zA-Z]+")){
                    return s;
                }else {
                    return fullName;
                }
            }
        } catch (Exception e) {
            logger.error("fail build Short Name ", e);
        }
        return fullName;
    }

    public static <T> boolean isExist(List<T> list,T o1){
        if(list.size() > 0) {
            for (T o : list) {
                //if(!mergeObjects(o, o1)){
                if (equals(o, o1)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static  <T> boolean isExist(List<T> list,List<T> list1){
        if(list.size() > 0) {
            boolean isMatchs = list.stream().allMatch(el1 -> list1.stream().anyMatch(el2 -> equals(el1, el2)));
        }
        return false;
    }

    public static <T> boolean equals(Object source, Object target) {
        Field[] allFields = FieldUtils.getAllFields(source.getClass());
        try {
            if(FieldUtils.getAllFields(source.getClass()).length != FieldUtils.getAllFields(target.getClass()).length){
                return false;
            }
            for (Field field : allFields) {
                field.setAccessible(true);
                if (field.get(source) == null && field.get(target)!= null){
                    return false;
                }
                if (field.get(source) != null && field.get(target)== null){
                    return false;
                }

                if (field.get(source) != null && field.get(target)!= null && !field.get(source).equals(field.get(target))) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static <T> T merge(List<T> list,List<T> list1){
        List<T> newList = new ArrayList<>();
        for(T t : list1){
            if(!isExist(list,t)){
                newList.add(t);
            }
        }
        newList.addAll(list);
        return (T) newList;
    }


/*
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

 */
}
