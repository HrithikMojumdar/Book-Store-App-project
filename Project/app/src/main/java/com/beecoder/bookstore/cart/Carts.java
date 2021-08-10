package com.beecoder.bookstore.cart;

import java.util.ArrayList;

public class Carts {
    private static ArrayList<String> bookIds = new ArrayList<>();

    public static void addBookId(String bookId) {
        bookIds.add(bookId);
    }

    public static void removeBookId(String bookId) {
        bookIds.remove(bookId);
    }

    public static boolean hasAddedToCart(String bookId) {
        return bookIds.contains(bookId);
    }

    public static void clearAll() {
        bookIds.clear();
    }
}
