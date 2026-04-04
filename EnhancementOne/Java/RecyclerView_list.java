package com.cs360.inventoryappdelarosa;

public class RecyclerView_list {
    // variables needed for item id,name, and quantity
    private Integer id;
    private String name;
    private Integer quantity;

    // constructor list
    public RecyclerView_list(Integer id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
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

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
