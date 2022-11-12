package org.softauto.injector.jvm;

import com.sun.tools.attach.VirtualMachine;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.management.ManagementFactory;

public class HeapHelperInitializer {

    private static final Logger logger = LogManager.getLogger(HeapHelperInitializer.class);
    private static HeapHelperInitializer jvmProviderImpl = null;
    String type = "JVM";

    public static HeapHelperInitializer getInstance(){
        if(jvmProviderImpl == null){
            jvmProviderImpl =  new HeapHelperInitializer();
        }
        return jvmProviderImpl;
    }



    public HeapHelperInitializer initialize() throws IOException {
        try {
            String path = System.getenv("temp");
            String name = "libHeapHelper.dll";
            loadLib(path,name);
            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            VirtualMachine jvm = VirtualMachine.attach(pid);
            jvm.loadAgentPath(path+"/"+name, null);
            logger.info("HeapHelper agent attach to pid " + pid + " successfully");
        }catch (Exception e){
            logger.error("HeapHelper agent fail to load",e);
        }
        return this;
    }

    private void loadLib(String path, String name) {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(name);
        OutputStream outputStream = null;
        try {
            File fileOut = new File(path+"/"+name);
            outputStream = new FileOutputStream(fileOut);
            IOUtils.copy(input, outputStream);
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




    public void shutdown() {
        HeapHelper.clean();
    }




}
