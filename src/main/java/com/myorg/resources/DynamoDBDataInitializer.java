package com.myorg.resources;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.customresources.AwsCustomResource;
import software.amazon.awscdk.customresources.AwsCustomResourceProps;
import software.constructs.Construct;

public class DynamoDBDataInitializer extends AwsCustomResource {
    public DynamoDBDataInitializer(@NotNull Construct scope, @NotNull String id, @NotNull AwsCustomResourceProps props) {
        super(scope, id, props);
    }
}
