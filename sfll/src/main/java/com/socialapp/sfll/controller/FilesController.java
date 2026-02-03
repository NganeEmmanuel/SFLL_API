package com.socialapp.sfll.controller;

import com.socialapp.sfll.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/files")
public class FilesController {

    @Autowired
    private FileService fileService;

    @GetMapping("/read")
    @ResponseStatus(HttpStatus.OK)
    public String read(@RequestParam String filename) {
        return fileService.readFile(filename);
    }
}
