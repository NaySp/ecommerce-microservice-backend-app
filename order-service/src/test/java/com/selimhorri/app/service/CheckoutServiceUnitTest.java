package com.selimhorri.app.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class CheckoutServiceUnitTest {
    
    @Test
    void testCalculateTotalPrice() {
        BigDecimal unitPrice = new BigDecimal("10.00");
        int quantity = 5;
        
        BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        
        assertEquals(new BigDecimal("50.00"), total, "Total should be unit price * quantity");
    }
    
    @Test
    void testApplyDiscountLogic() {
        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal discountPercent = new BigDecimal("10");
        
        BigDecimal discount = originalPrice.multiply(discountPercent).divide(BigDecimal.valueOf(100));
        BigDecimal finalPrice = originalPrice.subtract(discount);
        
        assertEquals(new BigDecimal("90.00"), finalPrice, "Final price should apply 10% discount");
    }
    
    @Test
    void testMinimumOrderValidation() {
        BigDecimal orderTotal = new BigDecimal("5.00");
        BigDecimal minimumOrder = new BigDecimal("10.00");
        
        boolean isValid = orderTotal.compareTo(minimumOrder) >= 0;
        
        assertFalse(isValid, "Order below minimum should be invalid");
    }
}
