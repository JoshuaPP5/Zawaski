package zawaski;

import java.util.*;
import java.util.function.Function;

public class LeaderboardModel<T> {
    private List<T> items;
    private String currentSort;
    private final Map<String, Comparator<T>> sortMethods;
    private final int pageSize;

    public LeaderboardModel(List<T> items, int pageSize) {
        this.items = new ArrayList<>(items);
        this.pageSize = pageSize;
        this.sortMethods = new HashMap<>();
    }

    // Add sorting method
    public void addSortMethod(String key, Comparator<T> comparator) {
        sortMethods.put(key, comparator);
        if (currentSort == null) {
            currentSort = key; // Set first added as default
        }
    }

    // Get sorted and paginated results
    public List<T> getPage(int page) {
        if (currentSort == null || !sortMethods.containsKey(currentSort)) {
            throw new IllegalStateException("No valid sort method configured");
        }

        List<T> sorted = new ArrayList<>(items);
        sorted.sort(sortMethods.get(currentSort));

        int start = page * pageSize;
        if (start >= sorted.size()) return Collections.emptyList();

        int end = Math.min(start + pageSize, sorted.size());
        return sorted.subList(start, end);
    }

    // Change sorting
    public void setSort(String sortKey) {
        if (!sortMethods.containsKey(sortKey)) {
            throw new IllegalArgumentException("Unknown sort key: " + sortKey);
        }
        currentSort = sortKey;
    }

    public void setItems(List<T> items) {
        this.items = new ArrayList<>(items);
    }
    
    // Helper to create comparators
    public static <T, U extends Comparable<U>> Comparator<T> comparing(
        Function<T, U> extractor, boolean descending) {
        Comparator<T> comp = Comparator.comparing(extractor);
        return descending ? comp.reversed() : comp;
    }
}