package org.softauto.analyzer.annotations.source;

import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.suite.SuiteBuilder;
import org.softauto.analyzer.model.test.Test;

import java.util.List;

public interface SuiteTree extends Tree{

    org.softauto.analyzer.model.suite.Suite getSuite();



    List<Test> getTests();



    TestTree getTestTree();

    SuiteBuilder.Builder getSuiteBuilder();



    ListenerTree getListenerTree();



    int getCounter();

    void setCounter(int counter);

    List<String> getNames();

    void setNames(List<String> names);

    String getName(GenericItem genericItem);
}
