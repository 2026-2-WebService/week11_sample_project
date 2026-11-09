package edu.handong.web11.controller;

import edu.handong.web11.domain.Post;
import edu.handong.web11.dto.PostForm;
import edu.handong.web11.service.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class PostController {
    private final PostService posts;

    public PostController(PostService posts) {
        this.posts = posts;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String list(Model model) {
        model.addAttribute("posts", posts.all());
        return "posts/list";
    }

    @GetMapping("/posts/new")
    public String form(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "posts/form";
    }

    @PostMapping("/posts")
    public String create(@Valid @ModelAttribute("postForm") PostForm form, BindingResult errors,
                         @RequestParam(name = "files", required = false) List<MultipartFile> files) {
        if (errors.hasErrors()) return "posts/form";
        Long id = posts.create(form, files == null ? List.of() : files);
        return "redirect:/posts/" + id;
    }

    @GetMapping("/posts/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", posts.get(id));
        return "posts/detail";
    }

    @GetMapping("/posts/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Post post = posts.get(id);
        PostForm form = new PostForm();
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());
        model.addAttribute("postForm", form);
        model.addAttribute("postId", id);
        return "posts/form";
    }

    @PostMapping("/posts/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("postForm") PostForm form, BindingResult errors,
                         Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("postId", id);
            return "posts/form";
        }
        posts.update(id, form);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/delete")
    public String delete(@PathVariable Long id) {
        posts.delete(id);
        return "redirect:/posts";
    }
}
