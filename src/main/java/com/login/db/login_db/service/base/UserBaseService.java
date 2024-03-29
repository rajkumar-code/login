/* 
* Generated by
* 
*      _____ _          __  __      _     _
*     / ____| |        / _|/ _|    | |   | |
*    | (___ | | ____ _| |_| |_ ___ | | __| | ___ _ __
*     \___ \| |/ / _` |  _|  _/ _ \| |/ _` |/ _ \ '__|
*     ____) |   < (_| | | | || (_) | | (_| |  __/ |
*    |_____/|_|\_\__,_|_| |_| \___/|_|\__,_|\___|_|
*
* The code generator that works in many programming languages
*
*			https://www.skaffolder.com
*
*
* You can generate the code from the command-line
*       https://npmjs.com/package/skaffolder-cli
*
*       npm install -g skaffodler-cli
*
*   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *
*
* To remove this comment please upgrade your plan here: 
*      https://app.skaffolder.com/#!/upgrade
*
* Or get up to 70% discount sharing your unique link:
*       https://app.skaffolder.com/#!/register?friend=5dc9035ef1ef4518a538176b
*
* You will get 10% discount for each one of your friends
* 
*/
package com.login.db.login_db.service.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import com.login.db.login_db.entity.User;
import com.login.db.login_db.service.UserService;

//IMPORT RELATIONS
import com.login.db.login_db.entity.User;

@Service
public class UserBaseService {

	private static NamedParameterJdbcTemplate jdbcTemplate;
	
		@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	


    //CRUD METHODS
    
    //CRUD - CREATE
    	
	public User insert(User obj) {
		Long id = jdbcTemplate.queryForObject("select max(_id) from `user`", new MapSqlParameterSource(), Long.class);
		obj.set_id(id == null ? 1 : id + 1);
		String sql = "INSERT INTO `user` (`_id`, `123`) VALUES (:id,:123)";
			SqlParameterSource parameters = new MapSqlParameterSource()
		    .addValue("id", obj.get_id())
			.addValue("123", obj.get123());
		
		jdbcTemplate.update(sql, parameters);
		return obj;
	}
	
    	
    //CRUD - REMOVE
    
	public void delete(Long id) {
		String sql = "DELETE FROM `user` WHERE `_id`=:id";
		SqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("id", id);
		
		String sql_log = "DELETE FROM `user_log` WHERE `id_user`= :id";
		jdbcTemplate.update(sql_log, parameters);
		jdbcTemplate.update(sql, parameters);
	}

    	
    //CRUD - GET ONE
    	
	public User get(Long id) {
	    
		String sql = "select * from `user` where `_id` = :id";
		
	    SqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("id", id);
	    
	    return (User) jdbcTemplate.queryForObject(sql, parameters, new BeanPropertyRowMapper(User.class));
	}


    	
        	
    //CRUD - GET LIST
    	
	public List<User> getList() {
	    
		String sql = "select * from `user`";
		
	    SqlParameterSource parameters = new MapSqlParameterSource();
	    return jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper(User.class));
	    
	    
	}

    	
    //CRUD - EDIT
    	
	public User update(User obj, Long id) {

		String sql = "UPDATE `user` SET `123` = :123  WHERE `_id`=:id";
		SqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("id", id)
			.addValue("123", obj.get123());
		jdbcTemplate.update(sql, parameters);
	    return obj;
	}
	
    	
    
    
    
    
    // External relation m:m log
    public static class User_logService {

    	public static ArrayList<Long> findBy_user(Long id_user) {
    		String sql = "select `id_User` from user_log WHERE `id_user`=:id_user";
    		SqlParameterSource parameters = new MapSqlParameterSource()
    			.addValue("id_user", id_user);
    	    
    	    List<Long> listId = jdbcTemplate.queryForList(sql, parameters, Long.class);
    		return (ArrayList<Long>) listId;
    	}
    	

    	public static void updateRelation(Long id_user, ArrayList<Long> log) {

    		// Delete not in array
    		String in = " and `id_User` NOT IN (:log)";
    		String sql = "DELETE FROM user_log WHERE `id_user`=:id_user ";
    		
    		if (log != null && log.size() > 0)
    			sql = sql + in;
    			
    		SqlParameterSource parameters = new MapSqlParameterSource()
    			.addValue("id_user", id_user)
    			.addValue("log", log);
    		
    		jdbcTemplate.update(sql, parameters);
    		
    		// Get actual    		
    	    List<Long> actual = UserService.User_logService.findBy_user(id_user);
    	    
    		// Insert new
    		for (Long id_User : log) {
    			if (actual.indexOf(id_User) == -1){
    				UserService.User_logService.insert(id_user, id_User);
    			}
    		}
    		
    	}
    	

    	public static void insert(Long id_user, Long id_User) {
    		User.User_log obj = new User.User_log();
			Long id = jdbcTemplate.queryForObject("select max(_id) FROM user_log", new MapSqlParameterSource(), Long.class);
			obj.set_id(id == null ? 1 : id + 1);
			
			String sql = "INSERT INTO user_log (`_id`, `id_user`, `id_User` )	VALUES (:id, :id_user, :id_User)";

			MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("id", obj.get_id())
				.addValue("id_user", id_user)
				.addValue("id_User", id_User);

			jdbcTemplate.update(sql, parameters);
    	}

    }
	

    
    /*
     * CUSTOM SERVICES
     * 
     *	These services will be overwritten and implemented in userService.java
     */
    

}
