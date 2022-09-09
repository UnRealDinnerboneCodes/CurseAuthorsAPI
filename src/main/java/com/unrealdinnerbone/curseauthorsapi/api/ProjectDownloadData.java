package com.unrealdinnerbone.curseauthorsapi.api;

import java.util.Map;

public record ProjectDownloadData(long downloadDate, int downloads, Map<String, Integer> modDownloads) {
}
