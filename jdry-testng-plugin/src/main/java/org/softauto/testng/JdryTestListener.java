package org.softauto.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.softauto.core.Assert;
import org.softauto.core.Context;
import org.softauto.core.TestContext;
import org.softauto.core.TestLifeCycle;
import org.softauto.listener.ListenerObserver;
import org.softauto.tester.SystemState;
import org.testng.*;

public class JdryTestListener implements ITestListener, IInvokedMethodListener {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JdryTestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        try {
            Context.setTestState(TestLifeCycle.START);
            if(SystemState.getInstance().startTest(result.getName())){
                logger.debug("successfully start test " + result.getName());
                } else {
                    logger.error("fail start test "+result.getName());
                }

        }catch (Exception e){
            logger.error("fail onTestStart "+result.getName(),e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.debug(result.getName()+" end successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.debug(result.getName()+" fail",result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.debug(result.getName()+" skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        TestContext.put("test_name",context.getName());
    }



    @Override
    public void onFinish(ITestContext context) {
        try {
            Context.setTestState(TestLifeCycle.STOP);
            if(SystemState.getInstance().endTest(context.getName())){
                    logger.debug("successfully end test ");
                } else {
                    logger.error("fail end test" );
                }
            ListenerObserver.getInstance().reset();
            //ListenerServerProviderImpl.getInstance().shutdown();
            //if(SystemState.getInstance().shutdown()){
                  // logger.debug("successfully shutdown ");
               // } else {
                   // logger.error("fail shutdown ");
               // }

            final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            final Configuration config = ctx.getConfiguration();
            config.getRootLogger().removeAppender("console");
            ctx.updateLoggers();
        }catch (Exception e){
            logger.error("fail onFinish ",e);
        }
    }


    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

       // Assert.setContext(testResult.getTestContext(),method);
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

    }
}
