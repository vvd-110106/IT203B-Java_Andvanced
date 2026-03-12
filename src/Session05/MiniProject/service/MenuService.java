package Session05.MiniProject.service;

import Session05.MiniProject.model.MenuItem;
import Session05.MiniProject.repository.MenuRepository;

import java.util.List;
import java.util.Optional;

public class MenuService {
    private MenuRepository repo = new MenuRepository();

    public void addItem(MenuItem item) {
        repo.add(item);
    }

    public void updateItem(String id, MenuItem newItem) {
        repo.findById(id).ifPresent(old -> {
            old.setPrice(newItem.getPrice());
            old.setStock(newItem.getStock());
            // name and id not changed in this simple model
        });
    }

    public void deleteItem(String id) {
        repo.remove(id);
    }

    public Optional<MenuItem> findById(String id) {
        return repo.findById(id);
    }

    public List<MenuItem> searchByName(String name) {
        return repo.searchByName(name);
    }

    public List<MenuItem> filterByPrice(double min, double max) {
        return repo.filterByPrice(min, max);
    }

    public List<MenuItem> getAll() {
        return repo.getAll();
    }
}
