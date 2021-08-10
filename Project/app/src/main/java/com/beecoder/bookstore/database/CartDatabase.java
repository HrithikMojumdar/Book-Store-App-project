package com.beecoder.bookstore.database;

import com.beecoder.bookstore.cart.Carts;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CartDatabase {
    private String userId = FirebaseAuth.getInstance().getUid();
    private String field_cart = "cart";
    private CollectionReference bookRef = FirebaseFirestore.getInstance().collection("Books");

    public Task<Void> addToCart(String bookId) {
        Carts.addBookId(bookId);
        return bookRef.document(bookId)
                .update(field_cart, FieldValue.arrayUnion(userId));
    }

    public Query getBookIds() {
        return bookRef.whereArrayContains(field_cart, userId).whereEqualTo("approved", true)
                .whereEqualTo("sold", false);
    }

    public Task<Void> removeBook(String id) {
        return bookRef.document(id)
                .update(field_cart, FieldValue.arrayRemove(userId));

    }
}
