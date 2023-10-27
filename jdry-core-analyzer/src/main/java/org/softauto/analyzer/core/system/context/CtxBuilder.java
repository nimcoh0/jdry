package org.softauto.analyzer.core.system.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.directivs.argument.Argument;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.core.rules.ProtocolRules;
import org.softauto.analyzer.core.rules.ResultRules;
import org.softauto.analyzer.core.utils.ResultUtils;
import org.softauto.analyzer.model.data.Data;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.test.Test;
import org.softauto.analyzer.core.utils.ProtocolUtils;
import org.softauto.analyzer.core.utils.Utils;
import java.util.HashMap;


public class CtxBuilder {

    private static Logger logger = LogManager.getLogger(CtxBuilder.class);

    public static Builder newBuilder() { return new Builder();}

    ItemContext itemContext;

    public ItemContext getItemContext() {
        return itemContext;
    }

    public CtxBuilder(ItemContext itemContext){
        this.itemContext = itemContext;
    }

    public static class Builder {

        GenericItem genericItem;

        Data data;

        Test test;

        String kind;

        public Builder setKind( String kind) {
            this.kind = kind;
            return this;
        }

        public Builder setTest(Test test) {
            this.test = test;
            return this;
        }

        public Builder setGenericItem(GenericItem genericItem) {
            this.genericItem = genericItem;
            return this;
        }

        public Builder setData(Data data) {
            this.data = data;
            return this;
        }

        public CtxBuilder build(){
            ItemContext ctx = null;
            try {
                ctx = new ItemContext();
                buildArgs(ctx);
                buildTestId(ctx);
                buildResult(ctx);
                buildExpected(ctx);
                ctx.addCtx("test",test);
                ctx.addCtx("method",genericItem);
                ctx.addCtx("class",Class.forName(genericItem.getNamespce()));
                ctx.addCtx("objectMapper",new ObjectMapper());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new CtxBuilder(ctx);
        }

        private void buildExpected(ItemContext ctx){
            try {
                if(ctx.getCtx("result") != null ){
                    ctx.addCtx("expected",ctx.getCtx("result"));
                }else {
                    ctx.addCtx("expected","expected");
                }
            } catch (Exception e) {
                logger.error("fail build context expected ",e);
            }
        }

        private void buildArgs(ItemContext ctx){
            try {
                //if(!AnnotationHelper.isTextExist(genericItem.getAnnotations(),"#result")) {
                    if(test != null && test.getData() != null && test.getData().getRequest() != null) {
                        for (Argument argument : test.getData().getRequest().getArguments()) {
                            ctx.addCtx(argument.getName(), argument.getValue());
                        }
                        return;
                    }else {
                        //}

                        HashMap<String, Object> args = new HashMap<>();
                        if (genericItem.getRequest() == null || genericItem.getRequest().getArguments() == null || genericItem.getRequest().getArguments().size() == 0) {
                            Request request = Utils.buildRequest(genericItem);
                            for (Argument argument : request.getArguments()) {
                                ctx.addCtx(argument.getName(), argument.getValue());
                            }
                        } else {
                            for (Argument argument : genericItem.getRequest().getArguments()) {
                                ctx.addCtx(argument.getName(), argument.getValue());
                            }
                        }
                        if (data != null && data.getRequest() != null && data.getRequest().getArguments().size() > 0) {
                            for (Argument argument : data.getRequest().getArguments()) {
                                ctx.addCtx(argument.getName(), argument.getValue());
                            }
                        }
                    }
            } catch (Exception e) {
               logger.error("fail build context args "+genericItem.getName(),e);
            }
        }



        private void buildResult(ItemContext ctx){
            try {
                HashMap<String,Object> res = new HashMap<>();
                Result result = new Result();

                    //if (test != null && !baseTree.getKind().equals(Tree.Kind.AFTER)) {
                    if (test != null && !kind.equals("AFTER")) {
                        result.setName(test.getExpected() != null ? test.getExpected().getName() : ResultRules.getResultName(genericItem));
                        result.setType(test.getExpected() != null ? test.getExpected().getType(): ResultRules.getResultType(genericItem, ProtocolRules.getDefaultProtocol()));
                        if (data != null && data.getResponse() != null && data.getResponse().getValue() != null) {
                            result.addValue(data.getResponse().getValue());
                        }

                    }else {


                    if (data == null) {

                        String protocol = null;
                        if(test != null){
                            protocol = test.getApi().getProtocol();
                        }else {
                            protocol = ProtocolUtils.getProtocol();
                        }
                        String resultType = ResultUtils.buildResultType(protocol,genericItem);
                        result = ResultUtils.buildResult(genericItem,resultType);
                    }
                    if (data != null && data.getResponse() != null) {
                        //result = Utils.buildResult(genericItem, Utils.buildResultString(genericItem, null), data);
                        String protocol = null;
                        if(test != null){
                            protocol = test.getApi().getProtocol();
                        }else {
                            protocol = ProtocolUtils.getProtocol();
                        }
                        String resultType = ResultUtils.buildResultType(protocol,genericItem);
                        result = ResultUtils.buildResult(genericItem,resultType);
                    }
                }

                ctx.addCtx("result", result.getName());
            } catch (Exception e) {
                logger.error("fail build context result "+ genericItem.getName(),e);
            }
        }

        private void buildTestId(ItemContext ctx){
            try {
                if(test != null){
                    ctx.addCtx("testId",test.getTestId());
                }else {
                    String name = genericItem.getName();
                    if (genericItem.getName().equals("<init>")) {
                        name = genericItem.getNamespce();
                    }
                    ctx.addCtx("testId", name);
                }
            } catch (Exception e) {
                logger.error("fail build context testId "+ genericItem.getName(),e);
            }
        }

    }
}
