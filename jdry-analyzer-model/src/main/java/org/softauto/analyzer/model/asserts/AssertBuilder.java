package org.softauto.analyzer.model.asserts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AssertBuilder {

    private static Logger logger = LogManager.getLogger(AssertBuilder.class);

    public static Builder newBuilder() { return new Builder();}

    private Assert anAssert;

    public AssertBuilder(Assert anAssert){
        this.anAssert = anAssert;
    }

    public Assert getAnAssert() {
        return anAssert;
    }

    public static class Builder   {

        boolean enabled = true;

        private String description;

        private String expression;

        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder setExpression(String expression) {
            this.expression = expression;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }



        public AssertBuilder build(){
            Assert anAssert = new Assert();
            try {
                anAssert.setDescription(description);
                anAssert.setExpression(expression);
                anAssert.setEnabled(enabled);
               // logger.debug("successfully build Assert ");
            } catch (Exception e) {
                logger.error("fail build Assert ",e);
            }
            return new AssertBuilder(anAssert);
        }
    }
}
