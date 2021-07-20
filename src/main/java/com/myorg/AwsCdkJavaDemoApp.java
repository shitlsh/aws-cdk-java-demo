package com.myorg;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.StackProps;

public class AwsCdkJavaDemoApp {
    public static void main(final String[] args) {
        App app = new App();

        new AutoLoveLetterStack(app, "AwsCdkJavaDemoStack");

        app.synth();
    }
}
