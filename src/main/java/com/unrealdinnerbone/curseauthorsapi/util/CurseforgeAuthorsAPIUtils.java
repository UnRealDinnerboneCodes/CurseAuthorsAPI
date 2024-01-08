package com.unrealdinnerbone.curseauthorsapi.util;

import com.google.gson.stream.JsonReader;
import com.unrealdinnerbone.curseauthorsapi.api.TransactionData;
import com.unrealdinnerbone.unreallib.apiutils.APIUtils;
import com.unrealdinnerbone.unreallib.apiutils.result.IResult;
import com.unrealdinnerbone.unreallib.json.JsonUtil;
import com.unrealdinnerbone.unreallib.json.gson.GsonParser;
import com.unrealdinnerbone.unreallib.json.gson.parsers.basic.IIDJsonAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;

public class CurseforgeAuthorsAPIUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurseforgeAuthorsAPIUtils.class);
    private static final String BASE_URL = System.getenv().getOrDefault("AUTHORS_API_URL", "https://authors.curseforge.com/_api/" +
            "");
    private static final String COOKIE = System.getenv().getOrDefault("AUTHORS_COOKIE", "");

    public static final HttpClient CLIENT = createClient();

    public static final GsonParser PARSER = JsonUtil.createParser(gsonBuilder -> gsonBuilder.registerTypeAdapter(TransactionData.Type.class, new FixAd(TransactionData.Type.values()) {

    }));

    public static class FixAd extends IIDJsonAdapter<TransactionData.Type> {

        public FixAd(Object[] enumConstants) {
            super(enumConstants);
        }

        @Override
        public TransactionData.Type read(JsonReader in) throws IOException {
            try {
                return super.read(in);
            }catch (Exception e) {
                LOGGER.error("Failed to parse TransactionData.Type", e);
                return TransactionData.Type.UNKNOWN;
            }
        }
    }

    @NotNull
    public static <T> IResult<T> get(Class<T> tClass, String urlData) {
        return APIUtils.getJson(CLIENT, tClass, BASE_URL + urlData, PARSER);
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
