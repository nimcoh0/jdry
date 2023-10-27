package org.softauto.jaxrs.analyzer.initializers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.core.utils.Analyzer;
import org.softauto.analyzer.core.utils.TreeTools;
import org.softauto.jaxrs.analyzer.analyzers.AnalyzeAnnotations;
import org.softauto.jaxrs.analyzer.handlers.HandleRequestType;
import org.softauto.jaxrs.analyzer.util.CallOptionBuilder;

import java.util.*;

public class BaseInitializer implements Analyzer {

    private static Logger logger = LogManager.getLogger(BaseInitializer.class);

    static GenericItem tree;

    private Provider provider;

    public BaseInitializer setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }


    //static Test test;

    public BaseInitializer setTree(GenericItem tree) {
        BaseInitializer.tree = tree;
        return this;
    }

    //public  BaseInitializer setTest(Test test) {
      //  BaseInitializer.test = test;
      //  return this;
    //}

    private boolean isParamAnnotation(String annotation){
        if(annotation.contains(".ws.rs.QueryParam") || annotation.contains(".ws.rs.FormParam") || annotation.contains(".ws.rs.PathParam") || annotation.contains(".ws.rs.BeanParam") || annotation.contains(".ws.rs.HeaderParam") || annotation.contains(".ws.rs.MatrixParam")){
            return true;
        }
        return false;
    }

    private boolean isEndPoint(String annotation){
        if(annotation.contains(".ws.rs.POST") || annotation.contains(".ws.rs.GET") || annotation.contains(".ws.rs.DELETE") || annotation.contains(".ws.rs.PUT")){
            return true;
        }
        return false;
    }

    private boolean isEndPoint(){
        if(tree.getAnnotations().toString().contains("/ws/rs/POST;") || tree.getAnnotations().toString().contains("/ws/rs/GET;") || tree.getAnnotations().toString().contains("/ws/rs/DELETE;") || tree.getAnnotations().toString().contains("/ws/rs/PUT;")){
            return true;
        }
        return false;
    }

    private boolean isPath(String annotation){
        if(annotation.contains(".ws.rs.Path") ){
            return true;
        }
        return false;
    }

    private boolean isController(){
        if(tree.getAnnotations().toString().contains("/ws/rs/Path;") ){
            return true;
        }
        return false;
    }

    private String getRestMainClass(LinkedHashMap<String,Object> classList){
        for(Map.Entry classes : classList.entrySet()){
            for(Map.Entry annotations : ((LinkedHashMap<String,Object>)classes.getValue()).entrySet()){
                if(annotations.getKey().equals("Ljavax/ws/rs/Path;")){
                    return classes.getKey().toString();
                }
            }
        }
        return null;
    }

    /*
    private void updateTest(String mainClass){
        if(!mainClass.equals(test.getNamespace())) {
            test.setNamespace(mainClass);
            test.setFullName((mainClass + "." + test.getName()).replace(".", "_"));
            test.setResult("result_" + mainClass.replace(".", "_") + test.getApi().getMethod());
            test.
        }
    }


     */

    public  HashMap<String,Object> initialize() {
        HashMap<String,Object> callOption = new HashMap<>();
        if(isEndPoint()) {
            try {
                    boolean r = updateGenericItem(tree);
                    if (r) {
                        AnalyzeAnnotations analyzeAnnotations = new AnalyzeAnnotations().setTree(tree).build();
                        callOption = CallOptionBuilder.newBuilder().setConsume(analyzeAnnotations.getConsume())
                                .setProduce(analyzeAnnotations.getProduce())
                                .setTree(tree)
                                .setProvider(provider)
                                .setPath(analyzeAnnotations.getPathList())
                                .build()
                                .getCallOption();
                        callOption.put("argumentsNames", tree.getArgumentsNames());
                        callOption.put("protocol", "JAXRS");
                        HashMap<String, Object> argumentsRequestType = HandleRequestType.setTree(tree).build().getArgumentsRequestType();
                        if (argumentsRequestType.size() > 0) {
                            callOption.put("argumentsRequestType", argumentsRequestType);
                        }

                        logger.debug("successfully initialize plugin data " + tree.getName());
                        return callOption;
                    }
                } catch(Exception e){
                    logger.error("fail initialize " + tree.getName(), e);
                }
                //return callOption;
            }

        return null;
    }

    private boolean updateGenericItem(GenericItem genericItem) {
        String mainClass = getRestMainClass(genericItem.getClassList());
        if(mainClass.equals(genericItem.getNamespce())){
            return true;
        }else if(!isDuplicateRestCall(genericItem)){
            genericItem.setNamespce(mainClass);
            genericItem.setNamespaceChange(true);
            return true;
        }
        return false;
    }

/*
    private boolean updateGenericItem(GenericItem genericItem) {
        String mainClass = getRestMainClass(genericItem.getClassList());
        boolean start = false;
        Boolean r = false;
        String[] keys = genericItem.getClassList().keySet().toArray(new String[genericItem.getClassList().size()]);
        for(int i=keys.length-1;i>0;i--){
        //for(Map.Entry entry : genericItem.getClassList().entrySet()){
            if(keys[i].equals(mainClass)){
                start = true;
            }
            if(keys[i].equals(genericItem.getName())){
                start = false;
            }
            if(start){
                //Boolean r = false;
                //HashMap<String,Object> map = (HashMap<String, Object>) genericItem.getClassList().get(keys[i]);
                if(!keys[i].equals(genericItem.getNamespce())) {
                    r = isDuplicateRestCall(keys[i].toString(), genericItem);
                    if(r){
                        return false;
                    }
                }
            }
        }
        if(!r ) {
            genericItem.setNamespce(mainClass);
            genericItem.setNamespaceChange(true);
            return true;
        }
        return false;
    }


 */


    private boolean isDuplicateRestCall(GenericItem genericItem){
       try {
          boolean start = false;
          for(Map.Entry entry : genericItem.getClassList().entrySet()) {
              if (entry.getKey().equals(genericItem.getNamespce() )) {
                start = true;
              }
              if(start && !entry.getKey().equals(genericItem.getNamespce())){
                  List<String> list = new ArrayList<>();
                  list.add(".ws.rs.Path");
                  list.add(".ws.rs.POST");
                  list.add(".ws.rs.GET");
                  list.add(".ws.rs.DELETE");
                  list.add(".ws.rs.UPDATE");
                  List<String> excludeList = new ArrayList<>();
                  excludeList.add(".ws.rs.PathParam");
                  if (TreeTools.isDuplicateAnnotationMethod(entry.getKey().toString(), genericItem.getAnnotationsHelper(), list,excludeList)) {
                      return true;
                  }
              }
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

/*
    private boolean isDuplicateRestCall(String mainClass,GenericItem genericItem){
        try {
            Class<?> controller =  ClassUtils.getClass(mainClass);
            return isEndPointExistInClass(controller,genericItem.getAnnotations());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


 */
/*
    private boolean isDuplicateRestCall(String mainClass,GenericItem genericItem){
        try {
            Class<?> controller =  ClassUtils.getClass(mainClass);
            List<String> parametersTypes  = genericItem.getParametersTypes();
            Class[] classes = new Class[parametersTypes.size()];
            for(int i=0;i<parametersTypes.size();i++ ){
                Class c = ClassUtils.getClass(parametersTypes.get(i));
                classes[i] = c;
            }
            Method m = controller.getDeclaredMethod(genericItem.getName(),classes);
            if(m != null){
                boolean found = false;
                Annotation[] annotations =  m.getAnnotations();
                if(annotations != null && annotations.length > 0){
                    for(Map.Entry entry : genericItem.getAnnotations().entrySet()){
                        if(entry.getKey().toString().contains("/ws/rs") ){
                            String key = entry.getKey().toString();
                            key = key.substring(1,key.length()-1).replace("/",".");
                            for(int j=0;j<annotations.length;j++){
                                if(annotations[j].annotationType().getTypeName().equals(key) ){
                                    Object o = Utils.getAnnotationValue(annotations[j]);
                                    if(((Map)entry.getValue()).get("value").equals(o)) {
                                        found = true;
                                    }
                                }
                            }
                            if(!found){
                                return false;
                            }
                        }
                    }
                }

                return true;
            }
            //String types = Utils.getListStringAsArrayTypesString(parametersTypes);
            //Object o = Espl.getInstance().addProperty("class",controller).evaluate("${#class.getDeclaredMethod("+genericItem.getName()+","+types+")}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


 */

    /*
    private boolean isEndPointExistInClass(Class c,HashMap<String,Object> annotations){
        for(Method m : c.getDeclaredMethods()){
          boolean result =   isEndPointExist(m,annotations);
          if(result){
              return true;
          }
        }
        return false;
    }

     */

    /*
    private boolean isEndPointExist(Method m,HashMap<String,Object> annotations){
            boolean found = false;
            Annotation[] localAnnotations = m.getAnnotations();
            for(Map.Entry annotation : annotations.entrySet()){
                found = false;
                if(annotation.getKey().toString().contains("/ws/rs") ) {
                    for (Annotation local : localAnnotations) {
                        String key = annotation.getKey().toString();
                        key = key.substring(1, key.length() - 1).replace("/", ".");
                        if((isPath(key) || isEndPoint(key)) && !isParamAnnotation(key)) {
                            if (key.equals(local.annotationType().getTypeName())) {
                                Object localValue = Utils.getAnnotationValue(local);
                                if (localValue != null && localValue.equals(((Map) annotation.getValue()).get("value"))) {
                                    found = true;
                                }
                            }
                        }
                    }
                }
            }
            if(!found){
                return false;
            }
        return found;
    }

     */
}
