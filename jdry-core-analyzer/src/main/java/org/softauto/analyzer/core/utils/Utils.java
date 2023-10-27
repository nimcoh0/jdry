package org.softauto.analyzer.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.directivs.callback.CallBack;
import org.softauto.analyzer.directivs.callback.CallBackBuilder;
import org.softauto.analyzer.core.rules.EntityRules;
import org.softauto.analyzer.core.system.config.Context;
import org.softauto.analyzer.core.system.config.DefaultConfiguration;
import org.softauto.analyzer.directivs.argument.Argument;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.core.system.espl.Espl;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.suite.Suite;
import org.softauto.analyzer.model.test.Test;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.system.scanner.AnnotationHelper;
import org.softauto.analyzer.core.system.config.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;


public class Utils {

    private static Logger logger = LogManager.getLogger(Utils.class);

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

    public static void toJson(String filename, String path, Suite suite)throws Exception{
       save(suite.toJson(), path + "/" + filename + ".json");
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
            logger.debug("Default Configuration load successfully");
        }catch(Exception e){
            logger.error("fail load Default Configuration ",e);
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
            logger.debug("Configuration load successfully");
        }catch (Exception e){
            logger.error("fail loadConfiguration ",e);
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
            logger.error("fail add Jar To Classpath ",e);
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
                logger.error("fail build request ", e);
            }
            request.setArguments(arguments);
        }
        return request;
    }

    /*
    public static Result buildResult(GenericItem genericItem){
        return buildResult(genericItem,null);
    }

    public static Result buildResult(GenericItem genericItem,String type){
        Result result = new Result();
        try {
            if(type != null){
                result.setType(type);
            }else {
                result.setType(genericItem.getReturnType());
            }
            result.setName(Utils.buildResultString(genericItem,null));
        } catch (Exception e) {
           logger.error("fail build result ",e);
        }
        return result;
    }

    public static  Result buildResult(GenericItem genericItem, String resultName, Data data){
        Result result = new Result();
        try {
            result.setName(resultName);
            result.setType(data != null ? data.getResponse().getType(): genericItem.getReturnType());
            //Expression expression = ExpressionBuilder.newBuilder().setValue(data != null ? data.getResponse().getValue().getExpression() : null).setDisplayType(DisplayType.STRING).build().getExpression();
            //Expression expression = ExpressionBuilder.newBuilder().setValue(data != null ? data.getResponse().getValue().getExpression() : null).build().getExpression();
            result.setValue(data != null ? data.getResponse().getValue() : null);
        } catch (Exception e) {
            logger.error("fail build result ",e);
        }

        return result;
    }

    public static String buildResultString(GenericItem genericItem, Map map){
        String result = null;
        try {
            if(genericItem.getName().equals("<init>")){
                result = "result_" + (genericItem.getNamespce()).replace(".", "_");
            }else {
                if (genericItem.getReturnType() != null && !genericItem.getReturnType().equals("Void") && !genericItem.getReturnType().equals("void")) {
                    result = "result_" + (genericItem.getNamespce() + "." + genericItem.getName()).replace(".", "_");
                }
            }
            if(map != null && map.containsKey("callback") && map.get("callback") != null){
                result = map.get("callback").toString();
            }
        } catch (Exception e) {
            logger.error("fail build result name",e);
        }

        return result;
    }


     */

    public static String buildTestFullName(GenericItem genericItem){
        String fullName = genericItem.getNamespce() + "." + buildTestName(genericItem);
        if(genericItem.getName().equals("<init>")){
            fullName = genericItem.getNamespce();
        }
        return fullName.replace(".","_");
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
            logger.error("fail build Short Name ",e);
        }
        return fullName;
    }

    public static Object getResultAsObject( Result result ){
        try {
            if(result.getValue() != null && !Utils.isPrimitive(result.getType())&& (result.getType().contains(Configuration.get(Context.DOMAIN).asString()))) {
                Class c = Class.forName(result.getType(), false, ClassLoader.getSystemClassLoader());
                if(c != null) {
                    if (Utils.isJson(result.getValue().toString())) {
                        return new ObjectMapper().readValue(result.getValue().toString(), c);
                    } else {
                        String s = new ObjectMapper().writeValueAsString(result.getValue());
                        return new ObjectMapper().readValue(s, c);
                    }
                }
            }else {
                return result.getValue();
            }
        } catch (Exception e) {
            logger.error("fail build result as Object ",e);
        }
        return null;
    }

    public static List<Object>  buildChildes(List<GenericItem> childes,List<Object> itemList){
        try {
            for(GenericItem genericItem : childes){
                if(genericItem != null && genericItem.getChildes().size() > 0){
                    itemList =  buildChildes(genericItem.getChildes(),itemList);
                }
                List<String> api_annotations = Configuration.get("api_annotations").asList();
                if(genericItem.getAnnotations() != null && genericItem.getAnnotations().size()>0){
                    if(AnnotationHelper.isExist("Lorg/softauto/annotations/VerifyForTesting;",genericItem.getAnnotations()) ||
                            AnnotationHelper.isExist("Lorg/softauto/annotations/ListenerForTesting;",genericItem.getAnnotations())){
                        itemList.add(genericItem);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("fail build childes ",e);
        }
        return itemList;
    }
    public static String buildTestName(GenericItem genericItem){
        String name = genericItem.getName();

        try {
            if(genericItem.getName().equals("<init>")){
                name =  Utils.unCapitalizeFirstLetter(Utils.getShortName(genericItem.getNamespce()));
            }
            if(Configuration.has("test_name")){
                String schema =  Configuration.get("test_name").asString();
                name = Espl.getInstance().addProperty("method",genericItem).evaluate(schema).toString();
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String buildName(GenericItem genericItem){
        String name = genericItem.getName();

        try {
            if(genericItem.getName().equals("<init>")){
                name =  Utils.unCapitalizeFirstLetter(Utils.getShortName(genericItem.getNamespce()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /*
    public static String getProtocol1(AbstractAnnotationScanner scanner, GenericItem genericItem){
        try {
            CheckNotNullOrEmpty.set(scanner).setType("org.softauto.analyzer.scanner.AbstractAnnotationScanner").isNotNull().isTrue(scanner.has("protocol"))
                     .onSuccess(p ->{
                         return ((AbstractAnnotationScanner)p).get("protocol").asString();
                     }).onFail(e ->{
                         List<HashMap<String,Object>> protocolIdentifierList = Configuration.get("protocol_identifier").asList();
                         for(HashMap<String,Object> hm : protocolIdentifierList) {
                             for (Map.Entry entry : hm.entrySet()) {
                                 for(Map.Entry s : genericItem.getAnnotations().entrySet()){
                                     if(s.getKey().toString().contains(((Map)entry.getValue()).get("identifier").toString().replace(".","/"))){
                                         return ((Map)entry.getValue()).get("name").toString();
                                     }
                                 }
                             }
                         }
                         return Configuration.get("default_protocol").asString();
                     });
        } catch (Exception e) {
            logger.error("fail build protocol ",e);
        }
        return null;
    }


     */

    public static String getProtocol(AbstractAnnotationScanner scanner, GenericItem genericItem,HashMap<String,Object> callOption){
        try {
            if(scanner != null && scanner.has("protocol")){
                return scanner.get("protocol").asString();
            }
            if(callOption.containsKey("protocol")){
                return callOption.get("protocol").toString();
            }
            List<HashMap<String,Object>> protocolIdentifierList = Configuration.get("protocol_identifier").asList();
            if(protocolIdentifierList != null) {
                for (HashMap<String, Object> hm : protocolIdentifierList) {
                    for (Map.Entry entry : hm.entrySet()) {
                        for (Map.Entry s : genericItem.getAnnotations().entrySet()) {
                            if (s.getKey().toString().contains(((Map) entry.getValue()).get("identifier").toString().replace(".", "/"))) {
                                return ((Map) entry.getValue()).get("name").toString();
                            }
                        }
                    }
                }
            }
            return Configuration.get("default_protocol").asString();
        } catch (Exception e) {
            logger.error("fail build protocol ",e);
        }
        return null;
    }

    public static Result getReturnType(Result r,AbstractAnnotationScanner scanner,GenericItem genericItem){
        try {
            if(r == null){
                r = new Result();
                r.setType(genericItem.getReturnType());
            }

            if(scanner != null) {
                if (scanner.has("expected")) {
                    r.addValue(scanner.get("expected").asString());
                }
                if (scanner.has("returnType")) {
                    r.setType(scanner.get("returnType").asString());

                    return r;
                }
            }
        } catch (Exception e) {
            logger.error("fail build return type ",e);
        }

        return null;
    }


    public static HashMap<String, Object> PublishArguments1(org.softauto.analyzer.model.data.Data data, List<Argument> args){
        HashMap<String, Object> publish = new HashMap<>();
        CheckNotNullOrEmpty.set(data).isNotNull().onSuccess(res ->{
            for(Argument argument : data.getRequest().getArguments()) {
                if (argument.getValue() != null){
                    publish.put(argument.getName(), argument.getName());
                }
            }
            return publish;
        }).onFail(res ->{
            CheckNotNullOrEmpty.set(args).isNotNull().isNotEmpty().onSuccess(res1 ->{
                for(Argument argument : args) {
                    if (Utils.isPrimitive(argument.getType())) {
                        publish.put(Utils.unCapitalizeFirstLetter(Utils.getShortName(argument.getName())), argument.getName());
                    } else {
                        publish.put(Utils.unCapitalizeFirstLetter(Utils.getShortName(argument.getType())), argument.getName());
                    }
                }
                return publish;
            });
            return publish;
        });

        return publish;
    }


    public static HashMap<String, Object> PublishArguments(org.softauto.analyzer.model.data.Data data, List<Argument> args){
        HashMap<String, Object> publish = new HashMap<>();
        try {
            if(data != null) {
                for(Argument argument : data.getRequest().getArguments()) {
                    if (argument.getValue() != null){
                       publish.put(argument.getName(), argument.getName());
                    }
                }
            }else {
                for(Argument argument : args) {
                    if (Utils.isPrimitive(argument.getType())) {
                        publish.put(Utils.unCapitalizeFirstLetter(Utils.getShortName(argument.getName())), argument.getName());
                    } else {
                        publish.put(Utils.unCapitalizeFirstLetter(Utils.getShortName(argument.getType())), argument.getName());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("fail Publish Arguments ",e);
        }
        return publish;
    }

    public static HashMap<String ,String> getArgsTypeAsString(List<String> names ,List<String> types){
        HashMap<String ,String> _types = new HashMap<>();
        try {
            for(int i=0;i<names.size();i++){
                _types.put(names.get(i),types.get(i));
            }
        } catch (Exception e) {
           logger.error("fail get Args Type As String ",e);
        }
        return _types;
    }

    public static HashMap<String ,String> getArgsTypeAsString(List<Argument> arguments ){
        HashMap<String ,String> types = new HashMap<>();
        try {
            for(int i=0;i<arguments.size();i++){
                types.put(arguments.get(i).getName(),arguments.get(i).getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return types;
     }

    public static HashMap<String ,Object> getArgsAsObject(List<Argument> arguments ){
        HashMap<String ,Object> args = new HashMap<>();
        try {
            for(int i=0;i<arguments.size();i++){
                Object result = null;
                Object value = arguments.get(i).getValue() != null ? arguments.get(i).getValue() : null;
                if(!Utils.isPrimitive(arguments.get(i).getType())&& (arguments.get(i).getType().contains(Configuration.get(Context.DOMAIN).asString()))) {
                    if(value != null) {
                        Class c = Class.forName(arguments.get(i).getType(), false, ClassLoader.getSystemClassLoader());
                        if (c != null) {
                            if (Utils.isJson(arguments.get(i).getValue().toString())) {
                                result = new ObjectMapper().readValue(arguments.get(i).getValue().toString(), c);
                            } else {
                                result = arguments.get(i).getValue().toString();
                            }
                        }
                    }else {

                    }
                }else {
                    result = value;
                }


                args.put(arguments.get(i).getName(),result);

            }
        } catch (Exception e) {
            logger.error("fail get Args As Object ",e);
        }
        return args;
    }


    public static Object mergeObjects(Object source, Object target) throws Exception {
        Field[] allFields = source.getClass().getDeclaredFields();
        for (Field field : allFields) {
            if(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())){
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

    private static boolean isModify(Object obj){
        try {
            if(obj.getClass().getDeclaredField("modify") != null){
                Object value = FieldUtils.readField(obj, "modify", true);
                return (Boolean) value;
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }


    public static Object merge(Object local, Object remote, Class type,String domain)
            throws IllegalAccessException, InstantiationException {
        Class<?> clazz = local.getClass();
        Class<?> rclazz = remote.getClass();
        Object merged = clazz.newInstance();
        try {
            if(isModify(local)) {
                if (!Utils.isPrimitive(clazz.getTypeName())) {
                    for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
                        Field localField = clazz.getDeclaredFields()[i];
                        Field remoteField = rclazz.getDeclaredFields()[i];
                        localField.setAccessible(true);
                        remoteField.setAccessible(true);
                        Object localValue = localField.get(local);
                        Object remoteValue = remoteField.get(remote);
                        if (localValue != null && remoteValue != null) {
                            if (Utils.isPrimitive(remoteValue.getClass().getTypeName())) {
                                localField.set(merged, remoteValue);
                            } else if (remoteValue instanceof Collections && localValue.toString().contains(domain)) {
                                for (int j = 0; j < ((Collection) remoteValue).size(); j++) {
                                    localField.set(j, merge(((Collection) localValue).toArray()[j], ((Collection) remoteValue).toArray()[j], type, domain));
                                }
                            } else if (remoteValue instanceof AbstractSequentialList<?> && localValue.toString().contains(domain)) {
                                for (int j = 0; j < ((AbstractSequentialList) remoteValue).size(); j++) {
                                    ((AbstractSequentialList) remoteValue).set(j, merge(((AbstractSequentialList) localValue).get(j), ((AbstractSequentialList) remoteValue).get(j), type, domain));
                                    localField.set(merged, remoteValue);
                                }
                            } else if (remoteValue instanceof Map) {
                                ((Map<String, Object>) remoteValue).forEach((k, v) -> {
                                    if (!((Map<String, Object>) localValue).containsKey(k)) {
                                        ((Map<String, Object>) localValue).put(k, v);
                                    } else if (v != null) {
                                        try {
                                            Object value = ((Map<String, Object>) localValue).get(k);
                                            Object mergeValue = merge(value, v, type, domain);
                                            ((Map<String, Object>) localValue).put(k, mergeValue);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                localField.set(merged, localValue);
                            } else if (remoteValue.getClass().getDeclaredFields().length > 0 && localValue.toString().contains(domain)) {
                                localField.set(merged, merge(localValue, remoteValue, type, domain));
                            } else {
                                 localField.set(merged, remoteValue);
                            }
                        } else if (remoteValue != null) {
                            localField.set(merged, remoteValue);
                        } else if (localValue != null) {
                            localField.set(merged, localValue);
                        }
                    }
                } else {
                    return remote;
                }
            }else {
                return local;
            }
        } catch (Exception e) {
            logger.error("fail merge objects ",e);
        }
        return merged;
    }


    public static String getDefaultAsertByType( String type, String value ) {
        try {
            if(type != null&& value != null) {
                String str = type.toLowerCase();
                if (type.contains(".")) {
                    str = type.substring(type.lastIndexOf(".") + 1).toLowerCase();
                }
                if (value.endsWith(";")) {
                    value = value.substring(0, value.length() - 1);
                }
                if (NumberUtils.isCreatable(value)) {
                    return value;
                }
                if (value.toLowerCase().equals("false") || value.toLowerCase().equals("true")) {
                    return value;
                }


                if (str.equals("boolean"))
                    return "org.junit.Assert.assertTrue("+value+")";
                if (str.equals("byte"))
                    return "org.junit.Assert.assertTrue("+value+" != null)";
                if (str.equals("short"))
                    return "org.junit.Assert.assertTrue("+value+" != -1)";
                if (str.equals("integer"))
                    return "org.junit.Assert.assertTrue("+value+" != -1)";
                if (str.equals("int"))
                    return "org.junit.Assert.assertTrue("+value +" != -1)";
                if (str.equals("long"))
                    return "org.junit.Assert.assertTrue("+value +" != -1L)";
                if (str.equals("float"))
                    return "org.junit.Assert.assertTrue("+value+"  > -1.0)";
                if (str.equals("double"))
                    return "org.junit.Assert.assertTrue("+value +" > -1.0)";
                if (str.equals("string")) return "org.junit.Assert.assertTrue("+value+" != null)";
                return "org.junit.Assert.assertTrue("+value+" != null)";
            }
        } catch (Exception e) {
            logger.error("fail get Default Assert By Type ",e);
        }
        return null;
    }

    public static String propertiesToString(Properties p){
        return  p.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));

    }

    public static Map<?,?> propertiesToMap(Properties p){
        return Maps.newHashMap(Maps.fromProperties(p));
    }

    public static Properties stringToProperties(String s){
        try {
            final Properties p = new Properties();
            p.load(new StringReader(s));
            return p;
        } catch (IOException e) {
            logger.error("fail string To Properties ",e);
        }
        return null;
    }

    public static boolean isPrimitive(String type){
        try {
            if(type != null) {
                Class c = ClassUtils.getClass(type);
                return ClassUtils.isPrimitiveOrWrapper(c);
                /*
                String name = null;
                if (type.contains(".")) {
                    name = type.substring(type.lastIndexOf(".") + 1);
                }else{
                    name = type;
                }
                if (PRIMITIVES.contains(name)) {
                   return true;
                }

                 */
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    static final List<String> PRIMITIVES = new ArrayList<>();
    static {
        //PRIMITIVES.add("string");
        PRIMITIVES.add("bytes");
        PRIMITIVES.add("int");
        //PRIMITIVES.add("integer");
        PRIMITIVES.add("long");
        PRIMITIVES.add("float");
        PRIMITIVES.add("double");
        PRIMITIVES.add("boolean");
        //PRIMITIVES.add("null");
        //PRIMITIVES.add("void");
        //PRIMITIVES.add("com.fasterxml.jackson.databind.node.IntNode");
        //PRIMITIVES.add("com.fasterxml.jackson.databind.node.NullNode");
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

    public static Class typeToClass(String type){
        try {
            if(isPrimitive(type)){
               return getPrimitiveClass(type);
            }
            return Class.forName(type,false,ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            logger.warn("fail retrieve class type "+type);
        }
        return null;
    }

    public static HashMap<String,Object> buildPublish(HashMap<String,Object> publish){
        HashMap<String, Object> hm = new HashMap<>();
        if(publish != null) {
            if(publish.get("value") instanceof String) {
                hm.put(publish.get("name").toString(), publish.get("value"));
            }
        }
        return hm;
    }

    public static HashMap<String,Object> buildPublish(Test test, HashMap<String,Object> publish){
        HashMap<String, Object> hm = new HashMap<>();
        if(publish != null) {
            if(test.getApi().hasAfter()){
                if(publish.get("value") instanceof String) {
                    hm.put(publish.get("name").toString(), test.getApi().getAfterList().get(0).getExpression());
                }
            }else {
                if (publish.get("value") instanceof String) {
                    hm.put(publish.get("name").toString(), publish.get("value"));
                }
            }
        }
        return hm;
    }

    public static HashMap<String,Object> buildPublish(List<HashMap<String,Object>> publish){
        HashMap<String,Object> hm = new HashMap<>();
        for(HashMap<String,Object> hm1 : publish){
            hm.putAll(buildPublish(hm1));
        }
        return hm;
    }

    public static String expressionParser(String expression) {
        if (expression.toString().contains("@{")) {
            String[] subStrings = StringUtils.substringsBetween(expression.toString(), "@{", "}");
            for (int j = 0; j < subStrings.length; j++) {
                expression = expression.toString().replace("@{" + subStrings[j] + "}", "@{}" + j);
            }
            expression = Utils.javaEscape(expression.toString());
            int leftSideIndex = 0;
            for (int k = 0; k < subStrings.length; k++) {
                String leftSide = expression.toString().substring(leftSideIndex, expression.toString().indexOf("@{}" + k));
                boolean hasLeftSide = leftSide.length() > 0;
                if (hasLeftSide) {
                    expression = expression.replace(leftSide,"\"" + leftSide + "\"");
                    subStrings[k] = "+" + subStrings[k];
                }

                if(expression.length() > leftSideIndex){
                    subStrings[k] = subStrings[k] + "+";
                }
                expression = expression.toString().replace("@{}" + k, subStrings[k]);
                leftSideIndex = expression.lastIndexOf(subStrings[k])+subStrings[k].length();

            }
            if(expression.length() > leftSideIndex){
             String exp =    expression.toString().substring(leftSideIndex, expression.toString().length());
             expression = expression.replace(exp,"\"" + exp + "\"");
            }
        }else {
            expression = "\"" + Utils.javaEscape(expression.toString())+ "\"";
        }
        return expression;
    }

    public static String toObject( String clazz ) {

        String str = clazz.toLowerCase();
        if(clazz.contains(".")){
            str = clazz.substring(clazz.lastIndexOf(".")+1).toLowerCase();
        }

        if( str.equals("boolean") ) return "Boolean";
        if( str.equals("byte") ) return "Byte";
        if( str.equals("short") ) return "Short";
        if( str.equals("integer") ) return "Integer;";
        if( str.equals("long" ) ) return "Long";
        if( str.equals("float") ) return "Float";
        if( str.equals("double") ) return "Double";
        if( str.equals("string") ) return "String";
        return clazz;
    }

    public static String toObjectFullName( String clazz ) {

        String str = clazz.toLowerCase();
        if(clazz.contains(".")){
            str = clazz.substring(clazz.lastIndexOf(".")+1).toLowerCase();
        }

        if( str.equals("boolean") ) return "java.lang.Boolean";
        if( str.equals("byte") ) return "java.lang.Byte";
        if( str.equals("short") ) return "java.lang.Short";
        if( str.equals("integer") ) return "java.lang.Integer;";
        if( str.equals("long" ) ) return "java.lang.Long";
        if( str.equals("float") ) return "java.lang.Float";
        if( str.equals("double") ) return "java.lang.Double";
        if( str.equals("string") ) return "java.lang.String";
        return clazz;
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

    /*
    public static Class toPrimitiveClass( String clazz ) {

        String str = clazz.toLowerCase();
        if(clazz.contains(".")){
            str = clazz.substring(clazz.lastIndexOf(".")+1).toLowerCase();
        }


        if( str.equals("boolean") ) return boolean.class;
        if( str.equals("byte") ) return byte.class;
        if( str.equals("short") ) return short.class;
        if( str.equals("int") ) return int.class;
        if( str.equals("long" ) ) return long.class;
        if( str.equals("float") ) return float.class;
        if( str.equals("double") ) return double.class;
        return null;
    }


     */

    public static Object emptyObjectToDefault(String type,Object value){
        if(type != null && (value == null || value.toString().equals("[]"))){
            if(type.equals("java.util.ArrayList")) {
                return "new java.util.ArrayList();";
            }
            if(type.equals("java.util.LinkedList")) {
                return "new java.util.LinkedList();";
            }

        }
        return null;
    }


    public static String getJavaTypeDefaultValue(String type){
        switch (type){
            case "int" :
            case "double" :
            case "float" :
            case "long" :return "-1";
            case "boolean" : return "false";
            case "java.util.ArrayList" : return "new java.util.ArrayList();";
            case "java.util.LinkedList" : return "new java.util.LinkedList();";
            default: return  type ;
            //default: return "new "+ type+ "()";
        }
    }

    public static Class StringToClass(String clazz){
        try {
            return ClassUtils.getClass(clazz);

/*
            if(Utils.isPrimitive(clazz)){
                return getPrimitiveClass(clazz);
            }

            return Class.forName(clazz );

 */
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(String clazz, String name,String[] types){
        try {
            if(!name.contains("<init>")) {
                Class[] classes = new Class[types.length];
                for (int i = 0; i < types.length; i++) {
                    classes[i] = StringToClass(types[i]);
                }
                Class c = StringToClass(clazz);
                if(c != null && name != null && classes != null) {
                    return c.getDeclaredMethod(name, classes);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    public static boolean isEntity(Argument argument){
        try {
            if(Configuration.has("entity_identify")){
                String s = Configuration.get("entity_identify").asString();
                return (boolean) Espl.getInstance().evaluate(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


     */
    public static String[] fixsplitByCharacterTypeCamelCase(String[] words){
        if(words != null && words.length > 0) {
            List<String> newWords = new ArrayList<>();
            for (int i = 0; i < words.length; i++) {
                if (Character.isDigit(words[i].charAt(0))) {
                    String last = newWords.get(newWords.size() - 1);
                    last = last + words[i];
                    newWords.set(newWords.size() - 1, last);
                } else {
                    newWords.add(words[i]);
                }
            }
            return newWords.toArray(new String[newWords.size()]);
        }
        return null;
    }

    public static Object getAnnotationValue(Annotation annotation){
        try {
            Method method1 = annotation.annotationType().getDeclaredMethod("value");
            return method1.invoke(annotation);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CallBack buildCallBack(AbstractAnnotationScanner scanner){
        if(scanner.has("callback")){
            Map<?,?> map = scanner.get("callback").asMap();
            return CallBackBuilder.newBuilder().setName(map.containsKey("name") ? map.get("name").toString() : null)
                    .setType(map.containsKey("type") ? map.get("type").toString() : null)
                    .setEnabledAssert(map.containsKey("enabledAssert") ? (boolean)map.get("enabledAssert") : false)
                    .setOnFailureExpression(map.containsKey("onFailure") && ((Map)map.get("onFailure")).containsKey("expression")? Espl.getInstance().evaluate(((Map)map.get("onFailure")).get("expression").toString()).toString(): null)
                    .setOnFailureParameters(map.containsKey("onFailure") && ((Map)map.get("onFailure")).containsKey("parameters")? ((Map)map.get("onFailure")).get("parameters").toString(): null)
                    .setOnFailureReturnType(map.containsKey("onFailure") && ((Map)map.get("onFailure")).containsKey("returnType")? ((Map)map.get("onFailure")).get("returnType").toString(): null)
                    .setOnSuccessExpression(map.containsKey("onSuccess") && ((Map)map.get("onSuccess")).containsKey("expression")? Espl.getInstance().evaluate(((Map)map.get("onSuccess")).get("expression").toString()).toString(): null)
                    .setOnSuccessParameters(map.containsKey("onSuccess") && ((Map)map.get("onSuccess")).containsKey("parameters")? ((Map)map.get("onSuccess")).get("parameters").toString(): null)
                    .setOnSuccessReturnType(map.containsKey("onSuccess") && ((Map)map.get("onSuccess")).containsKey("returnType")? ((Map)map.get("onSuccess")).get("returnType").toString(): null)
                    .build()
                    .getCallBack();


        }
        return null;
    }

    public static boolean isInheritFrom(String type,String superclass){
        try {
            //if(Suite.hasEntity(type)) {
             //   type = Suite.getEntityClass(type);
                Class c = ClassUtils.getClass(type);
                List<Class<?>> superclasses = ClassUtils.getAllSuperclasses(c);
                for (Class clazz : superclasses) {
                    if (clazz.getTypeName().equals(superclass)) {
                        return true;
                    }
                }
            //}
        } catch (Throwable e) {
            return false;
        }
        return false;
    }

    public static String[] splitCamelCaseSentence(String CamelCaseSentence){
        return  StringUtilsWrapper.splitByCharacterTypeCamelCase(CamelCaseSentence);

    }

    public static List<String> getContext(String CamelCaseSentence){
        String[] sentence =  StringUtilsWrapper.splitByCharacterTypeCamelCase(CamelCaseSentence);
        List<String> entities = new ArrayList<>();
        for(int i=0;i<sentence.length;i++){
            if(EntityRules.isEntity(sentence[i])){
                entities.add(sentence[i]);
            }
        }
        return entities;
    }


    public static boolean isClassExist(String className){
        try{
            ClassUtils.getClass(className);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}

