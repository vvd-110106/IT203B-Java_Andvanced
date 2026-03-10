package Session03;

import java.util.List;
import java.util.stream.Collectors;

public class Ex6 {
    public record Post(String title, List<String> tags) {}
    public static void main(String[] args) {
        List<Post> posts = List.of(
                new Post("Java", List.of("java", "backend")),
                new Post("Python", List.of("python", "data"))
        );
        List<String> allTags = posts.stream().flatMap(post -> post.tags().stream()).toList();
        System.out.println(allTags);
    }
}