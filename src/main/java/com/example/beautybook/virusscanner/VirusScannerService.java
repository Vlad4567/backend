package com.example.beautybook.virusscanner;

import xyz.capybara.clamav.commands.scan.result.ScanResult;

public interface VirusScannerService {
    ScanResult scanFile(String path);
}
