package com.unrealdinnerbone.curseauthorsapi.api.base;

import java.time.Instant;

public record QueryResult<T>(T data, String retrievedAt) {
    public Instant getRetrievedAt() {
        return Instant.parse(retrievedAt);
    }
}
