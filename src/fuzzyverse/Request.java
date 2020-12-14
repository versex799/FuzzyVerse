package fuzzyverse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class Request {

    public UrlInfo GetRequestTesting(String address) throws IOException {
        UrlInfo urlInfo = new UrlInfo();
        urlInfo.Url = address;
        urlInfo.Size = 0;

        URL url = new URL(address);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        conn.setRequestMethod("GET");

        int responseCode = 0;
        try {
            responseCode = conn.getResponseCode();
        }
        catch(IOException ex)
        {
            responseCode = -1;
        }
        urlInfo.ReturnCode = responseCode;

        if(responseCode == HttpURLConnection.HTTP_OK)
        {
                BufferedReader inputReader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = inputReader.readLine()) != null) {
                    response.append(inputLine);
                }

                urlInfo.Size = response.length();
                inputReader.close();
        }

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
