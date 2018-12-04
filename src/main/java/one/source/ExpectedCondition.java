package one.source;

import io.appium.java_client.android.AndroidDriver;

import java.util.function.Function;

public interface ExpectedCondition<T> extends Function<AndroidDriver, T> {}
