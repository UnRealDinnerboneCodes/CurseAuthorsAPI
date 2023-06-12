package com.unrealdinnerbone.curseauthorsapi.util;

import com.unrealdinnerbone.unreallib.apiutils.APIUtils;
import com.unrealdinnerbone.unreallib.apiutils.IResult;
import com.unrealdinnerbone.unreallib.apiutils.ResponseData;
import com.unrealdinnerbone.unreallib.json.JsonUtil;
import org.jetbrains.annotations.NotNull;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;

public class CurseforgeAuthorsAPIUtils {

    private static final String BASE_URL = System.getenv().getOrDefault("AUTHORS_API_URL", "https://authors-next.curseforge.com" +
            "");
    private static final String COOKIE = System.getenv().getOrDefault("AUTHORS_COOKIE", "");

    public static final HttpClient CLIENT = createClient();

    @NotNull
    public static <T> IResult<T> get(Class<T> tClass, String urlData) {
        return APIUtils.getResult(CLIENT, tClass, BASE_URL + urlData, JsonUtil.DEFAULT)
                .map(ResponseData::data);
    }



    private static HttpClient createClient() {
        CookieManager cookieManager = new CookieManager();
        HttpCookie.parse(COOKIE)
                .forEach(httpCookie -> cookieManager.getCookieStore().add(URI.create(httpCookie.getDomain()), httpCookie));
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .cookieHandler(cookieManager).build();
    }

}
