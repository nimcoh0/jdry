package org.softauto.analyzer.annotations.tree;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.annotations.source.*;
import org.softauto.analyzer.core.rules.RequestRules;
import org.softauto.analyzer.core.rules.ResultRules;
import org.softauto.analyzer.core.rules.TestRules;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.listener.ListenerBuilder;
import org.softauto.analyzer.core.skeletal.tree.phase.Phase;
import org.softauto.analyzer.core.system.context.CtxBuilder;
import org.softauto.analyzer.core.system.context.ItemContext;
import org.softauto.analyzer.core.system.espl.Espl;
import org.softauto.analyzer.core.system.scanner.AbstractAnnotationScanner;
import org.softauto.analyzer.core.system.scanner.AnnotationScanner;
import org.softauto.analyzer.core.utils.Utils;
import java.util.*;


public abstract class AnnotationsTree implements Tree {

    private static Logger logger = LogManager.getLogger(AnnotationsTree.class);


    public static AbstractAnnotationScanner scanner;


    public static org.softauto.analyzer.model.Item item;

    public static GenericItem genericItem;

    public static Result result;

    public static HashMap<String ,Object> args ;

    public static ItemContext ctx;

    @Override
    public Phase.kind getPhaseKind() {
        return Phase.kind.ANNOTATIONS;
    }

    public static  <R,T,D> void init(TreeVisitor<R,T,D> visitor, T t, D d, R r, Tree.Kind kind) {

        item = null;

        genericItem = null;

        result = null;
        args = null;
        ctx = null;



        genericItem = t != null && t instanceof GenericItem ? (GenericItem) t : null;
        ctx = CtxBuilder.newBuilder().setKind(kind.name()).setGenericItem(genericItem).build().getItemContext();
        Espl.getInstance().addProperties(ctx.getCtx()).addProperty("Utils", Utils.class);
    }


    public static class Item  implements ItemTree {



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



}
