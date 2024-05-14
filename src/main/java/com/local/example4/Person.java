package com.local.example4;

public class Person {
    private Integer id;
    private String name;

    public Integer getId(){
        return id;
    }
    public void setId(Integer value){
        this.id = value;
    }
    public String getName(){
        return name;
    }
    public void setName(String value){
        this.name = value;
    }

    public Person(Integer _id, String _name){
        this.id = _id;
        this.name = _name;
    }
}
