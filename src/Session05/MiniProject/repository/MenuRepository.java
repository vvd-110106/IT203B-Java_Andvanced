package Session05.MiniProject.repository;

import Session05.MiniProject.model.MenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuRepository {
    private List<MenuItem> items = new ArrayList<>();

    public void add(MenuItem item) {
        items.add(item);
    }

    public Optional<MenuItem> findById(String id) {
        return items.stream().filter(i -> i.getId().equals(id)).findFirst();
    }

    public void remove(String id) {
        items.removeIf(i -> i.getId().equals(id));
    }

    public List<MenuItem> getAll() {
        return items;
    }

    public List<MenuItem> searchByName(String name) {
        return items.stream()
                .filter(i -> i.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<MenuItem> filterByPrice(double min, double max) {
        return items.stream()
                .filter(i -> {
                    double p = i.calculatePrice();
                    return p >= min && p <= max;
                })
                .collect(Collectors.toList());
    }
}
