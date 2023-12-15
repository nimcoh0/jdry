package org.softauto.spel;

import org.springframework.expression.ParserContext;

public class TemplateParserContextCompileTime implements ParserContext {

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
