package com.example.nolo.entities.item.purchasable;

import android.util.Log;

import com.example.nolo.entities.item.variant.IItemVariant;

import java.util.Objects;

public class Purchasable implements IPurchasable {
    private IItemVariant itemVariant;
    private Integer quantity;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Purchasable(){}

    public Purchasable(IItemVariant itemVariant, Integer quantity){
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

    @Override
    public void incrementQuantity(Integer increment){
        if (increment < 0) {
            Log.i("Err", "Invalid increment");
            return;
        }

        quantity += increment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchasable that = (Purchasable) o;
        return Objects.equals(itemVariant, that.itemVariant) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemVariant, quantity);
    }
}
