package Expense.event.project.Service;

import Expense.event.project.Entity.User;
import Expense.event.project.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSerImpl implements Userser{

    @Autowired
    private UserRepo userRepo;
    @Override
    public List<User> getAllUsers() {
        List<User> allUsers=userRepo.findAll();
        return allUsers;
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User createUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public String authenticate(String name, String password) {
        User user = userRepo.findByName(name);
        if (user != null && user.getPassword().equals(password)) {
            return "success";
        }
        else {
            return "failure";
        }
    }

    @Override
    public String updateUser(Long id, User updatedUser) {
        User existingUser = userRepo.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setRole(updatedUser.getRole());
            userRepo.save(existingUser);
            return "User updated successfully.";
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        try {
            if (userRepo.existsById(id)) {
                userRepo.deleteById(id);
            }
            else {
                throw new RuntimeException("User not found");
            }
        }
        catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw new RuntimeException("Error deleting user"); // or a custom exception
            }
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
