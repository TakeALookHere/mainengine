package com.miskevich.engine;

import com.miskevich.engine.service.AddService;

public class Starter {

    public static void main(String[] args) {
        AddService addService = new AddService();
        while (true) {
            addService.run();
        }
    }
}
