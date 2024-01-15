package com.unrealdinnerbone.curseauthorsapi.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterBuilder
{
    public static final String EMPTY_FILTER = buildFilter(Collections.emptyMap());
    public static String buildFilter(Map<String, String> filter) {
        return "%7B" + filter
                .entrySet()
                .stream()
                .map(entry -> "%22" + entry.getKey() + "%22:%22" + entry.getValue() + "%22")
                .collect(Collectors.joining(","))
                + "%7D";
    }
}
