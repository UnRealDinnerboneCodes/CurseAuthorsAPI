package com.unrealdinnerbone.curseauthorsapi.api;

import java.time.Instant;

public record LastMonthRevenueData(long revenueDate, long revenue) {
    public Instant getRevenueDate() {
        return Instant.ofEpochMilli(revenueDate);
    }
}
