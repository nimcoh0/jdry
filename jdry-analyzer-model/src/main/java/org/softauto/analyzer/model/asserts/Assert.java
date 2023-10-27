package org.softauto.analyzer.model.asserts;

import java.io.Serializable;

public class Assert implements Cloneable, Serializable {

    private String description;

    private String expression;

    boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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
