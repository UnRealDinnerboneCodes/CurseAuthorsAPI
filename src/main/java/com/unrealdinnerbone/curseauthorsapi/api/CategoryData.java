package com.unrealdinnerbone.curseauthorsapi.api;

import java.util.List;

public record CategoryData(int id, String name, List<ItemData> availbleItems) {
}
