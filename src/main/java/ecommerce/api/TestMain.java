package ecommerce.api;

import ecommerce.api.entity.product.Product;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestMain {
    public static void main(String[] args) throws ParseException {
//        String dateTimeString = "2022-07-28T13:14:15.123+01:00";
//        String str = "2025-01-04T16:22:48.082Z";
//        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//        Date date = isoFormat.parse(str);
//        System.out.println(date);
        List<Product> products = new ArrayList<>(1);
        Product prod = new Product();
        prod.setId(UUID.fromString("f5b1f1b1-1b1b-1b1b-1b1b-1b1b1b1b1b1b"));
        prod.setPrice(BigDecimal.valueOf(100));
        prod.setDiscountPercent(BigDecimal.valueOf(10));
        products.add(prod);  // Corrected line
        Map<UUID, Integer> productQuantities = new HashMap<>(1);
        productQuantities.put(UUID.fromString("f5b1f1b1-1b1b-1b1b-1b1b-1b1b1b1b1b1b"), 1);

        BigDecimal amount = products.stream()
                .map(product -> product.getPrice()
                        .multiply(BigDecimal.valueOf(productQuantities.get(product.getId())))
                        .multiply(BigDecimal.valueOf(1).subtract(product.getDiscountPercent().divide(BigDecimal.valueOf(100))))
                ).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = amount.multiply(new BigDecimal("10.00").divide(BigDecimal.valueOf(100)));
        amount = amount.subtract(discount);
        System.out.println(amount);
    }
}
