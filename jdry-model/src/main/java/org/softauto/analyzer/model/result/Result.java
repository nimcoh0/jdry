package org.softauto.analyzer.model.result;

import org.softauto.analyzer.model.argument.Argument;

import java.io.Serializable;

/**
 * method call result
 */
public class Result extends Argument implements Cloneable , Serializable {

    private String publishName;

    public String getPublishName() {
        return this.publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }
}
