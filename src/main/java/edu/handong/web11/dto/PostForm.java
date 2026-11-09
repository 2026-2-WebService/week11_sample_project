package edu.handong.web11.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostForm {
    @NotBlank @Size(max = 120)
    private String title;
    @NotBlank
    private String content;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
