
package ru.netology;

import java.util.List;

public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public List<Post> all() {
        return service.all();
    }

    public Post getById(long id) {
        return service.getById(id);
    }

    public void save(Post post) {
        service.save(post);
    }

    public void removeById(long id) {
        service.removeById(id);
    }
}

