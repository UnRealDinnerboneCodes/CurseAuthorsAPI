package com.unrealdinnerbone.curseauthorsapi.api;

import java.util.List;

public record ProjectsBreakdownData(List<ProjectBreakdownData> projectBreakdowns, String dateCreated) {}
