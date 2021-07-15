package com.myorg;

import java.util.HashMap;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.Bucket;

public class WidgetService extends Construct {

    @SuppressWarnings("serial")
    public WidgetService(Construct scope, String id) {
        super(scope, id);

        Bucket bucket = new Bucket(this, "WidgetStore");

        Function handler = Function.Builder.create(this, "WidgetHandler")
                .runtime(Runtime.NODEJS_10_X)
                .code(Code.fromAsset("resources"))
                .handler("widgets.main")
                .environment(new HashMap<String, String>() {{
                    put("BUCKET", bucket.getBucketName());
                }}).build();

        bucket.grantReadWrite(handler);

        RestApi api = RestApi.Builder.create(this, "Widgets-API")
                .restApiName("Widget Service").description("This service services widgets.")
                .build();

        LambdaIntegration getWidgetsIntegration = LambdaIntegration.Builder.create(handler)
                .requestTemplates(new HashMap<String, String>() {{
                    put("application/json", "{ \"statusCode\": \"200\" }");
                }}).build();

        api.getRoot().addMethod("GET", getWidgetsIntegration);

        Resource widget = api.getRoot().addResource("{id}");

        // Add new widget to bucket with: POST /{id}
        LambdaIntegration postWidgetIntegration = new LambdaIntegration(handler);

        // Get a specific widget from bucket with: GET /{id}
        LambdaIntegration getWidgetIntegration = new LambdaIntegration(handler);

        // Remove a specific widget from the bucket with: DELETE /{id}
        LambdaIntegration deleteWidgetIntegration = new LambdaIntegration(handler);

        widget.addMethod("POST", postWidgetIntegration);     // POST /{id}
        widget.addMethod("GET", getWidgetIntegration);       // GET /{id}
        widget.addMethod("DELETE", deleteWidgetIntegration); // DELETE /{id}
    }
}