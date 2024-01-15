package com.unrealdinnerbone.curseauthorsapi.api;

import com.unrealdinnerbone.curseauthorsapi.api.TransactionData;

import java.time.Instant;
import java.util.List;

public record File(int id,
                   String fileName,
                   String displayName,
                   Integer parentFileId,
                   int releaseType,
                   String dateCreated,
                   int status,
                   int downloads,
                   List<GameVersion> gameVersions,
                   String changelog,
                   int changeLogType,
                   int childFileType,
                   String downloadUrl
                   ) {


    public Instant getDateCreated() {
        return Instant.parse(dateCreated);
    }
}
