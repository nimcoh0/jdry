package org.softauto.analyzer.directivs.field;

import java.io.Serializable;

public class Field implements Cloneable, Serializable {

    String name;

    String namespace;

    Object expression;

    String type;

    public String getName() {
        return name;
    }

    public Field setName(String name) {
        this.name = name;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public Field setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public Object getExpression() {
        return expression;
    }

    public Field setExpression(Object expression) {
        this.expression = expression;
        return this;
    }

    public String getType() {
        return type;
    }

    public Field setType(String type) {
        this.type = type;
        return this;
    }
}
