package zawaski;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class User {
    private String username;
    private String passwordHash;
    private List<CharacterModel> characters;

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

    public List<CharacterModel> getCharacters() {
        return characters;
    }


    public boolean verifyPassword(String inputPassword) throws NoSuchAlgorithmException {
        return inputPassword.equals(this.passwordHash);
    }
    
    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean register() throws IOException, NoSuchAlgorithmException {
        Map<String, String> users = FileController.loadUsers();
        
        if (users.containsKey(username)) {
            return false;
        }
        
        users.put(username, passwordHash);
        FileController.saveUsers(users);
        
        return true;
    }

    public User login(String password) throws IOException, NoSuchAlgorithmException {
        Map<String, String> users = FileController.loadUsers();
        
        if (!users.containsKey(username)) {
            return null;
        }
        
        return this.verifyPassword(users.get(username)) ? this : null;
    }
    
    public boolean changeUsername(String newUsername) throws IOException, NoSuchAlgorithmException{
		Map<String, String> users = FileController.loadUsers();
        
		users.remove(this.username);
        users.put(newUsername, this.passwordHash);
        
        FileController.saveUsers(users);
        
        return true;
    }

    public boolean changePassword(String newPassword) throws IOException, NoSuchAlgorithmException {
        Map<String, String> users = FileController.loadUsers();
        
        users.put(username, this.encryptPassword(newPassword));
        
        FileController.saveUsers(users);
        
        return true;
    }

    public boolean deleteAccount() throws IOException {
        Map<String, String> users = FileController.loadUsers();
        
        if (!users.containsKey(username)) {
            return false;
        }
        
        users.remove(username);
        FileController.saveUsers(users);
        
        return true;
    }
}