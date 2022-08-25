package com.example.nolo.entities.item.purchasable;

import android.util.Log;

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
    private ItemVariant itemVariant;
    private int quantity;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Purchasable() {}

    public Purchasable(ItemVariant itemVariant, int quantity) {
        this.itemVariant = itemVariant;
        this.quantity = quantity;
    }

    @Override
    public ItemVariant getItemVariant() {
        return itemVariant;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    /**
     * Add quantity (can be more than 1) to the purchasable item
     *
     * @param quantity Quantity
     */
    @Override
    public void addToQuantity(int quantity) {
        if (quantity < 0) {
            Log.e("Purchasable", "Invalid increment");
            return;
        }

        this.quantity += quantity;
    }

    /**
     * Increment or decrement the quantity of the purchasable item
     *
     * @param isIncrement Specify whether is it incrementing or decrementing
     */
    @Override
    public void incrementOrDecrementQuantity(boolean isIncrement) {
        if (isIncrement) {
            this.quantity++;
        } else {
            this.quantity--;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Purchasable that = (Purchasable) o;
        return Objects.equals(itemVariant, that.itemVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemVariant, quantity);
    }
}
