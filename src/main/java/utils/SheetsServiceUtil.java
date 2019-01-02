package utils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;

import authorize.GoogleAuthorizeUtil;

public class SheetsServiceUtil {
	private final static String APPLICATION_NAME = "WhatShouldWePlay";
	
    public static Sheets getSheetsService() throws GeneralSecurityException, IOException {
        Credential credential = GoogleAuthorizeUtil.authorize();
        
        return new Sheets.Builder(
          GoogleNetHttpTransport.newTrustedTransport(), 
          JacksonFactory.getDefaultInstance(), credential)
          .setApplicationName(APPLICATION_NAME)
          .build();
    }
}
