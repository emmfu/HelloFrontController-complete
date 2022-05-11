package com.revature.services;

import java.util.List;

import com.revature.models.User;

public interface UserService {

	public User login(String username, String password);
	
	public int register(User user);
	
	public User findUserById(int id);
	
	public User findUserByName(String name);
	
	public List<User> findAllUsers();
	
	public boolean editUser(User user);
	
	public boolean deleteUser(User user);
}
