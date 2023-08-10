package com.example.codemate.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class OcrService {

    private static final String IMAGE_FILE_PATH = "/home/ubuntu/ImageFiles/";
    private static final String OCR_DATA_PATH = "/home/ubuntu/Ocr/";
//    private static String IMAGE_FILE_PATH = "C:\\intellij_project\\capstone_image\\";
//    private static String OCR_DATA_PATH = "C:\\intellij_project\\capstone_image\\";

    public String ocr(MultipartFile file) throws IOException {

        /**요청으로 받은 이미지 저장*/
        String filename = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 4, file.getOriginalFilename().length());

        String imageFilePath = IMAGE_FILE_PATH + filename;

        try {
            Path destination = new File(imageFilePath).toPath();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
        /**요청으로 받은 이미지 저장 완료*/

        String result = ocrImage(imageFilePath); // 문자 인식

        return result;
    }


    private Tesseract getTesseract() {
        Tesseract instance = new Tesseract();
        instance.setDatapath(OCR_DATA_PATH);
        instance.setLanguage("eng+kor");//"kor+eng"
        return instance;
    }

    public String ocrImage(String filePath) {

        Tesseract tesseract = getTesseract();

        String result = null;

        File file = new File(filePath);

        if (file.exists() && file.canRead()) {
            try {
                result = tesseract.doOCR(file);
            } catch (TesseractException e) {
                result = e.getMessage();
            }
        } else {
            result = "not exist";
        }
        return result;

    }
}
