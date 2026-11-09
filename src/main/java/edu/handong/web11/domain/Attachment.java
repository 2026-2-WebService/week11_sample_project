package edu.handong.web11.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "attachments")
public class Attachment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @Column(nullable = false)
    private String originalFilename;
    @Column(nullable = false, unique = true)
    private String storedFilename;
    @Column(nullable = false)
    private String contentType;
    private long fileSize;

    protected Attachment() {}
    public Attachment(String originalFilename, String storedFilename, String contentType, long fileSize) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }
    public void setPost(Post post) { this.post = post; }
    public Long getId() { return id; }
    public Post getPost() { return post; }
    public String getOriginalFilename() { return originalFilename; }
    public String getStoredFilename() { return storedFilename; }
    public String getContentType() { return contentType; }
    public long getFileSize() { return fileSize; }
}
