package org.softauto.analyzer.annotations.tree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.annotations.source.*;
import org.softauto.analyzer.core.rules.PublishRules;
import org.softauto.analyzer.core.rules.RequestRules;
import org.softauto.analyzer.core.rules.ResultRules;
import org.softauto.analyzer.core.rules.TestRules;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.model.after.AfterBuilder;
import org.softauto.analyzer.model.asserts.AssertBuilder;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.listener.ListenerBuilder;
import org.softauto.analyzer.core.skeletal.tree.phase.Phase;
import org.softauto.analyzer.core.system.context.CtxBuilder;
import org.softauto.analyzer.core.system.context.ItemContext;
import org.softauto.analyzer.core.system.espl.Espl;
import org.softauto.analyzer.core.system.plugin.ProviderManager;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.system.scanner.AnnotationScanner;
import org.softauto.analyzer.core.utils.ProtocolUtils;
import org.softauto.analyzer.core.utils.ResultUtils;
import org.softauto.analyzer.core.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AnnotationsTree implements Tree {

    private static Logger logger = LogManager.getLogger(AnnotationsTree.class);


    public static AbstractAnnotationScanner scanner;

    public  static org.softauto.analyzer.model.test.Test test;

    public  static org.softauto.analyzer.model.data.Data data;

    public  static org.softauto.analyzer.model.suite.Suite suite;

    public  static org.softauto.analyzer.model.api.Api api;

    public static org.softauto.analyzer.model.Item item;

    public static GenericItem genericItem;

    public static Result result;

    public static HashMap<String ,Object> args ;

    public static ItemContext ctx;

    @Override
    public kind getPhaseKind() {
        return kind.BASE;
    }

    public static  <R,T,D> void init(TreeVisitor<R,T,D> visitor, T t, D d, R r,Kind kind) {
        test = null;
        item = null;
        api = null;
        genericItem = null;
        data = null;
        result = null;
        args = null;
        ctx = null;
        if (r != null && r instanceof org.softauto.analyzer.model.test.Test) {
            test = (org.softauto.analyzer.model.test.Test) r;
        }
        if (r != null && r instanceof org.softauto.analyzer.model.suite.Suite) {
            suite = (org.softauto.analyzer.model.suite.Suite) r;
        }
        if (r != null && r instanceof org.softauto.analyzer.model.api.Api) {
            api = (org.softauto.analyzer.model.api.Api) r;
        }

        data = d != null && d instanceof org.softauto.analyzer.model.data.Data ? (org.softauto.analyzer.model.data.Data) d : null;
        genericItem = t != null && t instanceof GenericItem ? (GenericItem) t : null;
        ctx = CtxBuilder.newBuilder().setKind(kind.name()).setTest(test).setGenericItem(genericItem).setData(data).build().getItemContext();
        Espl.getInstance().addProperties(ctx.getCtx()).addProperty("Utils", Utils.class);
    }


    public static class Item  implements ItemTree {


        public ApiTree getApiTree(){
            return new Api();
        }

        @Override
        public PluginTree getPluginTree() {
            return new Plugin();
        }



        public ListenerTree getListenerTree(){
            return new Listener();
        }


        @Override
        public Kind getKind() {
            return Kind.ITEM;
        }

        @Override
        public <R, T, D> R accept(TreeVisitor<R, T, D> visitor, T t, D d, R r) {
            return visitor.visitItem((ItemTree) this, t, d, r);
        }

        @Override
        public kind getPhaseKind() {
            return kind.ANNOTATIONS;
        }
    }





    public static class Plugin   implements PluginTree {


        @Override
        public Kind getKind() {
            return Kind.PLUGIN;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor,t,d,r,Kind.PLUGIN);
            for (PluginProvider plugin : ProviderManager.providers(ClassLoader.getSystemClassLoader())) {
                if (plugin.getType().equals("protocol")) {
                    Provider provider = null;
                    HashMap<String, Object> annotationsCallOption = null;
                    if (r instanceof org.softauto.analyzer.model.test.Test) {
                        provider = plugin.create(genericItem.getAnnotations(), genericItem);
                        annotationsCallOption = provider.getAnalyzer(kind.ANNOTATIONS).initialize();
                    } else {
                        provider = plugin.create(genericItem);
                        annotationsCallOption = provider.getAnalyzer(Phase.kind.JDRYNONETREE).initialize();
                    }
                    try {

                        if (annotationsCallOption != null && annotationsCallOption.size() > 0) {
                            if (r instanceof org.softauto.analyzer.model.test.Test) {
                                ((org.softauto.analyzer.model.test.Test) r).getData().getPluginData().putAll(annotationsCallOption);
                            }

                            logger.debug("successfully got plugin " + plugin.getName() + " data for " + genericItem.getName());
                        }
                    } catch (Exception e) {
                        logger.error("fail getting plugin " + plugin.getName() + "data for " + genericItem.getName(), e);
                    }
                  }
                }

                return visitor.visitPlugin(this, t, d, r);

        }


        @Override
        public kind getPhaseKind() {
            return kind.ANNOTATIONS;
        }
    }


    public static class Api   implements ApiTree {

        public static final String API = "Lorg/softauto/annotations/ApiForTesting;";

        org.softauto.analyzer.model.api.Api api;

        public org.softauto.analyzer.model.api.Api getApi() {
            return api;
        }

        public AssertTree getAssertTree(){
            if(scanner != null) {
                return new Assert(scanner.get("anAssert").asString());
            }
            return new Assert();
        }



        public AfterTree getAfterTree(){
            if(scanner != null) {
                return new After();
            }
            return new After();
        }

        public ClassTypeTree getClassTypeTree(){
            return new ClassType();
        }

        @Override
        public CallBackTree getCallBackTree() {
            return new CallBack();
        }


        @Override
        public Kind getKind() {
            return Kind.API;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor,t,d,r,Kind.API);
            try {
                if(r instanceof org.softauto.analyzer.model.test.Test) {
                    scanner = new AnnotationScanner().setPath(API).setAnnotations(genericItem.getAnnotations()).scanner();
                    if (scanner != null) {
                       test.getApi().setProtocol(Utils.getProtocol(scanner,genericItem,test.getData().getPluginData()))
                                .setId(scanner != null && scanner.has("id") ? scanner.get("id").asString() : genericItem.getName())
                                .setCallback(scanner != null ? Utils.buildCallBack(scanner) : null);
                       test.setPublish(Utils.buildPublish((HashMap<String, Object>) (scanner.get("publish").asObject() instanceof ArrayList ? scanner.get("publish").asList() : scanner.get("publish").asMap())));

                        r = (R) test;
                        logger.debug("successfully update api for "+test.getFullName());
                    }

                }
            } catch (Exception e) {
                logger.error("fail update api for "+ test.getFullName(),e);
            }
            return visitor.visitApi((ApiTree) this, t,d, r);
        }

        @Override
        public kind getPhaseKind() {
            return kind.ANNOTATIONS;
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
            init(visitor,t,d,r,Kind.CLASSTYPE);
            HashMap<String ,Object> args = data != null ? Utils.getArgsAsObject(data.getRequest().getArguments()) : null;
            String protocol = null;
            if(test != null) {
                protocol = test.getApi().getProtocol();
            }else {
                protocol = ProtocolUtils.getProtocol();
            }
            String resultType = ResultUtils.buildResultType(protocol,genericItem);
            Result result = ResultUtils.buildResult(genericItem,resultType);


            Object res = Utils.getResultAsObject(result) ;
            scanner =  new AnnotationScanner().setPath(path).addObj(args).addObj("expected",res).addObj("result", result.getName()).setAnnotations(genericItem.getAnnotations()).scanner();
            if(scanner != null ){
                api.setClassType(scanner.get("mode").asString());
            }
            return visitor.visitClassType( this, t,d, r);
        }

        @Override
        public kind getPhaseKind() {
            return kind.ANNOTATIONS;
        }
    }



    public static class Assert  implements AssertTree {

        public String _anAssert;

        public org.softauto.analyzer.model.asserts.Assert anAssert;

        public Assert(String anAssert){
            this._anAssert = anAssert;
        }

        public Assert(){

        }

        @Override
        public Kind getKind() {
            return Kind.ASSERTS;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor,t,d,r,Kind.ASSERTS);
            if (r instanceof org.softauto.analyzer.model.test.Test && !genericItem.getReturnType().equals("void")){
                try {
                    if (_anAssert != null ) {
                        AssertBuilder assertBuilder = (AssertBuilder) AssertBuilder.newBuilder()
                                .setExpression(Espl.getInstance().addProperty("result", test.getExpected().getName()).evaluate(_anAssert).toString())
                                .setExpression(Espl.getInstance().addProperty("result", test.getExpected().getName()).evaluate(_anAssert).toString())
                                .build();

                        anAssert = assertBuilder.getAnAssert();
                        test.setAnAssert(anAssert);
                    }else {
                        AssertBuilder assertBuilder = (AssertBuilder) AssertBuilder.newBuilder()
                                .setExpression(Utils.getDefaultAsertByType(test.getExpected().getType(),test.getExpected().getName()))
                                .build();
                        anAssert = assertBuilder.getAnAssert();
                        test.setAnAssert(anAssert);
                    }
                    logger.debug("successfully update assert for "+test.getFullName());
                } catch (Exception e) {
                    logger.error("fail update assert for "+test.getFullName(),e);
                }
            }


            return visitor.visitAssert( this, t,d, r);
        }


        @Override
        public kind getPhaseKind() {
            return kind.ANNOTATIONS;
        }
    }

    public static class Listener  implements ListenerTree {

        org.softauto.analyzer.model.listener.Listener listener;

        public static final String path = "Lorg/softauto/annotations/ListenerForTesting;";

        @Override
        public Kind getKind() {
            return Kind.LISTENER;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor,t,d,r,Kind.LISTENER);
            try {
                scanner =  new AnnotationScanner().setPath(path).setAnnotations(((GenericItem)t).getAnnotations()).scanner();
                if(scanner != null) {
                    String protocol = "RPC";
                    Result result = ResultRules.getResult(genericItem,protocol);
                    ListenerBuilder listenerBuilder = (ListenerBuilder) ListenerBuilder.newBuilder()
                            .setResult(result)
                            .setExpression(scanner.get("value").getMap(0).get("value"))
                            .setId(genericItem.getName())
                            .setDiscoveryId(String.valueOf(genericItem.getId()))
                            .setRequest(RequestRules.getRequest(genericItem))
                            .setMethod(genericItem.getName())
                            .setNamespce(genericItem.getNamespce())
                            .setFullName(TestRules.buildFullName(genericItem))
                            .build();
                    logger.debug("successfully create listener for "+genericItem.getName());
                    listener = listenerBuilder.getListener();
                    r = (R) listener;
                    }

            } catch (Exception e) {
                logger.error("fail create listener for "+genericItem.getName(),e);
            }


            return visitor.visitListener((ListenerTree) this, t,d, r);
        }

        @Override
        public kind getPhaseKind() {
            return kind.ANNOTATIONS;
        }
    }





    public static class After  implements AfterTree {

        public static final String API = "Lorg/softauto/annotations/ApiForTesting;";

        public org.softauto.analyzer.model.after.After after;

        private org.softauto.analyzer.model.after.After parse(HashMap map){
            after = AfterBuilder.newBuilder()
                    .setExpression(map.containsKey("value") ? map.get("value").toString() : test.getData().getResponse().getName())
                    .setType(map.get("type") != null ? map.get("type").toString() : test.getData().getResponse().getType())
                    .setName( "after_"+test.getData().getResponse().getName())
                    .setParentResultName(test.getApi().getResponse().getName())
                    .setParentName(test.getApi().getFullName())
                    .build()
                    .getAfter();
            return after;
        }

        @Override
        public Kind getKind() {
            return Kind.AFTER;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor,t,d,r,Kind.AFTER);
            try {
                scanner = new AnnotationScanner().setPath(API).setAnnotations(genericItem.getAnnotations()).scanner();
                if (scanner != null) {
                    for (LinkedHashMap<String,Object> hm : scanner.getMapList()) {
                        if (hm.containsKey("after")) {
                            test.getApi().setAfterList(new LinkedList<>());
                            Object o = hm.get("after");
                            if(o instanceof Map){
                                org.softauto.analyzer.model.after.After after =   parse((HashMap)o);
                                test.getApi().addAfter(after);
                                test.getExpected().setName(after.getName()); //.setResult(after.getName());
                                test.getExpected().setType(after.getType());//.setResultType(after.getType());
                                String str = PublishRules.buildTestPublishResultName(((HashMap)o).get("name") != null ? ((HashMap)o).get("name").toString() : test.getResultPublishName());
                                test.setResultPublishName(str);
                            }
                            if(o instanceof ArrayList<?>){
                               LinkedList<HashMap<?,?>> maps = (LinkedList<HashMap<?, ?>>) ((ArrayList)o).stream().collect(Collectors.toCollection(LinkedList::new));
                               for(HashMap<?,?> map : maps){
                                   org.softauto.analyzer.model.after.After after =   parse(map);
                                   test.getApi().addAfter(after);
                                   test.getExpected().setName(after.getName()); //.setResult(after.getName());
                                   test.getExpected().setType(after.getType());//.setResultType(after.getType());
                                   String str = PublishRules.buildTestPublishResultName(map.get("name") != null ? map.get("name").toString() : test.getResultPublishName());
                                   test.setResultPublishName(str);
                               }
                            }


                            logger.debug("successfully create After for " + genericItem.getName());
                        }
                    }
                }

            } catch (Exception e) {
                logger.error("fail create After for "+genericItem.getName(),e);
            }
            return visitor.visitAfter((AfterTree) this, t,d, r);
        }



        @Override
        public kind getPhaseKind() {
            return kind.ANNOTATIONS;
        }
    }


    public static class CallBack  implements CallBackTree {
        @Override
        public Kind getKind() {
            return Kind.CALLBACK;
        }

        @Override
        public <R, T, D> R accept(TreeVisitor<R, T, D> visitor, T t, D d, R r) {
            init(visitor, t, d, r,Kind.CALLBACK);
            try {
                if(r instanceof org.softauto.analyzer.model.test.Test) {
                    if(test.getApi().getCallback() != null){
                        for(int i=0;i<test.getApi().getRequest().getArguments().size();i++){
                            if(test.getApi().getRequest().getArguments().get(i).getName().equals(test.getApi().getCallback().getName())){
                                test.getApi().getRequest().getArguments().get(i).setCallback(true);
                                test.getData().getRequest().getArguments().get(i).setCallback(true);
                                test.getExpected().setType(test.getApi().getCallback().getType());
                                test.getExpected().setName(test.getApi().getCallback().getName());
                                test.getAnAssert().setEnabled(test.getApi().getCallback().isEnabledAssert());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("fail CallBack for " + genericItem.getName(), e);
            }
            return visitor.visitCallBack((CallBackTree) this, t, d, r);
        }

        @Override
        public kind getPhaseKind() {
            return kind.ANNOTATIONS;
        }
    }
}
