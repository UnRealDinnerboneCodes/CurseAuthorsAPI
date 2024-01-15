package com.unrealdinnerbone.curseauthorsapi.api;

import java.util.List;

public record ProjectsBreakdownData(List<ProjectBreakdownData> projectsBreakdown, String dateCreated) {}

//Todo
//https://authors.curseforge.com/_api/project-files?filter={%22projectId%22:%22297028%22}
//https://authors.curseforge.com/_api/projects/547229
//todo https://authors.curseforge.com/_api/projects/description/547229
//todo https://authors.curseforge.com/_api/project-license/license/547229
//todo https://authors.curseforge.com/_api/project-source/source/547229