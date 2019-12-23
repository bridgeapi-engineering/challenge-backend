package models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Round a double to the upper hundred
 */
public class RoundedSum {

    @JsonProperty("rounded_amount")
    long roundedAmount;

    public RoundedSum(double amount) {
        roundedAmount = (((long) Math.ceil(amount) + 99) / 100) * 100;
    }
}
