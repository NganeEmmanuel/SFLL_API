package com.socialapp.sfll.service;

import com.socialapp.sfll.exceptions.ReadFileException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    private static final String BASE_PATH = "C:/app/files/";

    public String readFile(String filename) {
        try {
            // Vulnerable concatenation leading to path traversal

            Path basePath = Paths.get(BASE_PATH).toRealPath();
            Path resolvedPath = basePath.resolve(filename).normalize();
            // C:/app/files/../../xampp/passwords.txt
            // c:/xampp/passwords.txt

            if(!resolvedPath.startsWith(BASE_PATH)) {
                throw new ReadFileException("Could not read file");
            }

            return Files.readString(resolvedPath);

        } catch (IOException e) {
            throw new ReadFileException("Could not read file");
        }
    }
}
