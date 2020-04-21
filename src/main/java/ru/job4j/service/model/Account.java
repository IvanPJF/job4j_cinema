package ru.job4j.service.model;

public class Account {

    private Integer id;
    private String name;
    private Long phone;

    public Account() {
    }

    public Account(String name, Long phone) {
        this.name = name;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getPhone() {
        return phone;
    }
}
