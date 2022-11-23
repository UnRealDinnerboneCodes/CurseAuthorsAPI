package com.unrealdinnerbone.curseauthorsapi.api;

public record ProjectBreakdownData(String projectName, String projectUrl, double points) {

    public String getSlug() {
        String[] split = projectUrl.split("/");
        return split[split.length - 1];
    }
}
