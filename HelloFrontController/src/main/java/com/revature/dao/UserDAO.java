package com.revature.dao;

import java.util.List;

import com.revature.models.User;

public interface UserDAO {
	//Basic CRUD operations
	public int insert(User user);
	
	public User selectById(int id);
	
	public User selectByName(String name);
	
	public List<User> selectAll();
	
	public boolean update(User user);
	
	public boolean delete(User user);
}
