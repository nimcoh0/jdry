package org.softauto.analyzer.core.utils;

import java.util.HashMap;

public class TestNameGenerator {

    private  static TestNameGenerator testNameGenerator ;

    private HashMap<String,Integer> names = new HashMap<>();

    private TestNameGenerator(){

    }

    public static TestNameGenerator getInstance(){
        if(testNameGenerator == null){
            testNameGenerator = new TestNameGenerator() ;
        }
        return testNameGenerator;
    }


    public String generateNameIfExist(String name) {
        name = name.replace(".","_");
        String newName;
        if(names.containsKey(name)){
            Integer i = Integer.valueOf(names.get(name).toString());
            newName = name + "_"+(i++);
            names.put(name,i);
            return newName;
        }else {
            names.put(name,0);
            return name;
        }
    }

}
