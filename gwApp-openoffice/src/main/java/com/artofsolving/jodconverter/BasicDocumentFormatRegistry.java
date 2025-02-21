package com.artofsolving.jodconverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * OpenOffice工具类，用于解决不兼容docx、xlsx、pptx转换pdf异常
 *
 * @author wy
 * @date 2023-06-02
 */
public class BasicDocumentFormatRegistry implements DocumentFormatRegistry {
    private List<DocumentFormat> documentFormats = new ArrayList();

    public void addDocumentFormat(DocumentFormat documentFormat) {
        documentFormats.add(documentFormat);
    }

    protected List<DocumentFormat> getDocumentFormats() {
        return documentFormats;
    }

    @Override
    public DocumentFormat getFormatByFileExtension(String extension) {
        if (extension == null) {
            return null;
        }
        new DefaultDocumentFormatRegistry();
        if (extension.contains("doc")) {
            extension = "doc";
        }
        if (extension.contains("ppt")) {
            extension = "ppt";
        }
        if (extension.contains("xls")) {
            extension = "xls";
        }
        String lowerExtension = extension.toLowerCase();
        for (Iterator it = documentFormats.iterator(); it.hasNext(); ) {
            DocumentFormat format = (DocumentFormat) it.next();
            if (format.getFileExtension().equals(lowerExtension)) {
                return format;
            }
        }
        return null;
    }

    @Override
    public DocumentFormat getFormatByMimeType(String mimeType) {
        for (Iterator it = documentFormats.iterator(); it.hasNext(); ) {
            DocumentFormat format = (DocumentFormat) it.next();
            if (format.getMimeType().equals(mimeType)) {
                return format;
            }
        }
        return null;
    }
}