package edu.handong.web11.service;

import edu.handong.web11.domain.Attachment;
import edu.handong.web11.domain.Post;
import edu.handong.web11.dto.PostForm;
import edu.handong.web11.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository repository;
    private final FileStorageService storage;
    public PostService(PostRepository repository, FileStorageService storage) {
        this.repository = repository; this.storage = storage;
    }

    @Transactional(readOnly = true)
    public List<Post> all() { return repository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id")); }

    @Transactional(readOnly = true)
    public Post get(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다.")); }

    @Transactional
    public Long create(PostForm form, List<MultipartFile> files) {
        Post post = new Post(form.getTitle(), form.getContent());
        List<Attachment> createdFiles = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                Attachment item = storage.store(file);
                createdFiles.add(item);
                post.addAttachment(item);
            }
            return repository.saveAndFlush(post).getId();
        } catch (RuntimeException ex) {
            createdFiles.forEach(item -> {
                try { storage.deleteIfExists(item); } catch (RuntimeException ignored) {}
            });
            throw ex;
        }
    }

    @Transactional
    public void update(Long id, PostForm form) {
        get(id).update(form.getTitle(), form.getContent());
    }

    // Demo cleanup. Production systems need reliable coordination of database and object storage.
    @Transactional
    public void delete(Long id) {
        Post post = get(id);
        for (Attachment attachment : post.getAttachments()) storage.deleteIfExists(attachment);
        repository.delete(post);
    }
}
