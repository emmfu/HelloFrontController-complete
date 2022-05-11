package com.revature.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.revature.models.User;
import com.revature.models.UserRole;
import com.revature.util.HibernateUtil;

public class UserDAOImpl implements UserDAO{
	private static Logger log = Logger.getLogger(UserDAOImpl.class);
	
	public int insertRole(UserRole role, Session ses, Transaction tx) {
		log.info("adding role to database. User role: " + role);
		
		int roleId = (int) ses.save(role);
		
		tx.commit();
		
		log.info("role has been saved as: " + roleId);
		return roleId;
	}
	
	public UserRole getUserRoleById(int id, Session ses) {
		log.info("get role by id. id: " + id);
		
		UserRole role = (UserRole) ses.createNativeQuery("SELECT * FROM store_userRoles WHERE store_userRole_id=" + id + "", UserRole.class).getSingleResult();
		
		return role;
	}
	
	@Override
	public int insert(User user) {
		log.info("adding user to database. user info: " + user);
		
		Session ses = HibernateUtil.getSession();
		
		Transaction tx = ses.beginTransaction();
		int roleId = 0;
		try {
			roleId = insertRole(user.getRole(), ses, tx);
		}catch(ConstraintViolationException e){
			log.info("This role already exist in database");
			tx.rollback();
			roleId = user.getRole().getRoleId();
		}
		//ses.clear();
		UserRole role = getUserRoleById(roleId, ses);
		Transaction tx2 = ses.beginTransaction();
		user.setRole(role);
		
		int pk = (int) ses.save(user);
		
		tx2.commit();
		log.info("Insert successful! New user id is " + pk);
		
		return pk; // return the auto-generated PK
	}

	@Override
	public User selectById(int id) {
		System.out.println("searching user by id: " + id);
		Session ses = HibernateUtil.getSession();
		User user = (User) ses.createNativeQuery("SELECT * FROM store_users WHERE store_user_id = " + id + "", User.class).getSingleResult();
		
		System.out.println("Search complete! Found: " + user);
		
		return user;
	}

	@Override
	public User selectByName(String name) {
		System.out.println("searching user by name: " + name);
		Session ses = HibernateUtil.getSession();
		User user = (User) ses.createNativeQuery("SELECT * FROM store_users WHERE store_user_firstname = '" + name + "'", User.class).getSingleResult();
		
		System.out.println("Search complete! Found: " + user);
		
		return user;
	}

	@Override
	public List<User> selectAll() {
		System.out.println("getting all users...");
		Session ses = HibernateUtil.getSession();
		
		List<User> userList = ses.createQuery("FROM User ORDER BY id", User.class).list();
		
		System.out.println("User list retrieval complete! Size: " + userList.size());
		
		return userList;
	}

	@Override
	public boolean update(User user) {
		System.out.println("Updating user. New user info: " + user);
		Session ses = HibernateUtil.getSession();
		Transaction tx = ses.beginTransaction();
		ses.clear();
		
		//ses.update(user);
		String sql = String.format("update store_users set store_user_username='%s', store_user_password='%s', store_user_firstname='%s', store_user_lastname='%s' where store_user_id=%d", user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getId());
		ses.createNativeQuery(sql, User.class).executeUpdate();
		tx.commit();
		
		System.out.println("Update complete!");
		
		return true;
	}

	@Override
	public boolean delete(User user) {
		System.out.println("Deleting user. Removed user info: " + user);
		Session ses = HibernateUtil.getSession();
		Transaction tx = ses.beginTransaction();
		ses.clear();
		ses.delete(user);
		
		tx.commit();
		
		System.out.println("Deletion complete!");
		
		return true;
	}

}
