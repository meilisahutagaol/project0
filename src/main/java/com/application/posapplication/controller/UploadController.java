package com.application.posapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UploadController {
    @RequestMapping("/upload")
    public String upload(){
        return "upload";
    }
}
