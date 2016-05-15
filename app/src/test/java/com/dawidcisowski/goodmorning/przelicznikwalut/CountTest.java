package com.dawidcisowski.goodmorning.przelicznikwalut;

import com.dawidcisowski.przelicznikwalut.Count;

import org.junit.Test;

import static org.junit.Assert.*;


public class CountTest {
    @Test
    public void convertTest() {

        assertEquals("2,67", Count.convert(2, 3, 4));
    }

    @Test
    public void convertValueZeroTest(){
        assertEquals("0",Count.convert(3,5,0));
    }
    @Test
    public void convertRateZero(){
        assertEquals("0",Count.convert(0,1,3));
    }
}