package com.unrealdinnerbone.curseauthorsapi.api;

import java.time.Instant;
import java.util.Map;

public record ProjectRevenueData(long revenueMonth, int revenue, Map<String, Integer> modRevenue) {
    public Instant getRevenueMonth() {
        return Instant.ofEpochMilli(revenueMonth);
    }
}
