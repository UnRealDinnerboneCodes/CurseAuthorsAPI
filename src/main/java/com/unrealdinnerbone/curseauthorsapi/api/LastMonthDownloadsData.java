package com.unrealdinnerbone.curseauthorsapi.api;

import java.time.Instant;

public record LastMonthDownloadsData(long downloadDate, long totalDownloads) {
    public Instant getDownloadDate() {
        return Instant.ofEpochMilli(downloadDate);
    }
}
