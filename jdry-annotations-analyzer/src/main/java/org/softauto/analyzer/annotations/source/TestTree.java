package org.softauto.analyzer.annotations.source;

public interface TestTree extends Tree {

    org.softauto.analyzer.model.test.Test getTest();

    ApiTree getApiTree();

    DataTree getDataTree();

    AssertTree getAssertTree();

    AfterTree getAfterTree();

    PluginTree getPluginTree();

    ExpectedTree getExpectedTree();
}
