package edu.tdd.educacion.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(
		name = "USERS",
		indexes = {
		@Index(unique = true, columnList = "email"),
		@Index(unique = true, columnList = "name"),
    })
public class User {
	
	@Id @Column(length = 36)
	private String id;
	private String name;
	private String email;
	private String pwd;
	
	public User() {
		this.id = UUID.randomUUID().toString();
	}

	public User(String name, String email, String pwd) {
		this();
		this.name = name;
		this.setEmail(email);
		this.setPwd(pwd);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

}