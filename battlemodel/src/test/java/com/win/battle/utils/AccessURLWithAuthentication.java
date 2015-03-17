package com.win.battle.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author vgryb
 */
public class AccessURLWithAuthentication {
    
    private final String authUrl = "/admin/j_spring_security_check";
    String host = "localhost";
    int port = 8080;
    String protocol = "http";
    String username;
    String password;
    
    public AccessURLWithAuthentication() {
    }
    
    public AccessURLWithAuthentication(String host, int port, String protocol, String username, String password) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.username = username;
        this.password = password;
    }
    
    public String getPageThroughAuth(String url) {
        
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpHost httpHost = new HttpHost(host, port, protocol);
        
        try {
            HttpGet securedResource = new HttpGet(url);
             HttpResponse httpResponse;
            HttpEntity responseEntity;
            String strResponse;
//            HttpResponse httpResponse = httpclient.execute(httpHost, securedResource);
//            HttpEntity responseEntity = httpResponse.getEntity();
//            String strResponse = EntityUtils.toString(responseEntity);
//            int statusCode = httpResponse.getStatusLine().getStatusCode();
//            EntityUtils.consume(responseEntity);
            
//            System.out.println("Http status code for Unauthenticated Request: " + statusCode);// Statue code should be 200
//            System.out.println("Response for Unauthenticated Request: \n" + strResponse); // Should be login page
//            System.out.println("================================================================\n");
            
            HttpPost authpost = new HttpPost(authUrl);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("j_username", this.username));
            nameValuePairs.add(new BasicNameValuePair("j_password", this.password));
            authpost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
            httpResponse = httpclient.execute(httpHost, authpost);
            responseEntity = httpResponse.getEntity();
//            strResponse = EntityUtils.toString(responseEntity);
//            statusCode = httpResponse.getStatusLine().getStatusCode();
            EntityUtils.consume(responseEntity);
            
//            System.out.println("Http status code for Authenticattion Request: " + statusCode);// Status code should be 302
//            System.out.println("Response for Authenticattion Request: \n" + strResponse); // Should be blank string
//            System.out.println("================================================================\n");
            
            httpResponse = httpclient.execute(httpHost, securedResource);
            responseEntity = httpResponse.getEntity();
            strResponse = EntityUtils.toString(responseEntity);
//            statusCode = httpResponse.getStatusLine().getStatusCode();
            EntityUtils.consume(responseEntity);
            
//            System.out.println("Http status code for Authenticated Request: " + statusCode);// Status code should be 200
//            System.out.println("Response for Authenticated Request: \n" + strResponse);// Should be actual page
//            System.out.println("================================================================\n");
            
            return strResponse;
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }
        
        return null;
    }
    
}
