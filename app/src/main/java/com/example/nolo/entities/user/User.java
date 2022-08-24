package com.example.nolo.entities.user;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link #userAuthUid} {@link #email} will not be in the Firestore
 */
public class User implements IUser {
    public static final int MAX_VIEWED = 5;
    private String userAuthUid, email;
    private List<ItemVariant> viewHistory = new ArrayList<>();
    private List<Purchasable> cart = new ArrayList<>();

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public User() {}

    public User(List<ItemVariant> viewHistory, List<Purchasable> cart) {
        this.viewHistory = viewHistory;
        this.cart = cart;
    }

    @Override
    @Exclude
    public String getUserAuthUid() {
        return userAuthUid;
    }

    @Override
    public void setUserAuthUid(String userAuthUid) {
        this.userAuthUid = userAuthUid;
    }

    @Override
    @Exclude
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public List<ItemVariant> getViewHistory() {
        return viewHistory;
    }

    @Override
    public void addViewHistory(IItemVariant item) {
        viewHistory.removeIf(viewedItem -> viewedItem.getItemId().equals(item.getItemId()));

        //add item to start of list
        viewHistory.add(0, (ItemVariant) item);

        //truncate list if greater than MAX_VIEWED
        while (viewHistory.size() > MAX_VIEWED) {
            viewHistory.remove(MAX_VIEWED);
        }
    }

    @Override
    public List<Purchasable> getCart() {
        return cart;
    }

    @Override
    public void addCart(IPurchasable cartItem) {
        //if already in cart, simply increment quantity of that in cart
        for (IPurchasable cItem : cart) {
            if (cItem.getItemVariant().equals(cartItem.getItemVariant())) {
                cItem.addToQuantity(cartItem.getQuantity());
                return;
            }
        }

        //else, add to cart
        cart.add((Purchasable) cartItem);
    }

    @Override
    public void updateCart(List<Purchasable> cartItems) {
        cart = cartItems;
    }


    /**
     * Check does the field name exist
     *
     * @param fieldName Field name (case sensitive)
     * @return True if it is one of the field in User class;
     *         False if it is not
     */
    @Override
    @Exclude
    public boolean isFieldNameValid(String fieldName) {
        try {
            User.class.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "userAuthUid='" + userAuthUid + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
