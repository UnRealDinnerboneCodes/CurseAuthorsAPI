package com.unrealdinnerbone.curseauthorsapi.util;

import com.unrealdinnerbone.unreallib.apiutils.APIUtils;
import com.unrealdinnerbone.unreallib.apiutils.IResult;
import com.unrealdinnerbone.unreallib.apiutils.ResponseData;
import com.unrealdinnerbone.unreallib.exception.ExceptionFunction;
import com.unrealdinnerbone.unreallib.json.JsonUtil;
import com.unrealdinnerbone.unreallib.json.exception.JsonParseException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;

public class CurseforgeAuthorsAPIUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurseforgeAuthorsAPIUtils.class);
    private static final String BASE_URL = System.getenv().getOrDefault("AUTHORS_API_URL", "https://authors-next.curseforge.com" +
            "");
    private static final String COOKIE = System.getenv().getOrDefault("AUTHORS_COOKIE", "");

    public static final HttpClient CLIENT = createClient();

    @NotNull
    public static <T> IResult<T> get(Class<T> tClass, String urlData) {
        IResult<ResponseData<T>> result = APIUtils.getResult(CLIENT, tClass, BASE_URL + urlData, JsonUtil.DEFAULT);
        return result
                .map(new ExceptionFunction<JsonParseException, ResponseData<T>, T>() {
                    @Override
                    public T apply(ResponseData<T> tResponseData) throws JsonParseException {
                        if(tResponseData.statusCode() != 200) {
                            JsonParseException jsonParseException = new JsonParseException("Not Vaild Response Code: " + tResponseData.statusCode());
                            LOGGER.error("Not 200 " + tResponseData.data().toString(), tResponseData);
                            throw jsonParseException;
                        }
                        return tResponseData.data();
                    }
                });
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
