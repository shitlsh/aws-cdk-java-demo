package com.myorg.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class InitializeDynamoDBData {
    public static JSONObject initializeSalutations(String tableName) {
        JSONArray putRequests = new JSONArray();
        putRequests.put(new JSONObject().put("PutRequest", Map.ofEntries(
                Map.entry("Item", Map.ofEntries(
                        Map.entry("itemId", Map.of("S", "1")),
                        Map.entry("itemType", Map.of("S", "salutation")),
                        Map.entry("content", Map.of("S", "sweetheart"))
                ))
        )));
        return new JSONObject().put(tableName, putRequests);
    }
}
