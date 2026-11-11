
package ru.netology;

import com.google.gson.Gson;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    private AnnotationConfigApplicationContext context;
    private PostController controller;

    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        controller = context.getBean(PostController.class);
    }

    @Override
    public void destroy() {
        if (context != null) {
            context.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = getPath(req);

        if (path.equals("/api/posts") || path.equals("/api/posts/")) {
            resp.getWriter().write(gson.toJson(controller.all()));
            return;
        }

        if (!path.startsWith("/api/posts/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            long id = Long.parseLong(
                    path.substring("/api/posts/".length())
            );

            Post post = controller.getById(id);

            if (post == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            resp.getWriter().write(gson.toJson(post));

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = getPath(req);

        if (!path.equals("/api/posts") && !path.equals("/api/posts/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Post post = gson.fromJson(req.getReader(), Post.class);

        if (post == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean isNew = post.getId() == 0;

        controller.save(post);

        resp.setStatus(isNew
                ? HttpServletResponse.SC_CREATED
                : HttpServletResponse.SC_OK);

        resp.getWriter().write(gson.toJson(post));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String path = getPath(req);

        if (!path.startsWith("/api/posts/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long id = Long.parseLong(
                    path.substring("/api/posts/".length())
            );

            controller.removeById(id);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(
                req.getContextPath().length()
        );
    }
}
