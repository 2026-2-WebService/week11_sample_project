package edu.handong.web11.controller;

import edu.handong.web11.domain.Attachment;
import edu.handong.web11.repository.AttachmentRepository;
import edu.handong.web11.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {
    private final AttachmentRepository attachments;
    private final FileStorageService storage;
    public AttachmentController(AttachmentRepository attachments, FileStorageService storage) {
        this.attachments = attachments; this.storage = storage;
    }
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Attachment file = attachments.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "첨부파일이 없습니다."));
        Resource resource = storage.load(file);
        String disposition = ContentDisposition.attachment()
            .filename(file.getOriginalFilename(), StandardCharsets.UTF_8)
            .build().toString();
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
            .body(resource);
    }
    // STUDENT TODO: GET /attachments/{id}/view (image preview).
}
