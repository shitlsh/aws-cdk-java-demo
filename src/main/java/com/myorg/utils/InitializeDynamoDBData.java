package com.myorg.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

public class InitializeDynamoDBData {

    private static final String[] salutationsMale = {"sir", "sweetheart", "darling", "swain", "suitor", "sire", "gentleman", "monsieur", "baby"};
    private static final String[] salutationsFemale = {"dame", "sweetheart", "delicacy", "darling", "angel", "moppet", "missy", "lass", "baby"};

    public static JSONObject initializeSalutations(String tableName) {
        JSONArray putRequests = new JSONArray();
        Arrays.stream(salutationsMale).forEach(salutation ->
                putRequests.put(new JSONObject().put("PutRequest", Map.ofEntries(
                        Map.entry("Item", Map.ofEntries(
                                Map.entry("itemId", Map.of("S", "1")),
                                Map.entry("itemType", Map.of("S", "salutation")),
                                Map.entry("content", Map.of("S", salutation))
                        ))
                )))
        );
        Arrays.stream(salutationsFemale).forEach(salutation ->
                putRequests.put(new JSONObject().put("PutRequest", Map.ofEntries(
                        Map.entry("Item", Map.ofEntries(
                                Map.entry("itemId", Map.of("S", "1")),
                                Map.entry("itemType", Map.of("S", "salutation")),
                                Map.entry("content", Map.of("S", salutation))
                        ))
                )))
        );
        return new JSONObject().put(tableName, putRequests);
    }
}
