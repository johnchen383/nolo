package com.example.nolo.entities.item;

public class Purchase implements IPurchase {
    private IItemVariant itemVariant;
    private Integer quantity;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Purchase(){}

    public Purchase(IItemVariant itemVariant, Integer quantity){
        this.itemVariant = itemVariant;
        this.quantity = quantity;
    }

    @Override
    public IItemVariant getItemVariant() {
        return itemVariant;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }
}
