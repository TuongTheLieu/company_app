package backend.flutter.service;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class OpenAiService {
    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;
    public String generateResponse(String prompt) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(apiUrl);

            // Headers
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + apiKey);

            // Body
            String requestBody = String.format(
                    "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [ { \"role\": \"user\", \"content\": \"%s\" } ] }",
                    prompt
            );
            httpPost.setEntity(new StringEntity(requestBody));

            // Gửi request
            CloseableHttpResponse response = httpClient.execute(httpPost);

            // Kiểm tra mã trạng thái
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode != HttpStatus.SC_OK) {
//                return "Lỗi khi gọi OpenAI API: " + statusCode + " - " + response.getStatusLine().getReasonPhrase();
//            }

            // Kiểm tra entity trong response
            if (response.getEntity() == null) {
                return "Không có phản hồi từ OpenAI API";
            }

            // Đọc response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent())
            );
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            if (result.length() == 0) {
                return "Không có dữ liệu trong phản hồi từ OpenAI API";
            }

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi gọi OpenAI API: " + e.getMessage();
        }
    }

}
