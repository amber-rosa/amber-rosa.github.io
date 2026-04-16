/*
* This is the model for the recyclerview list items
* Each item will have an integer id, String name, and integer quantity.
* The constructor list will hold id, name and quantity, with getters and setters to access data needed
* during CRUD operations
* Assignment: CS499 Computer Science Capstone
 * Student: Amber De La Rosa
 * Final edit: 3/26/2026 */

package com.cs360.inventoryappdelarosa;
public class RecyclerView_list {
    // variables needed for item id,name, and quantity
    private Integer id;
    private String name;
    private Integer quantity;

    // constructor list to initialize list objects throughout the program
    public RecyclerView_list(Integer id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    // retrieve and read item id with getId() and update/modify item id with setId()
    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }
    // retrieve and read item name with getName() and update/modify item name with setName()

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    // retrieve and read item quantity with getQuantity() and update/modify item quantity with setQuantity()
    public Integer getQuantity() {

        return quantity;
    }

    public void setQuantity(Integer quantity) {

        this.quantity = quantity;
    }
}
