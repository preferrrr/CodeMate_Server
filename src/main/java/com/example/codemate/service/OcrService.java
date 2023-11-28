package com.example.codemate.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Service
public class OcrService {

    private static final String IMAGE_PATH = "/home/ubuntu/images/";
    private static final String OCR_DATA_PATH = "/home/ubuntu/Ocr/";

    public String ocr(MultipartFile multipartFile) throws IOException {

        File file = convertMultipartFileToFile(multipartFile);

        String result = ocr(file);

        return result;

    }


    private Tesseract getTesseract() {
        Tesseract instance = new Tesseract();
        instance.setDatapath(OCR_DATA_PATH);
        instance.setLanguage("eng+kor");//"kor+eng"
        return instance;
    }

    private String ocr(File file) throws IOException{

        Tesseract tesseract = getTesseract();

        String result = null;

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

//    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
//        File file = new File("convertedFile.png", null); // 저장될 파일 객체 생성
//        try (OutputStream os = new FileOutputStream(file)) {
//            InputStream is = multipartFile.getInputStream(); // MultipartFile로부터 InputStream 얻기
//            byte[] buffer = new byte[1024]; // 버퍼 크기 설정
//            int bytesRead;
//            while ((bytesRead = is.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//        }
//
//        return file;
//    }

    public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(IMAGE_PATH + UUID.randomUUID() + ".png");
        InputStream inputStream = multipartFile.getInputStream();
        OutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();

        return file;
    }
}
