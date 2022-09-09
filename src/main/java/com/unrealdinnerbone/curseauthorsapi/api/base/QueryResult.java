package com.unrealdinnerbone.curseauthorsapi.api.base;

import java.util.List;

public record QueryResult<T>(List<T> data, String retrievedAt) {}
