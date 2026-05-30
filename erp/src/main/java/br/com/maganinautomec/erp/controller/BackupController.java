package br.com.maganinautomec.erp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/backup")
@CrossOrigin
public class BackupController {

    @Value("${sqlite.db.path}")
    private String sqliteDbPath;

    // Nova propriedade para o caminho da pasta do Google Drive
    @Value("${google.drive.local.sync.path}")
    private String googleDriveSyncPath;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> triggerBackup() {
        try {
            Path sourcePath = Paths.get(sqliteDbPath);

            // Adiciona um timestamp ao nome do arquivo
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String destinationFileName = sourcePath.getFileName().toString() + "_" + timestamp;

            // O caminho de destino é dentro da pasta espelhada do Google Drive
            Path destinationPath = Paths.get(googleDriveSyncPath, destinationFileName);

            // Copia o arquivo
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            String message = "Backup copiado para a pasta local do Google Drive com sucesso: " + destinationPath;
            return ResponseEntity.ok(Collections.singletonMap("message", message));

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Falha ao copiar o arquivo de backup: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", errorMessage));
        }
    }
}
