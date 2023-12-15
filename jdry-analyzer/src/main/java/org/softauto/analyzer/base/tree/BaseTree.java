package org.softauto.analyzer.base.tree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.base.source.*;
import org.softauto.analyzer.base.source.Tree;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.system.scanner.AnnotationScanner;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.plugin.ProviderManager;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;
import org.softauto.analyzer.core.system.scanner.AnnotationHelper;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseTree implements Tree {

    private static Logger logger = LogManager.getLogger(BaseTree.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public static class Item  implements ItemTree {


        public ClassTypeTree getClassTypeTree(){
            return new ClassType();
        }

        @Override
        public Kind getKind() {
            return Kind.ITEM;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            HashMap<String,Object>  callOption = null;
            List<String> l = Configuration.get("api_annotations").asList();
            LinkedList<String> pathList = (LinkedList<String>) ((ArrayList)l).stream().collect(Collectors.toCollection(LinkedList::new));
            if(AnnotationHelper.isExist(pathList,((GenericItem)t).getAnnotations())) {
                for (PluginProvider plugin : ProviderManager.providers(ClassLoader.getSystemClassLoader())) {
                    if (plugin.getType() != null && plugin.getType().equals("protocol")) {
                        Provider provider = plugin.create((GenericItem) t);
                        if(provider.isInterest((GenericItem)t)) {
                            callOption = provider.getAnalyzer((GenericItem) t).initialize();
                            ((GenericItem) t).setProperties(callOption);
                            if (callOption == null) {
                                return visitor.visitItem((ItemTree) this, t, d, r);
                            }
                        }
                    }
                }

            }
            return visitor.visitItem((ItemTree) this, t, d, r);
        }

    }

    public static class ClassType  implements ClassTypeTree {

        private static final String path = "Lorg/softauto/annotations/InitializeForTesting;";

        @Override
        public Kind getKind() {
            return Kind.CLASSTYPE;
        }



        @Override
        public <R, T, D> R accept(TreeVisitor<R, T, D> visitor, T t, D d, R r) {
            AbstractAnnotationScanner scanner =  new AnnotationScanner().setPath(path).setAnnotations(((GenericItem) t).getAnnotations()).scanner();
            if(scanner != null ){
                HashMap<String, Object> callIotion = ((GenericItem) t).getProperties();
                callIotion.put("classType",org.softauto.annotations.ClassType.fromString(scanner.get("value").asString()));
                if(scanner.has("parameters")){
                   Object p =  scanner.get("parameters").asObject();
                    List<HashMap<String, Object>> constructorParameters = new ArrayList<>();

                   if(p instanceof Map){
                     //for(Map.Entry entry :  ((HashMap<String,String>)p).entrySet()) {
                         HashMap<String, Object> parameter = new HashMap<>();
                         parameter.put(((HashMap<String, String>) p).get("type"), ((HashMap<String, String>) p).get("value"));
                         constructorParameters.add(parameter);
                     //}
                   }
                   if(p instanceof ArrayList){
                       for(Object o : (ArrayList)p){
                           HashMap<String, Object> parameter = new HashMap<>();
                           parameter.put(((HashMap<String, String>) p).get("type"), ((HashMap<String, String>) p).get("value"));
                           constructorParameters.add(parameter);
                       }
                   }
                    callIotion.put("constructor",constructorParameters);
                }
            }
            return visitor.visitClassType( this, t,d, r);
        }

    }



}
