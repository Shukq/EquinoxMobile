package com.quinox.mobile;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;

public class EquinoxGradleTestRunner extends RobolectricTestRunner {
    public static final int DEFAULT_SDK = 21;

    public EquinoxGradleTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
}
