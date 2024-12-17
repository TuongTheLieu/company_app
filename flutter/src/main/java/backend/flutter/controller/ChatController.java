package backend.flutter.controller;

import backend.flutter.dto.response.RestResponse;
import backend.flutter.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private OpenAiService openAiService;

    @PostMapping
    public ResponseEntity<RestResponse<String>> chat(@RequestBody ChatRequest request) {
        String response1 = openAiService.generateResponse(request.getPrompt());
        RestResponse<String> response = new RestResponse<>();
        response.setData(response1);

        return ResponseEntity.ok(response);
    }
}

class ChatRequest {
    private String prompt;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
