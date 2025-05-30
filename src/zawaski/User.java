package zawaski;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class User {
    private String username;
    private String passwordHash;
    private List<Character> characters;

    private static final String USER_DATA_FILE = "users.dat";

    // Constructor: takes username and plain-text password, stores hashed password
    public User(String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        this.passwordHash = encryptPassword(password);
        this.characters = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    // Encrypt password using SHA-256
    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    // Helper method to convert byte array to hex string
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Register user: save to file if username not taken
    public boolean register() throws IOException, NoSuchAlgorithmException {
        Map<String, String> users = loadUsers();
        if (users.containsKey(username)) {
            return false; // Username already exists
        }
        users.put(username, passwordHash);
        saveUsers(users);
        return true;
    }

    // Login user: verify username and password hash
    public boolean login(String password) throws IOException, NoSuchAlgorithmException {
        Map<String, String> users = loadUsers();
        if (!users.containsKey(username)) {
            return false; // Username not found
        }
        String hashedInput = encryptPassword(password);
        return users.get(username).equals(hashedInput);
    }

    // Change username: update key in file storage
    public boolean changeUsername(String newUsername) throws IOException {
        Map<String, String> users = loadUsers();
        if (!users.containsKey(username) || users.containsKey(newUsername)) {
            return false; // Current username not found or new username taken
        }
        String passHash = users.remove(username);
        users.put(newUsername, passHash);
        saveUsers(users);
        this.username = newUsername;
        return true;
    }

    // Change password: update password hash in file storage
    public boolean changePassword(String newPassword) throws IOException, NoSuchAlgorithmException {
        Map<String, String> users = loadUsers();
        if (!users.containsKey(username)) {
            return false; // Username not found
        }
        String newHash = encryptPassword(newPassword);
        users.put(username, newHash);
        saveUsers(users);
        this.passwordHash = newHash;
        return true;
    }

    // Delete account: remove user from file storage
    public boolean deleteAccount() throws IOException {
        Map<String, String> users = loadUsers();
        if (!users.containsKey(username)) {
            return false; // Username not found
        }
        users.remove(username);
        saveUsers(users);
        return true;
    }

    // Load all users from file into a Map<username, passwordHash>
    private Map<String, String> loadUsers() throws IOException {
        Map<String, String> users = new HashMap<>();
        Path path = Paths.get(USER_DATA_FILE);
        if (!Files.exists(path)) {
            // If file doesn't exist, return empty map
            return users;
        }
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                users.put(parts[0], parts[1]);
            }
        }
        return users;
    }

    // Save all users from Map<username, passwordHash> to file
    private void saveUsers(Map<String, String> users) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, String> entry : users.entrySet()) {
            lines.add(entry.getKey() + ":" + entry.getValue());
        }
        Path path = Paths.get(USER_DATA_FILE);
        Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}