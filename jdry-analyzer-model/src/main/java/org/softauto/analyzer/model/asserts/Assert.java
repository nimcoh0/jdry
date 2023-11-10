package org.softauto.analyzer.model.asserts;

import java.io.Serializable;

public class Assert implements Cloneable, Serializable {

    /**
     * assert description
     */
    private String description;

    /**
     * java expression according to Spring SpEL definition
     */
    private String expression;

    public String getDescription() {
        return description;
    }

    public Assert setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getExpression() {
        return expression;
    }

    public Assert setExpression(String expression) {
        this.expression = expression;
        return this;
    }
}
