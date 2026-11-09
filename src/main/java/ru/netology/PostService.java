
package ru.netology;

import java.util.List;

public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        return repository.all();
    }

    public Post getById(long id) {
        return repository.getById(id);
    }

    public void save(Post post) {
        repository.save(post);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}
