package com.techlambdas.delearmanagementapp.utils;

import java.time.LocalDate;
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

    public static String calculateFinancialYear() {
        String financialYear=null;
        LocalDate currentDate = LocalDate.now();
        if(currentDate.getMonthValue() < 4)
            financialYear = currentDate.getYear()-1+"-"+currentDate.getYear();
        else
            financialYear = currentDate.getYear()+"-"+currentDate.getYear()+1;

        return financialYear;

    }

}
