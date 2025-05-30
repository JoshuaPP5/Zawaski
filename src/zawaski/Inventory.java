package zawaski;

import java.util.ArrayList;
import java.util.List;

public class Inventory<T> {
    private List<T> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void addItem(T item) {
        items.add(item);
    }

    public void removeItem(T item) {
        items.remove(item);
    }

    public List<T> getItems() {
        return new ArrayList<>(items);
    }
}