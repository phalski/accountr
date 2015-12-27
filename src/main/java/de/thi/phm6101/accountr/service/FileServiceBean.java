package de.thi.phm6101.accountr.service;

import javax.ejb.Stateless;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by philipp on 27/12/15.
 */
@Stateless
public class FileServiceBean {

    public static final String RECEIPT_FOLDER = "content/receipt";

    public String buildFileName(Part receiptImage) {
        String fileExtension = getFileExtension(getFileName(receiptImage).orElse(""));
        String fileName = UUID.randomUUID().toString();
        if (!fileExtension.isEmpty()) {
            fileName += "." + fileExtension;
        }
        return fileName;
    }

    public static Optional<String> getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return Optional.of(filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1)); // MSIE fix.
            }
        }
        return Optional.empty();
    }

    private static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        return (i > 0) ? fileName.substring(i+1) : "";
    }

    public void uploadFile(Part part, String fileName) throws IOException {
        InputStream inputStream = part.getInputStream();
        File file = new File(fileName);
        Files.copy(inputStream, file.toPath());

    }
}
