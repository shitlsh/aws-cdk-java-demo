package com.myorg.service;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.events.CronOptions;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.Schedule;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Permission;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.sns.Topic;

import java.util.HashMap;

public class GenerateLoveLetterService extends Construct {

    public GenerateLoveLetterService(Construct scope, String id) {
        super(scope, id);

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

        Function generateLoveLetterHandler = Function.Builder.create(this, "GenerateLoveLetterHandler")
                //TODO need implement a function
                .runtime(Runtime.NODEJS_10_X)
                .code(Code.fromAsset("resources"))
                .handler("widgets.main")
                .build();

        loveWordsTable.grantReadData(generateLoveLetterHandler);

        Topic loveLetterTopic = Topic.Builder.create(this,"LoveLetterTopic").build();

        loveLetterTopic.grantPublish(generateLoveLetterHandler);

        Rule scheduleRule = Rule.Builder.create(this,"ScheduleGenerateEvent")
                .description("Trigger Lambda Function To Generate An Love Letter Every Day")
                .schedule(Schedule.expression("cron(0 1 * * ? *)"))
                .build();

        generateLoveLetterHandler.addPermission("", Permission.builder().sourceArn(scheduleRule.getRuleArn()).build());
    }
}
