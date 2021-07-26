package com.myorg.resources;

import com.myorg.utils.InitializeDynamoDBData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.core.Resource;
import software.amazon.awscdk.core.ResourceProps;
import software.amazon.awscdk.customresources.AwsCustomResource;
import software.amazon.awscdk.customresources.AwsCustomResourcePolicy;
import software.amazon.awscdk.customresources.AwsSdkCall;
import software.amazon.awscdk.customresources.PhysicalResourceId;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.constructs.Construct;

import java.util.List;

public class DynamoDBDataInitializer extends Resource {
    public DynamoDBDataInitializer(@NotNull Construct scope, @NotNull String id, @NotNull Table table,@NotNull Object TableParameters) {
        super(scope, id);
        AwsSdkCall initializeData = AwsSdkCall.builder()
                .service("DynamoDB")
                .action("BatchWriteItem")
                .physicalResourceId(PhysicalResourceId.of(table.getTableName() + "_initialization"))
                .parameters(TableParameters)
                .build();

        AwsCustomResource tableInitializationResource = AwsCustomResource.Builder.create(this, "TableInitializationResource")
                .policy(AwsCustomResourcePolicy.fromStatements(List.of(
                        PolicyStatement.Builder.create()
                                .effect(Effect.ALLOW)
                                .actions(List.of("dynamodb:BatchWriteItem"))
                                .resources(List.of(table.getTableArn()))
                                .build()
                )))
                .onCreate(initializeData)
                .onUpdate(initializeData)
                .build();
    }
}
