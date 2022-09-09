package com.unrealdinnerbone.curseauthorsapi;

import com.unrealdinnerbone.curseauthorsapi.api.*;
import com.unrealdinnerbone.curseauthorsapi.api.base.QueryResult;
import com.unrealdinnerbone.curseauthorsapi.util.CurseforgeAuthorsAPIUtils;
import com.unrealdinnerbone.unreallib.apiutils.DirectResult;
import com.unrealdinnerbone.unreallib.apiutils.IReturnResult;
import com.unrealdinnerbone.unreallib.json.JsonParseException;
import com.unrealdinnerbone.unreallib.json.JsonUtil;
import com.unrealdinnerbone.unreallib.json.api.DataString;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class CurseAuthorsAPI {


    @NotNull
    public static IReturnResult<QueryResult<RevenueEstimationData>> getEstimatedRevenue() {
        return get(RevenueEstimationData[].class, "revenueEstimation");
    }

    @NotNull
    public static IReturnResult<QueryResult<LastMonthDownloadsData>> getLastMonthDownloads() {
        return get(LastMonthDownloadsData[].class, "lastMonthDownloads");
    }

    @NotNull
    public static IReturnResult<QueryResult<LastMonthRevenueData>> getLastMonthRevenue() {
        return get(LastMonthRevenueData[].class, "lastMonthRevenue");
    }

    @NotNull
    public static IReturnResult<QueryResult<DownloadsTotalData>> getTotalDownloads() {
        return get(DownloadsTotalData[].class, "downloadsTotal");
    }


    @NotNull
    public static IReturnResult<QueryResult<ProjectDownloadData>> getProjectDownloads() {
        return get(ProjectDownloadData[].class, "downloads");
    }

    @NotNull
    public static IReturnResult<QueryResult<ProjectRevenueData>> getRevenue() {
        IReturnResult<QueryResult<Map<String, Long>>> theMap = getMap("revenue");
        try {
            QueryResult<Map<String, Long>> queryResult = theMap.getExceptionally();
            List<ProjectRevenueData> data = new ArrayList<>();
            for (Map<String, Long> downloadMap : queryResult.data()) {
                long revenueMonth = downloadMap.get("revenueMonth");
                int revenue = downloadMap.get("revenue").intValue();
                downloadMap.remove("revenueMonth");
                downloadMap.remove("revenue");
                Map<String, Integer> modDownloads = downloadMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, stringLongEntry -> stringLongEntry.getValue().intValue(), (a, b) -> b));
                data.add(new ProjectRevenueData(revenueMonth, revenue, modDownloads));
            }
            return new DirectResult<>(new QueryResult<>(data, queryResult.retrievedAt()));
        }catch (JsonParseException e) {
            return IReturnResult.createException(e);
        }
    }

    @NotNull
    public static IReturnResult<QueryResult<ProjectDownloadData>> getDownloads() {
        IReturnResult<QueryResult<Map<String, Long>>> theMap = getMap("downloads");
        try {
            QueryResult<Map<String, Long>> queryResult = theMap.getExceptionally();
            List<ProjectDownloadData> data = new ArrayList<>();
            for (Map<String, Long> downloadMap : queryResult.data()) {
                long revenueMonth = downloadMap.get("downloadDate");
                int revenue = downloadMap.get("downloads").intValue();
                downloadMap.remove("downloadDate");
                downloadMap.remove("downloads");
                Map<String, Integer> modDownloads = downloadMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, stringLongEntry -> stringLongEntry.getValue().intValue(), (a, b) -> b));
                data.add(new ProjectDownloadData(revenueMonth, revenue, modDownloads));
            }
            return new DirectResult<>(new QueryResult<>(data, queryResult.retrievedAt()));
        }catch (JsonParseException e) {
            return IReturnResult.createException(e);
        }
    }



    private static IReturnResult<QueryResult<Map<String, Long>>> getMap(String url) {
        try {
            IReturnResult<MapReturnObject> result = CurseforgeAuthorsAPIUtils.get(MapReturnObject.class, url);
            MapReturnObject returnObject = result.getExceptionally();
            return new DirectResult<>(new QueryResult<>(returnObject.queryResult().data(), returnObject.queryResult().retrievedAt()));
        } catch (JsonParseException e) {
            return IReturnResult.createException(e);
        }

    }



    private static @NotNull <T> IReturnResult<QueryResult<T>> get(Class<T[]> tClass, String base) {
        try {
            IReturnResult<ReturnObject> result = CurseforgeAuthorsAPIUtils.get(ReturnObject.class, base);
            ReturnObject returnObject = result.getExceptionally();
            T[] data = JsonUtil.DEFAULT.parse(tClass, returnObject.queryResult().data());
            QueryResult<T> queryResult = new QueryResult<>(Arrays.asList(data), returnObject.queryResult().retrievedAt());
            return new DirectResult<>(queryResult);
        } catch (JsonParseException e) {
            return IReturnResult.createException(e);
        }
    }



    public record ReturnObject(Data queryResult) {
        public record Data(@DataString String data, String retrievedAt) {}
    }

    public record MapReturnObject(MapData queryResult) {
        public record MapData(List<Map<String, Long>> data, String retrievedAt) {}
    }


}
