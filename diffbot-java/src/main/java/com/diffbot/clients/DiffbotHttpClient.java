package com.diffbot.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by wadi chemkhi on 02/01/14.
 * Email : wadi.chemkhi@gmail.com
 */
public class DiffbotHttpClient {

    private String token;
    private String version;

    /**
     * @param token the access token for the diffbot API
     */
    public DiffbotHttpClient(String token) {
        this.token = token;
        this.version = "2";
    }

    public DiffbotHttpClient(String token, String version) {
        this.version = version;
        this.token = token;
    }

    /**
     * @param url the url to be processed using the diffbot articles API.
     * @return String representing the json data of an article according to the provided url.
     */
    public String getArticle(String url) throws IOException {
        return getJson("article", url);
    }
    public String getArticle(String url,Map<String,String> params) throws IOException {
        return getJson("article", url,params);
    }

    /**
     * @param url the url to be processed using the diffbot products API.
     * @return String representing the json data of an array of products according to the provided url.
     */
    public String getProducts(String url,Map<String,String> params) throws IOException {
        return getJson("product", url,params);

    }
    public String getProducts(String url) throws IOException {
        return getJson("product", url);

    }
    public String getJson(String api, String url) throws IOException {
        return getJson( api,  url, null);
    }
    public String getJson(String api, String url, Map<String, String> params) throws IOException {

        URI uri = null;
        URIBuilder ub = new URIBuilder()
                .setScheme("https")
                .setHost("api.diffbot.com")
                .setPath("/v" + version + "/" + api);

        ub.setParameter("token", token);
        ub.setParameter("url", url);

        if (params!=null){
          Iterator<String> it = params.keySet().iterator();
          while (it.hasNext()) {
              String key = it.next();
              ub.setParameter(key, params.get(key));
          }
        }
        try {
            uri = ub.build();
            System.out.println(uri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        String json = null;

        response = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                response.getEntity().getContent(), "utf-8"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        json = sb.toString();
        response.close();
        return json;

    }
}
