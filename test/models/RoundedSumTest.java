package models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoundedSumTest {

    @Test
    public void constructor_should_round_up_input_to_the_upper_hundred() {
        assertEquals(100L, new RoundedSum(0.1).roundedAmount);
        assertEquals(100L, new RoundedSum((100)).roundedAmount);
        assertEquals(100L, new RoundedSum((99)).roundedAmount);
        assertEquals(0L, new RoundedSum((0)).roundedAmount);
        assertEquals(1000L, new RoundedSum((1000)).roundedAmount);
        assertEquals(10100L, new RoundedSum((10001)).roundedAmount);
    }
}
