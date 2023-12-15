package org.softauto.flow;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;
import org.softauto.clazz.ClassInfo;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import java.util.ArrayList;
import java.util.List;

public class FlowBuilder {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public static Builder newBuilder() { return new Builder();}

    FlowObject flowObject;

    public FlowBuilder(FlowObject flowObject){
        this.flowObject = flowObject;
    }

    public FlowObject getFlowObject() {
        return flowObject;
    }

    public static class Builder {

        private String name;

        private CallGraph cg;

        private String clazz;

        private ClassInfo classInfo;

        private List<FlowObject> chileds = new ArrayList<>();

        private SootMethod method ;

        private boolean isConstructor;

        private boolean isStaticInitializer;

        private boolean  isStatic;



        public Builder setStatic(boolean aStatic) {
            isStatic = aStatic;
            return this;
        }

        public Builder setStaticInitializer(boolean staticInitializer) {
            isStaticInitializer = staticInitializer;
            return this;
        }

        public Builder setConstructor(boolean constructor) {
            isConstructor = constructor;
            return this;
        }

        public Builder setClassInfo(ClassInfo classInfo) {
            this.classInfo = classInfo;
            return this;
        }

        public Builder setChileds(List<FlowObject> chileds) {
            this.chileds = chileds;
            return this;
        }

        public Builder addChiled(FlowObject chiled) {
            this.chileds.add(chiled);
            return this;
        }

        public Builder setClazz(String clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCg(CallGraph cg) {
            this.cg = cg;
            return this;
        }

        public Builder setMethod(SootMethod method) {
            this.method = method;
            return this;
        }




        public FlowBuilder build(){
           FlowObject flowObject = new FlowObject();
            try {
                String returnType = null;
                if (method.getName().equals("<init>")) {
                    returnType = clazz;
                } else {
                    returnType = method.getReturnType().toString();
                }
                flowObject.setCg(cg);
                flowObject.setMethod(method);
                flowObject.setName(name);
                flowObject.setClazz(clazz);
                flowObject.setChileds(chileds);
                flowObject.setClassInfo(classInfo);
                flowObject.setConstructor(isConstructor);
                flowObject.setStaticInitializer(isStaticInitializer);
                flowObject.setStatic(isStatic);
                flowObject.setReturnType(returnType);

                logger.debug(JDRY,"successfully build flowObject for "+ clazz + "." + method);
            } catch (Exception e) {
                logger.error(JDRY,"fail build flowObject for "+ clazz + "." + method);
            }
            return new FlowBuilder(flowObject);
        }
    }

}
