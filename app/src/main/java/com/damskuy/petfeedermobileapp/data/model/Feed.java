package com.damskuy.petfeedermobileapp.data.model;

public class Feed {

    public static final int MIN_FEED_AMOUNT = 1;
    public static final int MAX_FEED_AMOUNT = 7;
    private int feedAmount = 1;

    public Feed setFeedAmount(int amount) {
        if (amount <= MAX_FEED_AMOUNT && amount > MIN_FEED_AMOUNT) {
            feedAmount = amount;
        }
        return this;
    }

    public int getFeedAmount() {
       return feedAmount;
    }
}
