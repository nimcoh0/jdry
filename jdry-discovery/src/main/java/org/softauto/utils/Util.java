package org.softauto.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.config.Context;
import org.softauto.config.DefaultConfiguration;
import org.softauto.config.Configuration;
import org.softauto.flow.FlowObject;
import org.softauto.model.item.Item;
import org.yaml.snakeyaml.Yaml;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootClass;
import soot.jimple.parser.node.TClass;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;
import soot.util.dot.DotGraph;
import soot.util.queue.QueueReader;

import java.io.*;
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
        List<String> ignore = new ArrayList<>();
        return isExist(list,o1,ignore);
    }




    public static boolean isExist(ObjectNode nodeList, Item o1){
        List<String> ignore = null;
        List<Item> list = new ArrayList<>();
        try {
            ignore = new ArrayList<>();
            for(JsonNode node : nodeList){
                String nodeAsString = new ObjectMapper().writeValueAsString(node);
                Item item =  new ObjectMapper().readValue(nodeAsString, Item.class);
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist(list,o1,ignore);
    }

    public static <T> boolean isExist(List<T> list,T o1,List<String> ignore){
        if(list.size() > 0) {
            for (T o : list) {
                //if(!mergeObjects(o, o1)){
                if (equals(o, o1,ignore)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static  <T> boolean isExist(List<T> list,List<T> list1){
        List<String> ignore = new ArrayList<>();
        return isExist(list,list1,ignore);
    }

    public static  <T> boolean isExist(List<T> list,List<T> list1,List<String> ignore){
        if(list.size() > 0) {
            boolean isMatchs = list.stream().allMatch(el1 -> list1.stream().anyMatch(el2 -> equals(el1, el2,ignore)));
        }
        return false;
    }

    public static <T> boolean equals(Object source, Object target) {
        List<String> ignore = new ArrayList<>();
        return equals(source,target,ignore);
    }

    public static <T> boolean equals(Object source, Object target,List<String> ignore) {
        Field[] allFields = FieldUtils.getAllFields(source.getClass());
        try {
            if(FieldUtils.getAllFields(source.getClass()).length != FieldUtils.getAllFields(target.getClass()).length){
                return false;
            }
            for (Field field : allFields) {
                if (!ignore.contains(field.getName())) {
                    field.setAccessible(true);
                    if (field.get(source) == null && field.get(target) != null) {
                        return false;
                    }
                    if (field.get(source) != null && field.get(target) == null) {
                        return false;
                    }

                    if (field.get(source) != null && field.get(target) != null && !field.get(source).equals(field.get(target))) {
                        return false;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static <T> LinkedList<T> merge(LinkedList<T> list,LinkedList<T> list1){
        List<String> ignore = new ArrayList<>();
        return merge(list,list1,ignore);
    }

    public static <T> LinkedList<T> merge(LinkedList<T> list,LinkedList<T> list1,List<String> ignore){
        LinkedList<T> newList = new LinkedList<>();
        newList.addAll(list);
        for(T t : list1){
            if(!isExist(list,t,ignore)){
                newList.add(t);
            }
        }

        return (LinkedList<T>) newList;
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


    public static String buildEntityName(String name){
        String entityFullName =  addEntityPostfix(name);
        entityFullName = addEntityPrefix(entityFullName);
        return entityFullName;
    }

    public static String addEntityPostfix(String entityName){
        if(entityName != null) {
            String postfix = Configuration.has("entity_name_postfix") ? Configuration.get("entity_name_postfix").asString() : null;
            if (postfix != null && !entityName.endsWith(postfix)) {
                entityName = entityName+ postfix;
            }
        }
        return entityName;
    }

    public static String addEntityPrefix(String entityName){
        if(entityName != null) {
            String prefix = Configuration.has("entity_name_prefix") ? Configuration.get("entity_name_prefix").asString() : null;
            if (prefix != null && !entityName.startsWith(prefix)) {
                entityName =prefix + Util.capitalizeFirstLetter(entityName);
            }
        }
        return entityName;
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

    public static boolean isInclude(String name,List<String> list){
        for(String s : list){
            if(name.contains(s)){
                return true;
            }
        }
        return false;
    }
}
