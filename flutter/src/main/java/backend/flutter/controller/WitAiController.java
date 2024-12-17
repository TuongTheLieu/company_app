package backend.flutter.controller;

import backend.flutter.dto.response.RestResponse;
import backend.flutter.service.WitAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/witai")
public class WitAiController {

    @Autowired
    private WitAiService witAiService;

    // Endpoint nhận truy vấn từ người dùng và gọi Wit.ai API
    @GetMapping("/message")
    public ResponseEntity<RestResponse<String>> getMessageFromWitAi(@RequestParam String query) {
        // Gọi Service để lấy phản hồi từ Wit.ai
        String response1 = witAiService.callWitAi(query);
        RestResponse<String> response = new RestResponse<>();
        response.setData(response1);



        // Trả về kết quả từ Wit.ai
        return ResponseEntity.ok(response);
    }
}
