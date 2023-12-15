package org.softauto.testng;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.*;
import org.softauto.listener.ListenerObserver;
import org.softauto.system.SystemState;
import org.testng.*;

public class JdryTestListener implements ITestListener, IInvokedMethodListener, IJdryListener {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JdryTestListener.class);

    private static final Marker TESTER = MarkerManager.getMarker("TESTER");

    @Override
    public void onTestStart(ITestResult result) {
        try {
            System.setProperty("logFilename", result.getName());

            if(SystemState.getInstance().startTest(result.getName(),TestContext.getScenario().getId())){
                logger.debug(TESTER,"successfully start test " + result.getName());
                } else {
                    logger.error(TESTER,"fail start test "+result.getName());
                }
        }catch (Exception e){
            logger.error(TESTER,"fail onTestStart "+result.getName(),e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TestContext.getScenario().setState(ScenarioLifeCycle.START.PASS.name());
        logger.debug(TESTER,result.getName()+" end successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        TestContext.getScenario().setState(ScenarioLifeCycle.FAIL.name());
        logger.debug(TESTER,result.getName()+" fail",result.getThrowable());
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        logger.debug(result.getName()+" skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStepStart() {
        logger.info(TESTER,"step " + TestContext.get("step_name") + " start");
    }

    @Override
    public void onStepSuccess() {
        logger.info(TESTER,"step " + TestContext.get("step_name") + " finish successfully");
    }

    @Override
    public void onStepFailure() {
        logger.info(TESTER,"step " + TestContext.get("step_name") + " fail");
    }

    @Override
    public void onStepSkipped() {
        logger.info(TESTER,"step " + TestContext.get("step_name") + " skip");
    }

    @Override
    public void onStepFinish() {
        logger.info(TESTER,"step " + TestContext.get("step_name") + " finish ");
    }

    @Override
    public void onStart(ITestContext context) {

    }



    @Override
    public void onFinish(ITestContext context) {
        try {
            TestContext.getScenario().setState(ScenarioLifeCycle.STOP.name());;
            if(SystemState.getInstance().endTest(context.getName(),TestContext.getScenario().getId())){
                    logger.debug(TESTER,"successfully end test ");
                } else {
                    logger.error(TESTER,"fail end test" );
                }
            ListenerObserver.getInstance().reset();
        }catch (Exception e){
            logger.error(TESTER,"fail onFinish ",e);
        }
        logger.info(TESTER, "roll test");
    }


    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult arg0) {

    }
}
