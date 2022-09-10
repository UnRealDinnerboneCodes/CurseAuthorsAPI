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
        return getQueryResult(RevenueEstimationData[].class, "revenueEstimation");
    }

    @NotNull
    public static IReturnResult<QueryResult<DownloadsTotalData>> getTotalDownloads() {
        return getQueryResult(DownloadsTotalData[].class, "downloadsTotal");
    }

    @NotNull
    public static IReturnResult<List<LastMonthDownloadsData>> getLastMonthDownloads() {
        return getData(LastMonthDownloadsData[].class, "lastMonthDownloads");
    }

    @NotNull
    public static IReturnResult<List<LastMonthRevenueData>> getLastMonthRevenue() {
        return getData(LastMonthRevenueData[].class, "lastMonthRevenue");
    }

    @NotNull
    public static IReturnResult<List<ProjectRevenueData>> getRevenue() {
        IReturnResult<List<Map<String, Long>>> theMap = getMap("revenue");
        try {
            List<ProjectRevenueData> data = new ArrayList<>();
            for (Map<String, Long> downloadMap : theMap.getExceptionally()) {
                long revenueMonth = downloadMap.get("revenueMonth");
                int revenue = downloadMap.get("revenue").intValue();
                downloadMap.remove("revenueMonth");
                downloadMap.remove("revenue");
                Map<String, Integer> modDownloads = downloadMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, stringLongEntry -> stringLongEntry.getValue().intValue(), (a, b) -> b));
                data.add(new ProjectRevenueData(revenueMonth, revenue, modDownloads));
            }
            return new DirectResult<>(data);
        }catch (JsonParseException e) {
            return IReturnResult.createException(e);
        }
    }

    @NotNull
    public static IReturnResult<List<ProjectDownloadData>> getDownloads() {
        IReturnResult<List<Map<String, Long>>> theMap = getMap("downloads");
        try {
            List<ProjectDownloadData> data = new ArrayList<>();
            for (Map<String, Long> downloadMap : theMap.getExceptionally()) {
                long revenueMonth = downloadMap.get("downloadDate");
                int revenue = downloadMap.get("downloads").intValue();
                downloadMap.remove("downloadDate");
                downloadMap.remove("downloads");
                Map<String, Integer> modDownloads = downloadMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, stringLongEntry -> stringLongEntry.getValue().intValue(), (a, b) -> b));
                data.add(new ProjectDownloadData(revenueMonth, revenue, modDownloads));
            }
            return new DirectResult<>(data);
        }catch (JsonParseException e) {
            return IReturnResult.createException(e);
        }
    }



    private static IReturnResult<List<Map<String, Long>>> getMap(String url) {
        try {
            IReturnResult<MapReturnObject> result = CurseforgeAuthorsAPIUtils.get(MapReturnObject.class, url);
            MapReturnObject returnObject = result.getExceptionally();
            return new DirectResult<>(returnObject.queryResult().data());
        } catch (JsonParseException e) {
            return IReturnResult.createException(e);
        }

    }



    private static @NotNull <T> IReturnResult<QueryResult<T>> getQueryResult(Class<T[]> tClass, String base) {
        try {
            IReturnResult<ReturnObject> result = CurseforgeAuthorsAPIUtils.get(ReturnObject.class, base);
            ReturnObject returnObject = result.getExceptionally();
            T[] data = JsonUtil.DEFAULT.parse(tClass, returnObject.queryResult().data());
            QueryResult<T> queryResult = new QueryResult<>(Arrays.stream(data).findFirst().orElseThrow(() -> new JsonParseException(new IllegalStateException("No Data Found"))), returnObject.queryResult().retrievedAt());
            return new DirectResult<>(queryResult);
        } catch (JsonParseException e) {
            return IReturnResult.createException(e);
        }
    }

    private static @NotNull <T> IReturnResult<List<T>> getData(Class<T[]> tClass, String base) {
        try {
            IReturnResult<ReturnObject> result = CurseforgeAuthorsAPIUtils.get(ReturnObject.class, base);
            ReturnObject returnObject = result.getExceptionally();
            T[] data = JsonUtil.DEFAULT.parse(tClass, returnObject.queryResult().data());
            return new DirectResult<>(Arrays.asList(data));
        } catch (JsonParseException e) {
            return IReturnResult.createException(e);
        }
    }




    public record ReturnObject(Data queryResult) {
        public record Data(@DataString String data, String retrievedAt) {}
    }

    public record MapReturnObject(MapData queryResult) {
        public record MapData(List<Map<String, Long>> data) {}
    }


}
