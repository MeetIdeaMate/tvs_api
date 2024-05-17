package com.techlambdas.delearmanagementapp.utils;

import java.util.UUID;

public class RandomIdGenerator {

    public static String getRandomId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

  /*  public static String getGeneratedId(String idPrefix) {
        String id = idPrefix + getRandomId();
        System.out.println("Generated ID: " + id);
        return id;
    }*/



}
