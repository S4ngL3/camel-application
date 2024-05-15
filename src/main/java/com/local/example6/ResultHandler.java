package com.local.example6;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ResultHandler {
    public void printResult(List list){
        for (Object o : list) {
            System.out.println(o);
        }
    }

    public void printMessageResult(String message){
        System.out.println(message);
    }
}
