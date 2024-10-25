package Expense.event.project.Service;

import Expense.event.project.Entity.User;

import java.util.List;

public interface Userser {
    List<User> getAllUsers();
    User getUserById(Long id);
    User createUser(User user);
    String authenticate(String name, String password);
    String updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    User findByEmail(String email);
}
