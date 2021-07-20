package com.myorg;

import com.myorg.service.GenerateLoveLetterService;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;

public class AutoLoveLetterStack extends Stack {
    public AutoLoveLetterStack(final Construct scope, final String id) {
        this(scope, id, null);
        initStack();
    }

    public AutoLoveLetterStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        initStack();
    }

    private void initStack(){
        new GenerateLoveLetterService(this,"GenerateLoveLetterService");
    }
}
