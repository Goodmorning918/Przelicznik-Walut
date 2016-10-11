package com.dawidcisowski.goodmorning.przelicznikwalut;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.FlakyTest;
import android.test.suitebuilder.annotation.SmallTest;


public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    @SmallTest
    public void insertCurrencyTest() {
        BaseAdapter baseAdapter = new BaseAdapter(getContext());
        baseAdapter.open();
        assertTrue((baseAdapter.insertCurrency("Zloty", 1, "PLN", 3.14))!=-1);
    }
    @FlakyTest
    public void insertNullCurrencyTest(){
        BaseAdapter baseAdapter = new BaseAdapter(getContext());
        baseAdapter.open();
        assertTrue((baseAdapter.insertCurrency(null,0,null,0))==0);
    }
}