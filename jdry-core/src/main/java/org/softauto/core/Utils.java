package org.softauto.core;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
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


    public static Object instanceClass(String absoluteClassPath){
        try {
                String name = absoluteClassPath.substring(absoluteClassPath.lastIndexOf("/") + 1);
                String path = absoluteClassPath.substring(0, absoluteClassPath.lastIndexOf("/"));

                URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{
                        new URL(
                                "file:///" + path
                        )
                });

                Class clazz = urlClassLoader.loadClass(name);
                return clazz.getConstructors()[0].newInstance();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


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

    /*
    public static Object[] getConstructorDefaultValues(String fullClassName){
        List<Object> defaultValues = new ArrayList<>();
        try {
            Class klazz = Class.forName(fullClassName);
            Constructor<?>[] constructors = klazz.getConstructors();
            for (Constructor constructor : constructors) {
                Annotation[][] annotations = constructor.getParameterAnnotations();
                for (int i = 0; i < annotations.length; i++) {
                    for (int j = 0; j < annotations[i].length; j++) {
                        if (annotations[i][j].annotationType().getName().equals("org.softauto.annotations.DefaultValue")) {
                            String value = ((DefaultValue) annotations[i][j]).value();
                            if(value.equals("null"))
                                value = null;
                            String name = constructor.getParameters()[i].getName();
                            if (constructor.getParameters()[i].getAnnotation(DefaultValue.class).type() != null && !constructor.getParameters()[i].getAnnotation(DefaultValue.class).type().isEmpty()) {
                                String type = constructor.getParameters()[i].getAnnotation(DefaultValue.class).type().toString();
                                defaultValues.add(ObjectConverter.convert(value, constructor.getParameters()[i].getType(), type));
                            } else {
                                defaultValues.add(value);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return defaultValues.toArray();
    }



    public static Class[] extractConstructorDefaultArgsTypes(String fullClassName){
        try {
            Class c = Class.forName(fullClassName);
            Constructor[] constructors = c.getDeclaredConstructors();
            for(Constructor constructor: constructors){
                if(constructor.getParameters().length >0 &&  constructor.getParameters()[0].getAnnotation(DefaultValue.class) != null){
                    return constructor.getParameterTypes();
                }
            }
        }catch (Exception e){
            logger.error("fail extract Args Types for "+fullClassName,e);
        }
        return new Class[0];
    }

     */

    public static String getFullClassName(String descriptor){
        return  descriptor.substring(0,descriptor.lastIndexOf("_")).replace("_",".");
    }

    public static String getFullClassName2(String descriptor){
        return  descriptor.substring(0,descriptor.lastIndexOf("_"));
    }

    public static String getMethodName(String descriptor){
        return descriptor.substring(descriptor.lastIndexOf("_")+1,descriptor.length());
    }


    public static String getClassName(String descriptor){
        String str = getFullClassName2(descriptor);
        return  str.substring(str.lastIndexOf("_")+1);
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

    public static Class getClazz(String path,String clazzName){
        Class c = null;
        try{
            String localPath = path.substring(0,path.lastIndexOf("classes")+8);
            String clazz = path.substring(path.lastIndexOf("classes") + 8, path.length()).replace("/", ".")+"."+clazzName;
            //URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            URL[] urls = new URL[1];
            urls[0] =(new File(localPath.trim()).toURL());
            URLClassLoader urlClassLoader = createClassLoader(urls );
            //addURL(new File(localPath).toURL(),sysloader);
            c = (Class) urlClassLoader.loadClass(clazz );
        }catch (ClassNotFoundException e){
            logger.warn("class not found"+ path+"/"+clazzName);
        }catch (Exception e){
            logger.error("fail get class "+ path+"/"+clazzName,e);
        }
        return c;
    }

    protected static URLClassLoader createClassLoader(URL[] _urls ) throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.addAll(Arrays.asList(_urls));
        URLClassLoader uRLClassLoader =  new URLClassLoader(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
        Thread.currentThread().setContextClassLoader(uRLClassLoader);
        return uRLClassLoader;
    }

    public static Class<?> getSubClass(Class<?>[] classes, String name){
        for(Class<?> c : classes){
            if(c.getName().equals(name)){
                logger.debug("successfully found subclass for "+name);
                return c;
            }
        }
        logger.warn("subclass not found for "+name);
        return null;
    }

    public static Method getMethod2(Object o, String fullMethodName, Class[] types)throws Exception{
        try {
            logger.debug("trying to find method " + fullMethodName +" with types "+ result2String(types)+ " on "+o.getClass().getName());
            Method[] m = o.getClass().getMethods();
            if (o instanceof Class<?>) {
                Class c = (Class) o;
                Method method = c.getMethod(getMethodName(fullMethodName), types);
                logger.debug("found method " + fullMethodName);
                return method;
            }
            Method method = o.getClass().getMethod(fullMethodName, types);
            logger.debug("found method " + fullMethodName);
            return method;
        }catch (Exception e){
            logger.warn("fail get method 2 "+ fullMethodName,e.getMessage());
            return null;

        }

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

    public static boolean isPrimitive(String name){
        if(PRIMITIVES.contains(name.toLowerCase())){
            return true;
        }
        return false;
    }

    static final List<String> PRIMITIVES = new ArrayList<>();
    static {
        PRIMITIVES.add("string");
        PRIMITIVES.add("bytes");
        PRIMITIVES.add("int");
        PRIMITIVES.add("long");
        PRIMITIVES.add("float");
        PRIMITIVES.add("double");
        PRIMITIVES.add("boolean");
        PRIMITIVES.add("null");
        PRIMITIVES.add("void");
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
            if(str.startsWith("{")) {
                new ObjectMapper().readTree(str);
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static  String getVar(String s){
        if (s.toString().contains("${")) {
            String key = null;
            Pattern pattern = Pattern.compile("\\$\\{([^\\}]*)\\}");
            key = pattern
                    .matcher(s.toString())
                    .results()
                    .collect(Collectors.toList())
                    .get(0)
                    .group(1);
            return key;
            //return VariableResolver.setExpression("${" + key + "}").resolve().toString();
            //s = VariableResolver.setExpression(exp.toString()).resolve().toString();
        }
        return s;
    }

}
