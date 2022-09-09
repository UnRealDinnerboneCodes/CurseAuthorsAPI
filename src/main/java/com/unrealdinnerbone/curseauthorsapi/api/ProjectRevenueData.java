package com.unrealdinnerbone.curseauthorsapi.api;

import java.util.Map;

public record ProjectRevenueData(long revenueMonth, int revenue, Map<String, Integer> modRevenue) {}
