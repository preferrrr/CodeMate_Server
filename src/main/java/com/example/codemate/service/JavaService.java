package com.example.codemate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Service
public class JavaService {

    public String compile(MultipartFile file) throws IOException {

        //String javaFilePath = "C:/javatest/";
        String javaFilePath = "/home/ubuntu/JavaFiles/";
        String fullPath = javaFilePath + file.getOriginalFilename();

        try {
            Path destination = new File(fullPath).toPath();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String output = "";
        int compileResult = compiler.run(null, null, null, fullPath);
        if (compileResult == 0) {
            output = executeJavaFile(javaFilePath, file.getOriginalFilename());
        } else {
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(fullPath));
            StringWriter stringWriter = new StringWriter();
            JavaCompiler.CompilationTask task = compiler.getTask(stringWriter, fileManager, diagnostics, null, null, compilationUnits);
            task.call();

            StringBuilder errorMessage = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                errorMessage.append(diagnostic).append(System.lineSeparator());
            }

            output = errorMessage.toString();
            output = removeWordFromString(output, "error: ");

            fileManager.close();
        }

        return output;
    }

    private static String executeJavaFile(String javaFilePath, String filename) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", javaFilePath, filename.substring(0, filename.length() - 5));
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();

            return output.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Execution failed: " + e.getMessage();
        }
    }

    public static String removeWordFromString(String input, String word) {
        int index = input.indexOf(word);
        if (index != -1) {
            return input.substring(index + word.length()).trim();
        }
        return input;
    }
}
