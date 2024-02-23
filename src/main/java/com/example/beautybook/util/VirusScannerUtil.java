package com.example.beautybook.util;

import xyz.capybara.clamav.commands.scan.result.ScanResult;

public interface VirusScannerUtil {
    ScanResult scanFile(String path);
}
