package com.master.utility;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import com.emv.qrcode.decoder.mpm.DecoderMpm;
import com.emv.qrcode.model.mpm.MerchantAccountInformationReservedAdditional;
import com.emv.qrcode.model.mpm.MerchantPresentedMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.api.QrData;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Helper {
    private Helper() {
    }

    private static byte[] getEncryptDecryptKey() {
        String keyString = "DocumentKey";
        byte[] keyBuf = keyString.getBytes(StandardCharsets.UTF_8);
        int paddingLength = 16 - keyBuf.length;
        byte[] paddingBuf = new byte[paddingLength];
        Arrays.fill(paddingBuf, (byte) 0);
        return Arrays.copyOf(concatByteArrays(keyBuf, paddingBuf), 16);
    }

    private static byte[] concatByteArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }

        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, currentIndex, array.length);
            currentIndex += array.length;
        }
        return result;
    }

    public static String decryptData(String userId, String data) {
        if (userId == null || data == null) {
            return null;
        }

        byte[] decryptionKey = getEncryptDecryptKey();
        byte[] iv = concatByteArrays("123".getBytes(StandardCharsets.UTF_8), userId.getBytes(StandardCharsets.UTF_8),
                "456".getBytes(StandardCharsets.UTF_8));
        try {
            Cipher decipher = Cipher.getInstance("AES/CTR/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(decryptionKey, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            decipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decodedData = Base64.getDecoder().decode(data);
            byte[] decrypted = decipher.doFinal(decodedData);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptData(String userId, String data) {
        try {
            byte[] encryptionKey = getEncryptDecryptKey();
            byte[] iv = concatByteArrays("123".getBytes(StandardCharsets.UTF_8),
                    userId.getBytes(StandardCharsets.UTF_8),
                    "456".getBytes(StandardCharsets.UTF_8));
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKey = new SecretKeySpec(encryptionKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Object> getDataFromMap(Object object, List<String> keys) {
        Set<Object> result = new HashSet<>();

        iter(object, keys, result);

        return new ArrayList<>(result);
    }

    private static void iter(Object obj, List<String> keys, Set<Object> results) {
        if (!(obj instanceof Map)) {
            return;
        }

        Map<String, Object> map = (Map<String, Object>) obj;
        for (String key : keys) {
            Object value = map.get(key);
            if (value instanceof Object && !value.toString().isEmpty()) {
                results.add(value);
            }
        }
        map.values().stream().filter(Objects::nonNull).forEach(v -> iter(v, keys, results));
    }

    public static String formatDate(String date, String currentFormat, String convertToFormat) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(currentFormat));
        return localDate.format(DateTimeFormatter.ofPattern(convertToFormat));
    }

    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDate.format(formatter);
    }

    public static String toJsonString(Object obj) {
        // ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        // Convert object to JSON string
        String jsonString;
        try {
            jsonString = mapper.writeValueAsString(obj);
            return jsonString;
        } catch (JsonProcessingException e) {
            System.out.println("\nJSON conversion failed => " + e.getMessage());
            return "";
        }

    }

    public static String md5Encryption(String input) {
        return DigestUtils.md5Hex(input);
    }

    public static boolean isValidUrl(String urlString) {
        try {
            // Attempt to create a URL object
            new URL(urlString);
            return true; // If no exception is thrown, URL is valid
        } catch (Exception e) {
            return false; // If an exception is caught, URL is not valid
        }
    }

    public static class DataMapper {
        private DataMapper() {
        }

        public static <T> Map<String, Object> mapApiResponse(T response) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.convertValue(response, new TypeReference<Map<String, Object>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyMap();
            }
        }

    }

    public static QrData parseUPIUrl(String url) {
        try {

            Map<String, String> queryParams = new HashMap<>();

            // Extract query string from URL
            String queryString = url.substring(url.indexOf('?') + 1);

            // Use regex pattern to match key-value pairs
            Pattern pattern = Pattern.compile("([^&=]+)=([^&]*)");
            Matcher matcher = pattern.matcher(queryString);

            // Decode each key-value pair and put into map
            while (matcher.find()) {
                String key = URLDecoder.decode(matcher.group(1), StandardCharsets.UTF_8.name());
                String value = URLDecoder.decode(matcher.group(2), StandardCharsets.UTF_8.name());
                queryParams.put(key, value);
            }

            System.out.println("UPI URL PARSER => " + Helper.toJsonString(queryParams));

            if (queryParams.isEmpty()) {
                return null;
            }

            // Creating the HashMap with the required key-value pairs
            QrData data = new QrData();
            data.setAmount(queryParams.getOrDefault("am", ""));
            data.setMccCode(queryParams.getOrDefault("mc", ""));
            data.setMerchantName(queryParams.getOrDefault("pn", ""));
            data.setTransactionId(queryParams.getOrDefault("tr", ""));
            data.setVpa(queryParams.getOrDefault("pa", ""));

            return data;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static QrData parseEMVQR(String url) {
        try {

            QrData queryParams = new QrData();

            MerchantPresentedMode data = DecoderMpm.decode(url, MerchantPresentedMode.class);
            System.out.println("EMV QR PARSER => " + Helper.toJsonString(data));

            queryParams.setMccCode(data.getMerchantCategoryCode().getValue());

            queryParams.setMccCode(data.getMerchantName().getValue());
            queryParams.setVpa(
                    data.getMerchantAccountInformation().get("26")
                            .getTypeValue(MerchantAccountInformationReservedAdditional.class)
                            .getPaymentNetworkSpecific().get("01").getValue());
            queryParams.setMerchantCity(data.getMerchantCity().getValue());
            queryParams.setMerchantPincode(data.getPostalCode().getValue());
            queryParams.setTransactionId(data.getAdditionalDataField().getValue().getReferenceLabel().getValue());
            queryParams.setAmount(data.getTransactionAmount().getValue());

            return queryParams;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    public static Map<String, Object> jsonToMap(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                value = jsonToMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }
}
