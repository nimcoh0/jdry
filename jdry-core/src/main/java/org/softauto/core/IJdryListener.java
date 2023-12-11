package org.softauto.core;

import org.testng.ITestNGListener;

public interface IJdryListener extends ITestNGListener {

    public void onStepStart();


    public void onStepSuccess();


    public void onStepFailure();


    public void onStepSkipped();


    public void onStepFinish();




}
