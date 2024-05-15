package com.local.example6;

public class Inventory {
    private Integer id;
    private String name;
    private Integer quantity;

    public Integer getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public Integer getQuantity(){
        return this.quantity;
    }
    public void setId(Integer value){
        this.id = value;
    }
    public void setName(String value){
        this.name = value;
    }
    public void setQuantity(Integer value){
        this.quantity = value;
    }
    public Inventory(){

    }
    public Inventory(Integer id, String name, Integer quantity){
            this.id = id;
            this.name = name;
            this.quantity = quantity;
    }
    @Override
    public String toString(){
        return "Inventory [Id=" + this.id + ", Name=" + this.name + ", Quantity=" + this .quantity;
    }

}
