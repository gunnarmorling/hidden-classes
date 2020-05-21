package com.example;

import java.time.LocalDate;

public class Customer {

    private String name;
    private LocalDate birthday;
    private String address;

    public Customer(String name, LocalDate birthday, String address) {
        this.name = name;
        this.birthday = birthday;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }
}
