package org.softauto.testng;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.*;
import org.softauto.listener.ListenerObserver;
import org.softauto.system.SystemState;
import org.testng.*;

import java.io.PrintWriter;
import java.io.StringWriter;

public class JdryTestListener implements ITestListener, IInvokedMethodListener, IJdryListener {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JdryTestListener.class);

    private static final Marker TESTER = MarkerManager.getMarker("TESTER");




    @Override
    public void onTestStart(ITestResult result) {
        try {

            System.setProperty("logFilename", result.getName());
            TestContext.put("step_name",result.getName());
            //TestContext.getScenario().setState(ScenarioLifeCycle.START.name());;
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
        //TestContext.put("step_name",context.getName());
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

            //ListenerServerProviderImpl.getInstance().shutdown();
            //if(SystemState.getInstance().shutdown()){
                  // logger.debug("successfully shutdown ");
               // } else {
                   // logger.error("fail shutdown ");
               // }

            //final org.apache.logging.log4j.spi.LoggerContext ctx = (org.apache.logging.log4j.spi.LoggerContext) LogManager.getContext(false);
            //final Configuration config = ctx.getConfiguration();
            //config.getRootLogger().removeAppender("console");
            //ctx.updateLoggers();

        }catch (Exception e){
            logger.error(TESTER,"fail onFinish ",e);
        }
        logger.info(TESTER, "roll test");
    }


    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        //ConstructorOrMethod constructorOrMethod = method.getTestMethod().getConstructorOrMethod();
        //Object[] p = testResult.getParameters();
        Object i = testResult.getInstance();
       // Assert.setContext(testResult.getTestContext(),method);
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult arg0) {


        //ConstructorOrMethod constructorOrMethod = testResult.getMethod().getConstructorOrMethod();
       // Object[] p = testResult.getParameters();
        //Object i = testResult.getInstance();
        if(TestContext.getScenario().getError().size() > 0){
            try {
                ITestContext tc = Reporter.getCurrentTestResult().getTestContext();
                arg0.setStatus(ITestResult.FAILURE);
                String json = new ObjectMapper().writeValueAsString(TestContext.getScenario().getError().get(0));
                JsonNode node = new ObjectMapper().readTree(json);
                //Throwable ex = new ObjectMapper().readValue(json,Throwable.class);
                //Throwable ex = (Throwable) TestContext.getScenario().getError().get(0);
                arg0.setThrowable(new Exception(node.get(0).get("message").asText()));
               // tc.getFailedTests().addResult(arg0, Reporter.getCurrentTestResult().getMethod());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (arg0.getMethod().isTest()) {
            //Change Failed to Skipped based on exception text
            if (arg0.getStatus() == ITestResult.FAILURE) {
                if (arg0.getThrowable() != null) {
                    if (arg0.getThrowable().getStackTrace() != null) {
                        StringWriter sw = new StringWriter();
                        arg0.getThrowable().printStackTrace(new PrintWriter(sw));
                        //if (sw.toString().contains("visible")) {
                            ITestContext tc = Reporter.getCurrentTestResult().getTestContext();
                           // tc.getFailedTests().addResult(arg0, Reporter.getCurrentTestResult().getMethod());
                            //tc.getFailedTests().getAllMethods().remove(Reporter.getCurrentTestResult().getMethod());
                            //Reporter.getCurrentTestResult().setStatus(ITestResult.SKIP);
                            //tc.getSkippedTests().addResult(arg0, Reporter.getCurrentTestResult().getMethod());
                        //}
                    }
                }
            }
            if (TestContext.getStepState().equals(StepLifeCycle.SKIP) ) {
                arg0.setStatus(ITestResult.FAILURE);
                ITestContext tc = Reporter.getCurrentTestResult().getTestContext();
                //tc.getFailedTests().addResult(arg0, Reporter.getCurrentTestResult().getMethod());
                //tc.getSkippedTests().addResult(arg0, Reporter.getCurrentTestResult().getMethod());
            }
        }

    }
}
