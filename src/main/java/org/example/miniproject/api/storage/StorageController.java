package org.example.miniproject.api.storage;

import lombok.RequiredArgsConstructor;
import org.example.miniproject.libs.services.StorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    @PostMapping(consumes = "multipart/form-data")
    public void store(MultipartFile file) throws IOException {
        storageService.put(StorageService.Storage.DEFAULT, "", file.getInputStream());
    }
}
