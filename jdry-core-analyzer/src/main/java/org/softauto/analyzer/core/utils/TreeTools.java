package org.softauto.analyzer.core.utils;

import org.apache.commons.collections4.ListUtils;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.suite.Suite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreeTools {

    private static List<GenericItem> trees;

    private static Suite suite;

    public static void setSuite(Suite suite) {
        TreeTools.suite = suite;
    }

    public static void setTrees(List<GenericItem> trees) {
       TreeTools.trees = trees;
    }

    private static boolean findAnnotation(String annotation,List<String> pattern,List<String> excludePattern,List<String> list) {
        for (String p : pattern) {
            if (annotation.contains(p)) {
                for (String e : excludePattern) {
                    if (annotation.contains(e))
                        return false;
                }
                if (list == null) {
                    return false;
                } else if (!list.contains(annotation)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    public static boolean isDuplicateAnnotationMethod(String className,List<String> annotationList,List<String> pattern,List<String> excludePattern) {
        HashMap<String, List<String>> classListOfMethodsAnnotationSummery = Suite.getClassListOfMethodsAnnotationSummery();
        List<String> list = null;
        if (classListOfMethodsAnnotationSummery.containsKey(className)) {
            list = classListOfMethodsAnnotationSummery.get(className);
        }
        boolean found = false;
        //for (String p : pattern) {
        for (String annotation : annotationList) {
            found = findAnnotation(annotation,pattern,excludePattern,list);
            if(!found){
                return false;
            }
        }
        return found;
    }

    public static List<GenericItem> getTrees(String clazz){
        List<GenericItem> glist = new ArrayList<>();
        for(GenericItem tree : trees){
            if(tree.getNamespce().equals(clazz) ){
                glist.add(tree);
            }
        }
        return glist;
    }

    public static GenericItem getTree(String clazz, String method , List<String> parameterTypes){
            for(GenericItem tree : trees){
                if(tree.getNamespce().equals(clazz) && tree.getName().equals(method) && ListUtils.isEqualList(tree.getParametersTypes(),parameterTypes)){
                    return tree;
                }
            }
            return null;
    }

}
