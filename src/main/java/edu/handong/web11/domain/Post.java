package edu.handong.web11.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Attachment> attachments = new ArrayList<>();

    protected Post() {
    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.setPost(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }
}
