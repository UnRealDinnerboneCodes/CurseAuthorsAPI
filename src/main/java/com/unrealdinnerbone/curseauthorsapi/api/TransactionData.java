package com.unrealdinnerbone.curseauthorsapi.api;

import java.time.Instant;

public record TransactionData(long id, double pointChange, int type, OrderData order, String dateCreated) {

    public Instant getDateCreated() {
        return Instant.parse(dateCreated);
    }
}
