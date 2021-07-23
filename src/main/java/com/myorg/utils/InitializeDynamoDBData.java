package com.myorg.utils;

import java.util.Map;

public class InitializeDynamoDBData {
    public static Map<String,Object> initializeLoveLetter(String tableName){
        return Map.ofEntries(
                Map.entry(tableName, Map.ofEntries(
                        Map.entry("PutRequest",Map.ofEntries(
                                Map.entry("Item",Map.ofEntries(
                                        Map.entry("itemId",Map.of("S","1")),
                                        Map.entry("itemType",Map.of("S","")),
                                        Map.entry("content",Map.of("S",""))
                                ))
                        ))
                ))
        );
    }
}
