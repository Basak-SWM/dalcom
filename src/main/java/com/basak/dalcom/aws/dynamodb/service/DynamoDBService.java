package com.basak.dalcom.aws.dynamodb.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DynamoDBService {

    private final AmazonDynamoDB dynamoDB;
    private final String defaultTableName;
    private final String DEFAULT_ID_KEY = "id";

    protected Optional<Map<String, Object>> findById(String idValue) {
        return findById(DEFAULT_ID_KEY, idValue);
    }

    protected Optional<Map<String, Object>> findById(String idKey, String idValue) {
        return findById(defaultTableName, idKey, idValue);
    }

    protected Optional<Map<String, Object>> findById(String tableName, String idKey,
        String idValue) {
        GetItemRequest request = new GetItemRequest()
            .withTableName(tableName)
            .withKey(new HashMap<String, AttributeValue>() {{
                put(idKey, new AttributeValue(idValue));
            }});

        GetItemResult result = dynamoDB.getItem(request);
        if (result.getItem() == null) {
            return Optional.empty();
        } else {
            Map<String, Object> resultMap = processDynamoDBResult(result.getItem());
            return Optional.of(resultMap);
        }

    }

    protected Optional<Map<String, Object>> findById(String tableName, String idKey,
        Integer idValue) {
        return findById(tableName, idKey, idValue.toString());
    }

    protected Optional<Map<String, Object>> findById(String idKey, Integer idValue) {
        return findById(idKey, idValue.toString());
    }

    protected Optional<Map<String, Object>> findById(Integer idValue) {
        return findById(DEFAULT_ID_KEY, idValue.toString());
    }

    protected Optional<Map<String, Object>> findById(String tableName, String idKey, Long idValue) {
        return findById(tableName, idKey, idValue.toString());
    }

    protected Optional<Map<String, Object>> findById(String idKey, Long idValue) {
        return findById(defaultTableName, idKey, idValue.toString());
    }

    protected Optional<Map<String, Object>> findById(Long idValue) {
        return findById(defaultTableName, DEFAULT_ID_KEY, idValue.toString());
    }


    private Map<String, Object> processDynamoDBResult(Map<String, AttributeValue> dynamoDBItem) {
        Map<String, Object> resultMap = new HashMap<>();

        for (Map.Entry<String, AttributeValue> entry : dynamoDBItem.entrySet()) {
            String key = entry.getKey();
            AttributeValue attributeValue = entry.getValue();

            if (attributeValue.getM() != null) {
                resultMap.put(key, processDynamoDBResult(attributeValue.getM()));
            } else if (attributeValue.getL() != null) {
                List<Object> listResult = new ArrayList<>();
                for (AttributeValue av : attributeValue.getL()) {
                    if (av.getM() != null) {
                        listResult.add(processDynamoDBResult(av.getM()));
                    } else {
                        listResult.add(convertAttributeValue(av));
                    }
                }
                resultMap.put(key, listResult);
            } else {
                resultMap.put(key, convertAttributeValue(attributeValue));
            }
        }

        return resultMap;
    }

    private Object convertAttributeValue(AttributeValue value) {
        if (value.getS() != null) {
            return value.getS();
        } else if (value.getN() != null) {
            return Double.parseDouble(value.getN());
        } else if (value.getBOOL() != null) {
            System.out.println(">>>" + value.getBOOL());
            return value.getBOOL();
        } else if (value.getNULL() != null && value.getNULL()) {
            return null;
        } else {
            // Handle other types as needed (e.g., binary data)
            log.error("Failed to parse AttributeValue: {}", value);
            return null;
        }
    }

}
