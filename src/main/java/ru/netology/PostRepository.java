
package ru.netology;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {

    private final List<Post> posts = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public synchronized List<Post> all() {
        return new ArrayList<>(posts);
    }

    public synchronized Post getById(long id) {
        return posts.stream()
                .filter(post -> post.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized void save(Post post) {
        if (post.getId() == 0) {
            post.setId(nextId.getAndIncrement());
            posts.add(post);
            return;
        }

        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == post.getId()) {
                posts.set(i, post);
                return;
            }
        }

        posts.add(post);
        nextId.updateAndGet(current ->
                Math.max(current, post.getId() + 1));
    }

    public synchronized void removeById(long id) {
        posts.removeIf(post -> post.getId() == id);
    }
}