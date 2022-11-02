package edu.tdd.educacion.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tdd.educacion.model.User;

public interface UserDAO extends JpaRepository <User, String> {

	User findByNameAndPwd(String name, String pwd);

	User findByName(String userName);

}