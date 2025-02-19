package com.unrealdinnerbone.curseauthorsapi.api;

import com.unrealdinnerbone.unreallib.json.api.IID;

import java.time.Instant;

public record TransactionData(long id, double pointChange, Type type, OrderData order, String dateCreated) {

    public Instant getDateCreated() {
        return Instant.parse(dateCreated);
    }


    public enum Type implements IID {
        REWARD(1),
        TRANSFER_OUT(2),
        TRANSFER_IN(3),
        PLACED(5),
        FULFILLED(8),
        EXPIRED(7),
        PAYPAL_OLD(6),
        PAYPAL(11),
        TREMENDOUS(12),

        UNKNOWN(-1)
        ;

        private final int id;
        Type(int i) {
            this.id = i;
        }

        @Override
        public int getId() {
            return id;
        }
    }
}
