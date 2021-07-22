package com.myorg.utils;

import java.util.Map;

public class InitializeDynamoDBData {
    public static Map<String,Object> initializeLoveLetter(String tableName){
        return Map.ofEntries(
                Map.entry("TableName", tableName),
                Map.entry("Item", Map.ofEntries(
                        Map.entry("itemId", Map.of("S", "0")),
                        Map.entry("data", Map.of("S", "data"))
                )),
                Map.entry("ConditionExpression", "attribute_not_exists(itemId)")
        );
    }
}
