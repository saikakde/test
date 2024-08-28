package com.bajaj;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SpringBootApplication
public class BajajTestApplication {
	public static void main(String[] args) {
	if (args.length != 2) {
        System.out.println("Usage: java -jar jsonprocessor-1.0-SNAPSHOT.jar 240341220162 D:/test/TestRepo/BajajTest/src/main/java/example.json");
        return;
    }

    String prnNumber = args[0];
    String filePath = args[1];

    try {
        FileReader reader = new FileReader(filePath);
        JsonElement jsonElement = JsonParser.parseReader(reader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String destinationValue = findDestinationValue(jsonObject);

        if (destinationValue == null) {
            System.out.println("Key 'destination' not found.");
            return;
        }

        String randomString = generateRandomString(8);

        String md5Hash = generateMd5Hash(prnNumber + destinationValue + randomString);

        System.out.println(md5Hash + ";" + randomString);

    } catch (IOException e) {
        System.err.println("Error reading JSON file: " + e.getMessage());
    }
}

private static String findDestinationValue(JsonObject jsonObject) {
    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
        String key = entry.getKey();
        JsonElement value = entry.getValue();

        if ("destination".equals(key)) {
            return value.getAsString();
        }

        if (value.isJsonObject()) {
            String found = findDestinationValue(value.getAsJsonObject());
            if (found != null) {
                return found;
            }
        }
    }
    return null;
}

private static String generateRandomString(int length) {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new Random();
    StringBuilder result = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
        int index = random.nextInt(characters.length());
        result.append(characters.charAt(index));
    }
    return result.toString();
}

private static String generateMd5Hash(String input) {
    return DigestUtils.md5Hex(input);
}
}
