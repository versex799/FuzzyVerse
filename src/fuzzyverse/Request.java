package fuzzyverse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Supplier;

public class Request {

    public UrlInfo GetRequest(String address) throws IOException, InterruptedException {
        UrlInfo urlInfo = new UrlInfo();

        URL url = new URL(address);

        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder().uri(URI.create(address)).build();

        var resp = client.send(request, HttpResponse.BodyHandlers.ofString());

        urlInfo.ReturnCode = resp.statusCode();
        urlInfo.Size = resp.body().length();
        urlInfo.Url = address;

        return urlInfo;
    }

    public UrlInfo PostRequest(String address, Header header, Cookie cookie) throws IOException, InterruptedException {
        UrlInfo urlInfo = new UrlInfo();

        URL url = new URL(address);

        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder().uri(URI.create(address)).build();

        var resp = client.send(request, HttpResponse.BodyHandlers.ofString());

        urlInfo.ReturnCode = resp.statusCode();
        urlInfo.Size = resp.body().length();
        urlInfo.Url = address;

        return urlInfo;
    }
}
