package com.unrealdinnerbone.curseauthorsapi.api;

import com.google.gson.annotations.SerializedName;

public record GameVersion(@SerializedName("Label") String label, @SerializedName("ID") int ID) {
}
