package com.example.superdataworker.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class TestUtils {

    public static MultipartFile createMultipartFile(String fileName, byte[] content) throws IOException {
        return new MockMultipartFile(fileName, fileName, null, content);
    }
}
