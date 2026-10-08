package com.example.Ece.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileControllerTest {
    @Test
    void keepsSimpleAndInternationalFilenames() {
        assertEquals("leaf.jpg", FileController.normalizeUploadName("leaf.jpg"));
        assertEquals("叶片检测.png", FileController.normalizeUploadName("叶片检测.png"));
    }

    @Test
    void discardsUntrustedDirectoryComponents() {
        assertEquals("leaf.jpg", FileController.normalizeUploadName("../../leaf.jpg"));
        assertEquals("leaf.jpg", FileController.normalizeUploadName("C:\\fakepath\\leaf.jpg"));
    }

    @Test
    void rejectsEmptyOrSuspiciousNames() {
        for (String name : new String[]{"", ".", "..", "/", "dir/", "a\nb.jpg"}) {
            ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                    () -> FileController.normalizeUploadName(name));
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void rejectsMissingAndOverlongNames() {
        assertThrows(ResponseStatusException.class, () -> FileController.normalizeUploadName(null));
        String overlong = String.join("", java.util.Collections.nCopies(181, "a"));
        assertThrows(ResponseStatusException.class, () -> FileController.normalizeUploadName(overlong));
    }
}
