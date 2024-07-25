package com.master.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class BufferedRequestWrapper extends HttpServletRequestWrapper {
    
    private final String body;

    public BufferedRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            char[] buffer = new char[1024];
            int bytesRead;
            while ((bytesRead = bufferedReader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, bytesRead);
            }
        }
        body = stringBuilder.toString();
    }

    public String getBody() {
        return body;
    }
}
