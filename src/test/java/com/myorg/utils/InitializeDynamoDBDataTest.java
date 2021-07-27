package com.myorg.utils;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class InitializeDynamoDBDataTest {

    @Test
    public void should_return_salutations_json_object(){
        JSONObject parameters = InitializeDynamoDBData.initializeSalutations("test_table");
        System.out.println(parameters.toString());
    }

}