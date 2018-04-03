package com.hal.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sun.misc.BASE64Encoder;


/**
 * Class Utils - Methods to create http parameters
 * @author DHUY
 * 
 */
public final class HttpUtils {

    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String USER_AGENT = "User-Agent";

    private HttpUtils() {

    }

    /**
     * @author usubellil 
     * 2.0-11/12
     * 
     * Post data to HTTP server
     * 
     * @param url the url of the server
     * @param userAgent can be null
     * @param contentType can be null
     * @param bytes data to post
     * @return the string server response or null if the response 
     *          code is different of 200 OK
     * 
     * @throws IOException if an error occurred connecting to the server.
     * 
     */
    public static String securePost(URL url, String userName, String userPassword, String userAgent, String contentType, String content) throws IOException {

        // Check the URL
        if(url == null) {
            
            throw new IOException("The server url cannot be null");
            
        }
        
        // Check the datas
        
        if(content == null) {
            
            throw new IOException("Content to post cannot be null");
            
        }
        
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        // Mutate the request method
        
        httpURLConnection.setRequestMethod("POST");
        
//        httpURLConnection.setRequestProperty("Authorization", "Basic " + BASE64E	ncoder.encode(new String(userName + ":" + userPassword).getBytes()));

        // Settings of HTTP header
        
        if (userAgent != null) {

            httpURLConnection.setRequestProperty(USER_AGENT, userAgent);

        }

        if (contentType != null) {

            httpURLConnection.setRequestProperty(CONTENT_TYPE, contentType);

        }
        
        httpURLConnection.setRequestProperty(CONTENT_LENGTH, Integer.toString(content.length()));
        
        httpURLConnection.setDoOutput(true);
        
        httpURLConnection.setDoInput(true);

        // Open and write on output stream
        
        OutputStream outputStream = httpURLConnection.getOutputStream();

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        bufferedOutputStream.write(content.getBytes());

        bufferedOutputStream.close();
        
        // Check the server response

        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            
            // Open and read on input stream

            InputStream inputStream = httpURLConnection.getInputStream();
            
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            
            BufferedReader br = new BufferedReader(inputStreamReader);
            
            StringBuilder stringBuilder = new StringBuilder();
            
            String read;
            
            while((read = br.readLine()) != null) {
                
                stringBuilder.append(read);
                
            }

            return stringBuilder.toString();

        }

        return null;
        
    }
    
}
