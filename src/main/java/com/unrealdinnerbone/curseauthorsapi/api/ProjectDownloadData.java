package com.unrealdinnerbone.curseauthorsapi.api;

import java.time.Instant;
import java.util.Map;

public record ProjectDownloadData(long downloadDate, int downloads, Map<String, Integer> modDownloads) {

    public Instant getDownloadDate() {
        return Instant.ofEpochMilli(downloadDate);
    }
}
