package com.unrealdinnerbone.curseauthorsapi.util;

import com.unrealdinnerbone.unreallib.apiutils.APIUtils;
import com.unrealdinnerbone.unreallib.apiutils.IReturnResult;
import com.unrealdinnerbone.unreallib.web.HttpHelper;
import org.jetbrains.annotations.NotNull;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;

public class CurseforgeAuthorsAPIUtils {

    private static final String BASE_URL = System.getenv().getOrDefault("AUTHORS_API_URL", "https://authors-next.curseforge.com/_api/statistics/queries/");
    private static final String COOKIE = System.getenv().getOrDefault("AUTHORS_COOKIE", "");

    private static final HttpHelper HTTP_HELPER = new CurseHttpHelper();

    @NotNull
    public static <T> IReturnResult<T> get(Class<T> tClass, String urlData) {
        return APIUtils.get(HTTP_HELPER, tClass, BASE_URL + urlData);
    }



    private static class CurseHttpHelper extends HttpHelper {


        public CurseHttpHelper() {
            super(createClient(), "Java");
        }

    }


    private static HttpClient createClient() {
        CookieManager cookieManager = new CookieManager();
        HttpCookie.parse(COOKIE).forEach(httpCookie -> cookieManager.getCookieStore().add(URI.create(httpCookie.getDomain()), httpCookie));
        return HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).cookieHandler(cookieManager).build();
    }

}
