package org.softauto.espl;

import org.springframework.expression.ParserContext;

public class TemplateParserContext implements ParserContext {

    public String getExpressionPrefix() {
        return "${";
    }

    public String getExpressionSuffix() {
        return "}";
    }

    public boolean isTemplate() {
        return true;
    }
}
