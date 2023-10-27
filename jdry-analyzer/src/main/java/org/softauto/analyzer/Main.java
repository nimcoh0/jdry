package org.softauto.analyzer;


import org.softauto.analyzer.item.TreeScanner;
import org.softauto.analyzer.model.suite.Suite;


public class Main {

    static Suite suite;

    public static Suite getSuite() {
        return suite;
    }

    public static void main(String[] args) {
        org.softauto.analyzer.core.Main m = new org.softauto.analyzer.core.Main(args);
        build();
        m.save(suite);
    }

    public static void build(){
        suite = new TreeScanner().getSuite();

    }
}
