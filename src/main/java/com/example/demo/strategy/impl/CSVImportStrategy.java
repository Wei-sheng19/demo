package com.example.demo.strategy.impl;

import com.example.demo.strategy.ImportStrategy;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class CSVImportStrategy implements ImportStrategy {
    
    @Override
    public List<Map<String, String>> parseFile(MultipartFile file) throws Exception {
        List<Map<String, String>> records = new ArrayList<>();
        
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {
            
            for (CSVRecord csvRecord : csvParser) {
                Map<String, String> data = new HashMap<>();
                csvParser.getHeaderNames().forEach(header -> 
                    data.put(header, csvRecord.get(header))
                );
                records.add(data);
            }
        }
        
        return records;
    }
    
    @Override
    public boolean validateFileFormat(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return filename != null && filename.toLowerCase().endsWith(".csv");
    }
} 