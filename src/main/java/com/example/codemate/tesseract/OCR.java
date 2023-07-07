package com.example.codemate.tesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OCR {
    private static Tesseract getTesseract() {
        Tesseract instance = new Tesseract();
        instance.setDatapath("C:\\intellij_project\\capstone_image");
        instance.setLanguage("eng+kor");//"kor+eng"
        return instance;
    }

    public static String ocrImage(String fileName) {

        Tesseract tesseract = getTesseract();


        String result = null;

        File file = new File("C:\\intellij_project\\capstone_image\\"+fileName);

        if(file.exists() && file.canRead()) {
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
