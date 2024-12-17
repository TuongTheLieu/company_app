package backend.flutter.service;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WitAiService {

    @Value("${witai.api.key}") // Lấy API key từ application.yaml
    private String apiKey;

    private static final String WIT_API_URL = "https://api.wit.ai/message?v=20221204&q=";

    public String callWitAi(String query) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Tạo GET request để gọi Wit.ai API
            HttpGet getRequest = new HttpGet(WIT_API_URL + query);
            getRequest.setHeader("Authorization", "Bearer " + apiKey);

            // Thực thi request và nhận response
            CloseableHttpResponse response = client.execute(getRequest);
            String responseString = EntityUtils.toString(response.getEntity());

            return responseString;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
}
