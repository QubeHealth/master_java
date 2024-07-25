package com.master.utility;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class BufferedResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(baos)));

    public BufferedResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public String getBody() {
        writer.flush();
        return baos.toString();
    }
}
