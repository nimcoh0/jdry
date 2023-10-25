package org.softauto.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.ArrayList;
import java.util.List;

public class Test {

    static ObjectMapper mapper = new ObjectMapperWrapper();

    public static void main(String[] args) {


        try {
            SimpleModule module = new SimpleModule();
            //module.addSerializer(Object.class,new LoopDetectionSerializer());
            module.setSerializerModifier(new ExBeanSerializerModifier());
            mapper.registerModule(module);
            Folder folder = new Folder();
            Folder folder1 = new Folder();
            List<File> files = new ArrayList<>();
            File file = new File();
            file.setName("bb");
            file.setFolder(folder);
            files.add(file);
            File file1 = new File();
            file1.setName("cc");
            file1.setFolder(folder1);
            files.add(file1);
            //Folder folder = new Folder();
            folder.setName("aa");
            folder.setFiles(files);
            folder1.setName("kk");
            folder1.setFiles(files);
            String f = mapper.writeValueAsString(folder);
            String f1 = mapper.writeValueAsString(folder);
            System.out.println(f);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
