package com.example.nolo.entities.item.purchasable;

import android.util.Log;

import com.example.nolo.entities.item.variant.IItemVariant;

import java.util.Objects;

/**
 * This class is for the selected Item with quantity.
 * E.g. Items in Cart.
 */
public class Purchasable implements IPurchasable {
    private IItemVariant itemVariant;
    private int quantity;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Purchasable(){}

    public Purchasable(IItemVariant itemVariant, int quantity){
        this.itemVariant = itemVariant;
        this.quantity = quantity;
    }

    @Override
    public IItemVariant getItemVariant() {
        return itemVariant;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void incrementQuantity(int increment){
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
        return quantity == that.quantity && Objects.equals(itemVariant, that.itemVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemVariant, quantity);
    }
}
