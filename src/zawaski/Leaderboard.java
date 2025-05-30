package zawaski;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Leaderboard {
    private List<LeaderboardEntry> entries;

    public Leaderboard() {
        entries = new ArrayList<>();
    }

    public List<LeaderboardEntry> getTopPlayers(int page, int size) {
        return entries.stream()
                .sorted(Comparator.comparingInt(LeaderboardEntry::getScore).reversed())
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public int getPlayerRank(String username) {
        List<LeaderboardEntry> sorted = entries.stream()
                .sorted(Comparator.comparingInt(LeaderboardEntry::getScore).reversed())
                .collect(Collectors.toList());
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).getUsername().equals(username)) {
                return i + 1;
            }
        }
        return -1; // Not found
    }

    public void addOrUpdateEntry(String username, int score) {
        for (LeaderboardEntry entry : entries) {
            if (entry.getUsername().equals(username)) {
                entry.setScore(score);
                return;
            }
        }
        entries.add(new LeaderboardEntry(username, score));
    }
}