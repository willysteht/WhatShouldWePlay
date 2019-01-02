package authorize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

public class GoogleAuthorizeUtil {
	private static final String CREDENTIALS_FILE_PATH = "credentials.json";
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	private static final String GOOGLE_DEV_URI = "https://developers.google.com/sheets/api/guides/authorizing#APIKey";
	
	private static final Logger logger = LoggerFactory.getLogger(GoogleAuthorizeUtil.class);
	
	public static Credential authorize() {
        InputStream in = null;
		try {
			in = new FileInputStream(CREDENTIALS_FILE_PATH);
		} catch (FileNotFoundException e) {
			logger.error("Credentials needed. See: " + GOOGLE_DEV_URI);
			logger.error("Copy your credentials in here: " + Paths.get(CREDENTIALS_FILE_PATH).toAbsolutePath());
			
			Path file = Paths.get(CREDENTIALS_FILE_PATH);
			try {
				Files.writeString(file, "<your credentials here>", Charset.forName("UTF-8"));
			} catch (IOException e1) {
				logger.error("",e1);
			}
		}
		
        GoogleClientSecrets clientSecrets = null;
		try {
			clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Could not read Credentials!", e);
			System.exit(0);
		}


        GoogleAuthorizationCodeFlow flow;
        Credential credential = null;
		try {
			flow = new GoogleAuthorizationCodeFlow.Builder(
					GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets, SCOPES)
					.setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
			        .setAccessType("offline")
			        .build();
		
					credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		} catch (IOException | GeneralSecurityException e) {
			logger.error("",e);
		}

        return credential;
    }
}
