package com.unrealdinnerbone.curseauthorsapi.api;

public record Project(int id,
                      String name,
                      String game,
                      String gameSLug,
                      String classSlug,
                      String slug,
                      String avatarUrl,
                      String role,

                      int[] userPermissionIds,
                      int status) {
}
