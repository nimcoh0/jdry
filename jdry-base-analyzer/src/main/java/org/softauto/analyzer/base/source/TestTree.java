package org.softauto.analyzer.base.source;

public interface TestTree extends Tree {

    ApiTree getApiTree();

    AssertTree getAssertTree();

    AfterTree getAfterTree();

    ExpectedTree getExpectedTree();
}
