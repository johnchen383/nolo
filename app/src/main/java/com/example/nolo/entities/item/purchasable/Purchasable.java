package com.example.nolo.entities.item.purchasable;

import android.util.Log;

import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;

import java.util.Objects;

/**
 * This class is for the selected Item with quantity.
 * E.g. Items in Cart.
 */
public class Purchasable implements IPurchasable {
    /**
     * Cannot use IItemVariant (interface),
     * the reason is when the Firebase auto converts the data into
     * the object, it is unable to deserialize the object.
     * It is because the interface does not have 0-argument constructor.
     * To have the Firebase auto converts the data into the object,
     * our team decided to use StoreVariant and Specs.
     * So it is a reasonable excuse to violate the SOLID principle.
     */
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
    public void addToQuantity(int quantity){
        if (quantity < 0) {
            Log.i("Err", "Invalid increment");
            return;
        }

        this.quantity += quantity;
    }

    @Override
    public void incrementOrDecrementQuantity(boolean isIncrement) {
        if (isIncrement) {
            this.quantity++;
        } else {
            if (this.quantity > 1) {
                this.quantity--;
            }
        }
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
