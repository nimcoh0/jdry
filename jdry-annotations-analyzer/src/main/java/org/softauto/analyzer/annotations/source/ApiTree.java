package org.softauto.analyzer.annotations.source;

public interface ApiTree extends Tree{

   org.softauto.analyzer.model.api.Api getApi();

   //AssertTree getAssertTree();

   //BeforeTree getBeforeTree();

   AfterTree getAfterTree();

   ClassTypeTree getClassTypeTree();

   CallBackTree getCallBackTree();




}
