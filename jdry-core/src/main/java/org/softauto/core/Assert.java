package org.softauto.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.testng.IInvokedMethod;
import org.testng.ITestContext;

public class Assert  {
    ITestContext ctx;
    IInvokedMethod invokedMethod;
    static String _testId;
    static String _publish;

    public Assert(Object testId,String publish){
        if(testId instanceof String) {
            _testId = testId.toString();
        }
       _testId =  String.valueOf(testId);
       _publish = publish;
    }

    public Assert(){

    }

    public String getTestId() {
        return _testId;
    }

    public Object get_publish() {
        return _publish;
    }

    public static Assert setPublish(String publish) {
        return new Assert(_testId,publish);
    }

    public static Assert setTestId(Object testId) {
       return new Assert(testId,_publish);
    }

    public  boolean equals(Object expected, Object actual) {
        return EqualsBuilder.reflectionEquals(expected, actual);
    }

    public  ITestContext getContext() {
        return ctx;
    }

    public  void setContext(ITestContext context,IInvokedMethod method) {
        ctx = context;
        invokedMethod = method;
    }

    public  void AssertThat(Object result, Object expected, AssertType assertType){

    }

    public  void assertEquals(Object expected, Object actual) {
        //ctx.setAttribute(invokedMethod.getTestMethod().getMethodName(),actual);
        Resolver.getInstance().addVariable(_testId,_publish);
        org.junit.Assert.assertTrue(equals(expected, actual));
        //org.junit.Assert.assertEquals(expected,actual);
    }

    public  void assertNotEquals(Object expected, Object actual) {
        //ctx.setAttribute(invokedMethod.getTestMethod().getMethodName(),actual);
        Resolver.getInstance().addVariable(_testId,_publish);
        org.junit.Assert.assertFalse(equals(expected, actual));
        //org.junit.Assert.assertEquals(expected,actual);
    }

    public  void assertTrue(boolean condition){
        if (!condition) {
            org.junit.Assert.fail();
        }
    }

    public boolean isSuccesses(){
        return true;
    }
}
