package com.bevinisaditch.theinebriator;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This class runs all the android junit tests
 * 
 * @author michael
 *
 */
public class AllTests extends TestSuite {

	public static Test suite() {
        return new TestSuiteBuilder(AllTests.class)
        .includeAllPackagesUnderHere()
        .build();
    }
}
