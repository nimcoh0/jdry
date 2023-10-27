package org.softauto.analyzer.model.after;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AfterBuilder {

    private static Logger logger = LogManager.getLogger(AfterBuilder.class);

    public static Builder newBuilder() { return new Builder();}

    private After after;

    public AfterBuilder(After after){
        this.after = after;
    }

    public After getAfter() {
        return after;
    }

    public static class Builder {

        protected Object expression;

        protected String parentResultName;

        private String type;

        private String name;

        private String parentName;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setParentName(String parentName) {
            this.parentName = parentName;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setParentResultName(String parentResultName) {
            this.parentResultName = parentResultName;
            return this;
        }

        public Builder setExpression(Object expression) {
            if(expression != null)
                this.expression = expression;
            return this;
        }

        public AfterBuilder build(){
            After after = new After();
            try {
                after.setExpression(expression);
                after.setParentResultName(parentResultName);
                after.setType(type);
                after.setName(name);
                after.setParentName(parentName);
                //logger.debug("successfully build After "+parentName);
            } catch (Exception e) {
                logger.error("fail build After "+parentName,e);
            }
            return new AfterBuilder(after);
        }
    }
}
