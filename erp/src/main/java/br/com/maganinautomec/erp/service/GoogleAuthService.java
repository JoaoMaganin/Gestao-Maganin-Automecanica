package br.com.maganinautomec.erp.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleAuthService {

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);

    @Value("${google.client-secret.path}")
    private String clientSecretPath;

    @Value("${google.credentials-store.path}")
    private String credentialsStorePath;

    private Credential authorize() throws Exception {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // Carrega os segredos do cliente do arquivo JSON
        InputStream in = getClass().getResourceAsStream(clientSecretPath.replace("classpath:", "/"));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Constrói o fluxo de autorização e aponta para o local onde o token será salvo
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(credentialsStorePath)))
                .setAccessType("offline") // Essencial para obter o refresh_token
                .build();

        // Dispara o processo de autorização
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        // A linha abaixo abrirá o navegador para você autorizar a aplicação
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Drive getDriveService() throws Exception {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize();
        return new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName("SpringBoot Backup App")
                .build();
    }
}
