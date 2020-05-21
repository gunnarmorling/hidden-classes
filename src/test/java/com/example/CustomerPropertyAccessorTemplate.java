package com.example;

import dev.morling.demos.hiddenclass.PropertyAcessor;

/**
 * Used as template for ASMifier.
 */
public class CustomerPropertyAccessorTemplate implements PropertyAcessor<Customer> {

    @Override
    public Object getValue(Customer instance, String property) {
        if (property.equals("name")) {
            return instance.getName();
        }

        if (property.equals("birthday")) {
            return instance.getBirthday();
        }

        if (property.equals("address")) {
            return instance.getAddress();
        }

        throw new IllegalArgumentException("Unknown property: " + property);
    }
}
