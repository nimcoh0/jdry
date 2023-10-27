package org.softauto.compiler;

import org.apache.commons.lang3.ClassUtils;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static boolean isPrimitive(String type){
        try {
            if(type != null) {

                String name = null;
                if (type.contains(".")) {
                    name = type.substring(type.lastIndexOf(".") + 1);
                }else{
                    name = type;
                }
                if (PRIMITIVES.contains(name)) {
                   return true;
                }


            }
        } catch (Exception e) {
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
}
