package backend.flutter.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    @PostMapping("/file")
    public String upload (@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder) {
        return file.getOriginalFilename() + folder;
    }
}
