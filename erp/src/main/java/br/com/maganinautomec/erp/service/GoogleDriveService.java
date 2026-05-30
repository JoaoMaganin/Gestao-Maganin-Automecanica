package br.com.maganinautomec.erp.service;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

@Service
public class GoogleDriveService {

    @Autowired
    private GoogleAuthService googleAuthService;

    @Value("${google.drive.folder-id}")
    private String folderId;

    public String uploadFile(String filePath, String mimeType) throws Exception {
        java.io.File localFile = new java.io.File(filePath);
        String originalFileName = localFile.getName();

        String baseName;
        String extension;

        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            baseName = originalFileName.substring(0, lastDotIndex); // "meu-banco"
            extension = originalFileName.substring(lastDotIndex);   // ".sqlite"
        } else {
            baseName = originalFileName;
            extension = "";
        }

        // 3. Formata a data para o padrão ANO-MÊS-DIA
        String dateStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); // ex: "2025-09-01"

        // 4. Monta o novo nome do arquivo
        String newFileName = baseName + "_" + dateStamp + extension; // ex: "meu-banco_2025-09-01.sqlite"

        // Use a variável 'newFileName' ao criar os metadados
        File fileMetadata = new File();
        fileMetadata.setName(newFileName); // <-- AQUI
        fileMetadata.setParents(Collections.singletonList(folderId));

        //String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String fileName = localFile.getName() + "_" + timestamp;

        //File fileMetadata = new File();
        //fileMetadata.setName(fileName);
        //fileMetadata.setParents(Collections.singletonList(folderId));

        InputStreamContent mediaContent = new InputStreamContent(mimeType, new FileInputStream(localFile));

        // Obtém o serviço do Drive já autenticado
        Drive driveService = googleAuthService.getDriveService();

        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        return uploadedFile.getId();
    }
}