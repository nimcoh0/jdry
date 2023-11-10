package org.softauto.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.config.Configuration;
import org.softauto.config.Context;
import org.softauto.config.DefaultConfiguration;
import org.softauto.discovery.handlers.flow.FlowObject;
import org.yaml.snakeyaml.Yaml;
import soot.MethodOrMethodContext;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.dot.DotGraph;
import soot.util.queue.QueueReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class Util {

    private static Logger logger = LogManager.getLogger(Util.class);


    public static void save(JsonNode items,String output){
        try {
            Util.toJson(output, items);
            logger.debug("save discovery items to "+output);
        } catch (Exception e) {
            logger.error("fail save discovery items ",e.getMessage());
        }
    }

    public static void save(JsonNode items){
        try {
            Util.toJson(Configuration.get(Context.FILE_NAME).asString(), Configuration.get(Context.OUTPUT_FILE_PATH).asString(), items);
            logger.debug("save discovery items to "+Configuration.get(Context.FILE_NAME).asString(), Configuration.get(Context.OUTPUT_FILE_PATH).asString());
        } catch (Exception e) {
            logger.error("fail save discovery items ",e.getMessage());
        }
    }

    /*
    public static boolean isExist(Item item, List<Object> items){
        try {
            for(Object i : items) {
                if (i instanceof Item && ((Item)i).getType().equals("method")) {
                    if (((Item)i).getNamespce().equals(item.getNamespce()) &&
                            ((Item)i).getName().equals(item.getName()) &&
                            ((Item)i).getParametersTypes().equals(item.getParametersTypes())) {
                        logger.debug("item found " + item.getNamespce() + "." + item.getName());
                        return true;
                    }
                }
            }
            logger.debug("item not found " +item.getNamespce()+ "."+item.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

     */

    /*
    public static boolean isExist(Item item, HashMap<String,Object> items){
        try {
            if(!items.containsKey(item.getNamespce()+"."+item.getName())){
                return false;
            }
            for(Map.Entry entry : items.entrySet()){
                if (entry instanceof Item && ((Item)entry).getType().equals("method")) {
                    if (((Item)entry).getNamespce().equals(item.getNamespce()) && ((Item)entry).getName().equals(item.getName())){
                        if(((Item)entry).getParametersTypes().equals(item.getParametersTypes())){
                            logger.debug("item found " + item.getNamespce() + "." + item.getName());
                            return true;
                        }else {

                        }
                    }
                }
            }
            logger.debug("item not found " +item.getNamespce()+ "."+item.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

     */

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
            logger.debug("obj save to json file "+ path + filename+".json successfully");
        } catch (Exception e) {
            logger.error("fail to save obj to json file ",e.getMessage());
        }
    }

    public static void toJson(String output, Object obj)throws Exception{
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
            String schema = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            save(schema, output);
            logger.debug("obj save to json file "+ output+".json successfully");
        } catch (Exception e) {
            logger.error("fail to save obj to json file ",e.getMessage());
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
            logger.debug("json save to file "+ path + " successfully");
        }catch (Exception e){
            logger.error("fail to save json to file " + path,e.getMessage());
        }finally {
            file.close();
        }

    }



    public static void loadDefaultConfiguration()  {
        try {
            Configuration.setConfiguration(DefaultConfiguration.getConfiguration());
        }catch(Exception e){
            logger.error("fail to load defaultConfiguration ",e.getMessage());
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
            logger.debug("configuration load successfully");
        }catch (Exception e){
           logger.error("fail to load configuration ",e.getMessage());
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
            e.printStackTrace();
        }
    }
    public static void addJarToClasspath(List<String> f) {
        try {
            for(String file : f) {
                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                addURL(file, classLoader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addURL(String path,ClassLoader sysloader)  {
        try {
            Method method = sysloader.getClass()
                    .getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
            method.setAccessible(true);
            method.invoke(sysloader, path);
        } catch (Throwable t) {
            logger.error("fail add jar to classpath",t);
        }
    }


}
