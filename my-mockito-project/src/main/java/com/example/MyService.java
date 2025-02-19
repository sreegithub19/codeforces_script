package com.example;

public class MyService {
    private final MyRepository repository;

    public MyService(MyRepository repository) {
        this.repository = repository;
    }

    public String getDataById(int id) {
        return repository.findById(id);
    }
}
