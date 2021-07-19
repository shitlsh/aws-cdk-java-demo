package com.myorg.service;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.customresources.AwsCustomResource;
import software.amazon.awscdk.customresources.AwsCustomResourcePolicy;
import software.amazon.awscdk.customresources.AwsSdkCall;
import software.amazon.awscdk.customresources.PhysicalResourceId;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.Schedule;
import software.amazon.awscdk.services.events.targets.LambdaFunction;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Permission;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.sns.Topic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateLoveLetterService extends Construct {



    public GenerateLoveLetterService(Construct scope, String id) {
        super(scope, id);

        // Create Table: love-words-table
        TableProps tableProps;
        Attribute partitionKey = Attribute.builder()
                .name("itemId")
                .type(AttributeType.STRING)
                .build();
        tableProps = TableProps.builder()
                .tableName("love-words-table")
                .partitionKey(partitionKey)
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
        Table loveWordsTable = new Table(this, "LoveWordsTable", tableProps);

        // Initialize love-words
        AwsSdkCall initializeData = AwsSdkCall.builder()
                .service("DynamoDB")
                .action("putItem")
                .physicalResourceId(PhysicalResourceId.of(loveWordsTable.getTableName() + "_initialization"))
                .parameters(Map.ofEntries(
                        Map.entry("TableName", loveWordsTable.getTableName()),
                        Map.entry("Item", Map.ofEntries(
                                Map.entry("itemId", Map.of("S", "0")),
                                Map.entry("data", Map.of("S", "data"))
                        )),
                        Map.entry("ConditionExpression", "attribute_not_exists(itemId)")
                ))
                .build();

        AwsCustomResource tableInitializationResource = AwsCustomResource.Builder.create(this, "TableInitializationResource")
                .policy(AwsCustomResourcePolicy.fromStatements(List.of(
                        PolicyStatement.Builder.create()
                                .effect(Effect.ALLOW)
                                .actions(List.of("dynamodb:PutItem"))
                                .resources(List.of(loveWordsTable.getTableArn()))
                                .build()
                )))
                .onCreate(initializeData)
                .onUpdate(initializeData)
                .build();
        tableInitializationResource.getNode().addDependency(loveWordsTable);

        // Create a topic to publish generated love letter
        Topic loveLetterTopic = Topic.Builder.create(this,"LoveLetterTopic").build();

        // Create a lambda function generate love letter by Table
        Function generateLoveLetterHandler = Function.Builder.create(this, "GenerateLoveLetterHandler")
                //TODO need implement a function
                .runtime(Runtime.NODEJS_10_X)
                .code(Code.fromAsset("resources"))
                .handler("widgets.main")
                .environment(new HashMap<>() {{
                    put("TOPIC_ARN", loveLetterTopic.getTopicArn());
                }})
                .build();

        // Grant permission to lambda
        loveWordsTable.grantReadData(generateLoveLetterHandler);
        loveLetterTopic.grantPublish(generateLoveLetterHandler);

        // Create a daily schedule rule to trigger lambda
        Rule scheduleRule = Rule.Builder.create(this,"ScheduleGenerateEvent")
                .description("Trigger Lambda Function To Generate An Love Letter Every Day")
                .schedule(Schedule.expression("cron(0 1 * * ? *)"))
                .build();

        generateLoveLetterHandler.addPermission("", Permission.builder().sourceArn(scheduleRule.getRuleArn()).build());
        scheduleRule.addTarget(new LambdaFunction(generateLoveLetterHandler));
    }
}
