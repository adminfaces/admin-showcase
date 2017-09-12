package com.github.adminfaces.showcase.analytics.driver;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class DriverService {

    private DriverService() {

    }


    private static Credential authorize() throws IOException, GeneralSecurityException {

        GoogleCredential credential = createCredentialWithRefreshToken(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new TokenResponse().setRefreshToken(System.getenv("refreshtoken")));

        credential.refreshToken();

        return credential;
    }

    private static GoogleCredential createCredentialWithRefreshToken(HttpTransport transport,
                                                                     JsonFactory jsonFactory, TokenResponse tokenResponse) {
        return new GoogleCredential.Builder().setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(System.getenv("clientid"), System.getenv("clientsecret"))
                .build()
                .setFromTokenResponse(tokenResponse);
    }


    /**
     * Build and return an authorized Drive client service.
     *
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("adminfaces")
                .build();
    }
}
