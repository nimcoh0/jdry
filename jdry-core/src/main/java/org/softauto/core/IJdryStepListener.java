package org.softauto.core;

import org.testng.ITestContext;
import org.testng.ITestNGListener;

public interface IJdryStepListener extends ITestNGListener {

    public void onStepStart();


    public void onStepSuccess();


    public void onStepFailure();


    public void onStepSkipped();


    public void onStepFinish();
}
