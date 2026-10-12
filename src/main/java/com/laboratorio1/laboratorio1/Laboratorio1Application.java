package com.laboratorio1.laboratorio1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Laboratorio1Application {

    public static void main(String[] args) {
        SpringApplication.run(Laboratorio1Application.class, args);
        System.out.println("Compilacion completa. Probar en Postman con la coleccion en JSON");
    }
}
