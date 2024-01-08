package com.unrealdinnerbone.curseauthorsapi;

import com.unrealdinnerbone.curseauthorsapi.api.*;
import com.unrealdinnerbone.curseauthorsapi.api.base.QueryResult;
import com.unrealdinnerbone.curseauthorsapi.util.CurseforgeAuthorsAPIUtils;
import com.unrealdinnerbone.unreallib.apiutils.result.IResult;
import com.unrealdinnerbone.unreallib.json.api.JsonString;
import com.unrealdinnerbone.unreallib.json.exception.JsonParseException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurseAuthorsAPI {

    @NotNull
    public static IResult<QueryResult<RevenueEstimationData>> getEstimatedRevenue() {
        return getQueryResult(RevenueEstimationData[].class, "statistics/queries/revenueEstimation");
    }

    @NotNull
    public static IResult<QueryResult<DownloadsTotalData>> getTotalDownloads() {
        return getQueryResult(DownloadsTotalData[].class, "statistics/queries/downloadsTotal");
    }

    @NotNull
    public static IResult<List<LastMonthDownloadsData>> getLastMonthDownloads() {
        return getData(LastMonthDownloadsData[].class, "statistics/queries/lastMonthDownloads");
    }

    @NotNull
    public static IResult<List<LastMonthRevenueData>> getLastMonthRevenue() {
        return getData(LastMonthRevenueData[].class, "statistics/queries/lastMonthRevenue");
    }

    @NotNull
    public static IResult<UserPointData> getUserPoints() {
        return getDirectData(UserPointData.class, "reward-store/user-points");
    }

    @NotNull
    public static IResult<RewardStoreData> getRewardStore() {
        return getDirectData(RewardStoreData.class, "reward-store");
    }

    @NotNull
    public static IResult<List<TransactionData>> getTransactions() {
        //Todo fix this
        String url = "transactions?" + "filter=%7B%7D";
        return getDataDirect(TransactionData[].class, url);
    }

    public static IResult<ProjectsBreakdownData> getBreakdown(long id) {
        return getDirectData(ProjectsBreakdownData.class, "transactions/breakdown/" + id);
    }

    public static IResult<List<Project>> getProjects() {
        return getDataDirect(Project[].class, "projects?filter={}");
    }

    @NotNull
    public static IResult<List<ProjectRevenueData>> getRevenue() {
        return getMap("statistics/queries/revenue").map(maps -> {
            List<ProjectRevenueData> data = new ArrayList<>();
            for (Map<String, Long> downloadMap : maps) {
                long revenueMonth = downloadMap.get("revenueMonth");
                int revenue = downloadMap.get("revenue").intValue();
                downloadMap.remove("revenueMonth");
                downloadMap.remove("revenue");
                Map<String, Integer> modDownloads = downloadMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, stringLongEntry -> stringLongEntry.getValue().intValue(), (a, b) -> b));
                data.add(new ProjectRevenueData(revenueMonth, revenue, modDownloads));
            }
            return data;
        });
    }

    @NotNull
    public static IResult<List<ProjectDownloadData>> getDownloads() {
        return getMap("statistics/queries/downloads")
                .map(maps -> {
                    List<ProjectDownloadData> data = new ArrayList<>();
                    for (Map<String, Long> downloadMap : maps) {
                        long revenueMonth = downloadMap.get("downloadDate");
                        int revenue = downloadMap.get("downloads").intValue();
                        downloadMap.remove("downloadDate");
                        downloadMap.remove("downloads");
                        Map<String, Integer> modDownloads = downloadMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, stringLongEntry -> stringLongEntry.getValue().intValue(), (a, b) -> b));
                        data.add(new ProjectDownloadData(revenueMonth, revenue, modDownloads));
                    }
                    return data;
                });
    }



    private static IResult<List<Map<String, Long>>> getMap(String url) {
        return CurseforgeAuthorsAPIUtils.get(MapReturnObject.class, url)
                .map(object -> object.queryResult().data());
    }



    private static @NotNull <T> IResult<QueryResult<T>> getQueryResult(Class<T[]> tClass, String base) {
        return CurseforgeAuthorsAPIUtils.get(ReturnObject.class, base)
                .map((returnObject) -> {
                    String json = returnObject.queryResult().data().json();
                    T[] parse = CurseforgeAuthorsAPIUtils.PARSER.parse(tClass, json);
                    return new QueryResult<>(Arrays.stream(parse)
                            .findFirst()
                            .orElseThrow(() -> new JsonParseException(new IllegalStateException("No Data Found"))), returnObject.queryResult().retrievedAt());
                });
    }

    private static @NotNull <T> IResult<List<T>> getData(Class<T[]> tClass, String base) {
        return CurseforgeAuthorsAPIUtils.get(ReturnObject.class, base)
                .map((returnObject) ->
                        Arrays.asList(CurseforgeAuthorsAPIUtils.PARSER.parse(tClass, returnObject.queryResult().data().json())));
    }

    private static <T> IResult<List<T>> getDataDirect(Class<T[]> tClass, String base) {
        return CurseforgeAuthorsAPIUtils.get(tClass, base).map(Arrays::asList);
    }

    private static @NotNull <T> IResult<T> getDirectData(Class<T> tClass, String base) {
        return CurseforgeAuthorsAPIUtils.get(tClass, base);
    }




    public record ReturnObject(Data queryResult) {
        public record Data(JsonString data, String retrievedAt) {}
    }

    public record MapReturnObject(MapData queryResult) {
        public record MapData(List<Map<String, Long>> data) {}
    }


}
