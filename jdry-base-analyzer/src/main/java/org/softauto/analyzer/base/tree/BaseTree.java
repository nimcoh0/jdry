package org.softauto.analyzer.base.tree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.base.source.*;
import org.softauto.analyzer.base.source.Tree;
import org.softauto.analyzer.core.rules.*;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.model.after.AfterBuilder;
import org.softauto.analyzer.model.api.ApiBuilder;
import org.softauto.analyzer.model.asserts.AssertBuilder;
import org.softauto.analyzer.model.data.DataBuilder;
import org.softauto.analyzer.model.expected.ExpectedBuilder;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.suite.Suite;
import org.softauto.analyzer.model.test.TestBuilder;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.context.CtxBuilder;
import org.softauto.analyzer.core.system.context.ItemContext;
import org.softauto.analyzer.core.system.espl.Espl;
import org.softauto.analyzer.core.system.plugin.ProviderManager;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;
import org.softauto.analyzer.core.system.scanner.AnnotationHelper;
import org.softauto.analyzer.core.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseTree implements Tree {

    private static Logger logger = LogManager.getLogger(BaseTree.class);

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

        @Override
        public TestTree getTestTree() {
            return new Test();
        }

        public DataTree getDataTree(){
            return new Data();
        }


        @Override
        public Kind getKind() {
            return Kind.ITEM;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor,t,d,r,Kind.ITEM);
            HashMap<String,Object>  callOption = null;
            List<String> l = Configuration.get("api_annotations").asList();
            LinkedList<String> pathList = (LinkedList<String>) ((ArrayList)l).stream().collect(Collectors.toCollection(LinkedList::new));
            if(AnnotationHelper.isExist(pathList,((GenericItem)t).getAnnotations())) {
                for (PluginProvider plugin : ProviderManager.providers(ClassLoader.getSystemClassLoader())) {
                    if (plugin.getType() != null && plugin.getType().equals("protocol")) {
                        Provider provider = plugin.create((GenericItem) t);
                        callOption = provider.getAnalyzer(kind.BASE).initialize();
                        if (callOption == null) {
                            return visitor.visitItem((ItemTree) this, t, d, r);
                        }
                    }
                }
                return visitor.visitItem((ItemTree) this, t, d, (R) callOption);
            }
            return r;
        }


        @Override
        public kind getPhaseKind() {
            return kind.BASE;
        }
    }

    public static class Test  implements TestTree {

        org.softauto.analyzer.model.test.Test test;

        @Override
        public Kind getKind() {
            return Kind.TEST;
        }

        @Override
        public AssertTree getAssertTree() {
            return new Assert();
        }

        @Override
        public AfterTree getAfterTree() {
            return new After();
        }

        @Override
        public ExpectedTree getExpectedTree() {
            return new Expected();
        }

        public ApiTree getApiTree(){
            return new Api();
        }


        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor,t,d,r,Kind.TEST);
            try {
                   TestBuilder.Builder testBuilder = TestBuilder.newBuilder()
                            .setAnnotations(genericItem.getAnnotations())
                            .setTestId(TestRules.createTestId(genericItem))
                            .setFullName(TestRules.createTestFullName(genericItem))
                            .setNamespace(genericItem.getNamespce())
                            //.setCrud(genericItem.getCrudToSubject())
                            .setName(TestRules.createTestName(genericItem));
                    test = testBuilder.build().getTest();
                    if(r instanceof org.softauto.analyzer.model.data.Data){
                        test.setData((org.softauto.analyzer.model.data.Data)r);
                    }
                    //Object context = TestRules.getTestContext(test);
                    //test.setContext(context != null ? context.toString().toLowerCase() : null);
                    //Object subject = TestRules.getTestSubject(test);
                    //test.setSubject(subject != null ? subject.toString().toLowerCase(): null);

                    r = (R) test;

                    logger.debug("successfully create test  "+ test.getFullName());
            } catch (Throwable e) {
                logger.error("fail create test " + genericItem.getName(),e);
            }
            return visitor.visitTest((TestTree) this, t,d, r);
        }

        @Override
        public kind getPhaseKind() {
            return kind.BASE;
        }
    }

    public static class Data  implements DataTree {


        @Override
        public kind getPhaseKind() {
            return kind.BASE;
        }


        @Override
        public Kind getKind() {
            return Kind.DATA;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor,t,d,r,Kind.DATA);
                  d = (D) DataBuilder.newBuilder()
                            .setPluginData(r instanceof HashMap ? (HashMap<String, Object>) r : new HashMap<>())
                            .setRequest(RequestRules.getDataRequest(genericItem))
                            .setMethod(genericItem.getName())
                            .setClazz(genericItem.getNamespce())
                            .build()
                            .getData();

                  data = (org.softauto.analyzer.model.data.Data) d;
                  data.setResponse(ResultRules.getResult(genericItem, ProtocolRules.getProtocol(data.getPluginData())));
                  logger.debug("create basic data for test " + genericItem.getName());
            return visitor.visitData(this, t,d, (R)data);
        }
    }



    public static class Api  implements ApiTree {

        private static final List<String> pathList = Configuration.get("api_annotations").asList();

        org.softauto.analyzer.model.api.Api api;

        private boolean isApi(){
            for(String path : pathList) {
                if (AnnotationHelper.isExist(path.replace(".","/"), genericItem.getAnnotations(), null)) {
                    return true;
                }
            }
            return false;
        }

        private void fixCrud(org.softauto.analyzer.model.test.Test test){
            List<HashMap<String, String>> cruds =  test.getCrud();
            for(HashMap<String,String> hm : cruds){
                for(Map.Entry entry : hm.entrySet()){
                    if(entry.getValue() == null ){
                        String entity = Suite.getEntityClass(test.getSubject());
                        entry.setValue(entity);
                    }
                }
            }
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
                    if (isApi()) {
                        ApiBuilder.Builder apiBuilder = (ApiBuilder.Builder) ApiBuilder.newBuilder()
                                .setProtocol(ProtocolRules.getProtocol(test))
                                .setRequest(RequestRules.getRequest(genericItem))
                                .setChildes(Utils.buildChildes(genericItem.getChildes(), new ArrayList<>()))
                                //.setDiscoveryId(String.valueOf((genericItem.getId())))
                                .setFullName(ApiRules.buildFullName(genericItem))
                                .setMethod(ApiRules.buildName(genericItem))
                                .setNamespce(genericItem.getNamespce());

                        api = apiBuilder.build().getApi();
                        api.setResponse(ResultRules.getResult(genericItem,api.getProtocol()));
                        test.setApi(api);
                        test.setResultPublishName(TestRules.createTestREsultPublishName(genericItem.getReturnType()));
                        //fixCrud(test);
                        logger.debug("successfully create api for test " + test.getFullName());
                        return visitor.visitApi((ApiTree) this, t, d, r);
                    }
                }
            } catch (Exception e) {
                logger.error("fail create api for test "+ test.getFullName(),e);
            }
            return r;
        }

        @Override
        public kind getPhaseKind() {
            return kind.BASE;
        }
    }

    public static class After  implements AfterTree {

        @Override
        public Kind getKind() {
            return Kind.AFTER;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor, t, d, r,Kind.AFTER);
            try {
                if(!genericItem.getReturnType().equals("void") && test.getApi().getResponse().getType() != null && !test.getApi().getResponse().getType().equals(genericItem.getReturnType())){

                    org.softauto.analyzer.model.after.After after = AfterBuilder.newBuilder()
                            .setExpression(AfterRules.buildExpression(test))
                            .setType(AfterRules.getType(genericItem))
                            .setName(AfterRules.getName(test))
                            .setParentResultName(test.getApi().getResponse().getName())
                            .setParentName(test.getApi().getFullName())
                            .build()
                            .getAfter();
                    test.getApi().addAfter(after);
                    test.getExpected().setName(after.getName());
                    test.getExpected().setType(after.getType());
                    test.getData().getResponse().setType(after.getType());
                    test.setResultPublishName(test.getResultPublishName().equals("result") ? TestRules.createTestREsultPublishName(after.getType()): test.getResultPublishName());
                    logger.debug("successfully create after api for " + test.getFullName());

                }
            } catch (Exception e) {
               logger.error("fail create after api for test " + test.getFullName(),e);
            }
            return visitor.visitAfter((AfterTree) this, t, d, r);
        }

        @Override
        public kind getPhaseKind() {
            return kind.BASE;
        }
    }

    public static class Expected  implements ExpectedTree {
        @Override
        public Kind getKind() {
            return Kind.EXPECTED;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor,t,d,r,Kind.EXPECTED);
            if(r instanceof org.softauto.analyzer.model.test.Test){
                org.softauto.analyzer.model.expected.Expected expected = ExpectedBuilder.newBuilder()
                        .setName(ResultRules.getResultName(genericItem))
                        .setType(ResultRules.getResultType(genericItem,test.getApi().getProtocol()))
                        .build()
                        .getExpected();
                test.setExpected(expected);
            }

            return visitor.visitExpected( this, t,d, r);
        }

        @Override
        public kind getPhaseKind() {
            return kind.BASE;
        }
    }


    public static class Assert  implements AssertTree {

        public org.softauto.analyzer.model.asserts.Assert anAssert;

        @Override
        public Kind getKind() {
            return Kind.ASSERTS;
        }

        @Override
        public <R,T,D> R accept(TreeVisitor<R,T,D> visitor, T t, D d, R r) {
            init(visitor,t,d,r,Kind.ASSERTS);
            if(r instanceof org.softauto.analyzer.model.test.Test && (!genericItem.getReturnType().equals("void") || test.getExpected().getType() != "void")) {
                AssertBuilder assertBuilder = (AssertBuilder) AssertBuilder.newBuilder()
                        .setExpression(AssertRules.buildExpression(test))
                        .build();
                anAssert = assertBuilder.getAnAssert();
                test.setAnAssert(anAssert);
            }

            logger.debug("successfully create assert for " + test.getFullName());
            return visitor.visitAssert( this, t,d, r);
        }

        @Override
        public kind getPhaseKind() {
            return kind.BASE;
        }


    }

}
