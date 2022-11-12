package org.softauto.listener.impl;

import com.sun.tools.attach.VirtualMachine;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.softauto.listener.MultipleRecursiveToStringStyle;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Utils.class);

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


    public static URLClassLoader createClassLoader(URL[] _urls ) throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.addAll(Arrays.asList(_urls));
        URLClassLoader uRLClassLoader =  new URLClassLoader(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
        Thread.currentThread().setContextClassLoader(uRLClassLoader);
        return uRLClassLoader;
    }


    public static void addJarToClasspath(String f) {
        try {
            URL[] urls = new URL[1];
            urls[0] =(new File(f.trim()).toURL());
            URLClassLoader urlClassLoader = createClassLoader(urls );
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }


    public static  void startWeaver(String aspectjweaver){
        try {

            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            VirtualMachine jvm = VirtualMachine.attach(pid);
            jvm.loadAgent(aspectjweaver);
            jvm.detach();
            logger.info("Weaver Load successfully ");
        }catch (Exception e){
            logger.fatal("load Weaver fail ",e);
            System.exit(1);
        }

    }



    public static String buildMethodFQMN(String methodName, String clazz){
        return clazz.replace(".","_")+"_"+methodName;
    }


    public static void loadLib(String path, String name) {
        InputStream input = Listener.class.getClassLoader().getResourceAsStream(name);
        OutputStream outputStream = null;
        try {
            File fileOut = new File(path+"/"+name);
            outputStream = new FileOutputStream(fileOut);
            org.apache.commons.io.IOUtils.copy(input, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * get the arguments types
     **/
    public static Class[] getTypes(Object obj){
        Class[] types;
        if(obj instanceof Class<?>){
            types = new Class[1];
            types[0] = (Class)obj;
        }else {
            return (Class[])obj;
        }

        return types;
    }

    /**
     * get the arguments values
     **/
    public static Object[] getArgs(Object obj){
        Object[] args;

        if(!(obj instanceof Object[])){
            args = new Object[1];
            args[0] = obj;
        }else {
            return (Object[])obj;
        }

        return args;
    }


}
