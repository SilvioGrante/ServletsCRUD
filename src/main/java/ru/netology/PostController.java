
package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class PostController {

    private final PostService service;
    private final Gson gson = new GsonBuilder().create();

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(gson.toJson(service.all()));
    }

    public void getById(long id, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        Post post = service.getById(id);

        if (post == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.getWriter().print(gson.toJson(post));
    }

    public void save(BufferedReader reader, HttpServletResponse resp) throws IOException {
        Post post = gson.fromJson(reader, Post.class);
        service.save(post);

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(gson.toJson(post));
    }

    public void removeById(long id, HttpServletResponse resp) throws IOException {
        service.removeById(id);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
