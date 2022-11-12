package org.softauto.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.testng.IInvokedMethod;
import org.testng.ITestContext;

public class Assert extends org.junit.Assert {
    static ITestContext ctx;
    static IInvokedMethod invokedMethod;


    public static boolean equals(Object expected, Object actual) {
        return EqualsBuilder.reflectionEquals(expected, actual);
    }

    public static ITestContext getContext() {
        return ctx;
    }

    public static void setContext(ITestContext context,IInvokedMethod method) {
        ctx = context;
        invokedMethod = method;
    }

    public static void AssertThat(Object result, Object expected, AssertType assertType){

    }

    public static void assertEquals(Object expected, Object actual) {
        ctx.setAttribute(invokedMethod.getTestMethod().getMethodName(),actual);
        org.junit.Assert.assertTrue(equals(expected, actual));
        //org.junit.Assert.assertEquals(expected,actual);
    }

    public static void assertNotEquals(Object expected, Object actual) {
        ctx.setAttribute(invokedMethod.getTestMethod().getMethodName(),actual);
        org.junit.Assert.assertFalse(equals(expected, actual));
        //org.junit.Assert.assertEquals(expected,actual);
    }

    public static void assertTrue(boolean condition){
        if (!condition) {
            fail();
        }
    }

    public boolean isSuccesses(){
        return true;
    }
}
