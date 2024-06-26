package com.example.beautybook.util.impl;

import com.example.beautybook.util.VirusScannerUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

@Component
public class VirusScannerUtilImpl implements VirusScannerUtil {
    private final ClamavClient clamavClient;

    public VirusScannerUtilImpl(
            @Value("${clamav.host}") String host,
            @Value("${clamav.port}") int port
    ) {
        this.clamavClient = new ClamavClient(host, port);
    }

    public ScanResult scanFile(String path) {
        Path filePath = Path.of(path);
        ScanResult scan = clamavClient.scan(filePath);
        if (scan instanceof ScanResult.VirusFound) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error deleting the infected file: " + path, e);
            }
            return scan;
        }
        return scan;
    }
}
