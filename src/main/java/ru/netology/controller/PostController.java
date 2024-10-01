package ru.netology.controller;

import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        final var allPosts = service.all();
        try (var writer = response.getWriter()) {
            writer.write(allPosts.toString());
        }
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        final var post = service.getById(id);
        if (post != null) {  // Проверяем, найден ли пост
            response.setContentType("application/json");
            try (var writer = response.getWriter()) {
                writer.write(post.toString());  // Конвертируем пост в JSON или строку
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);  // Пост не найден
        }
    }

    public void save(BufferedReader reader, HttpServletResponse response) throws IOException {
        String content = reader.readLine();
        final var post = new Post(0, content);
        final var savedPost = service.save(post);
        response.setContentType("application/json");
        try (var writer = response.getWriter()) {
            writer.write(savedPost.toString());
        }
    }

    public void removeById(long id, HttpServletResponse response) {
        if (service.removeById(id)) {  // Если пост был найден и удалён
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);  // Возвращаем статус 204
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);  // Пост не найден
        }
    }

}
