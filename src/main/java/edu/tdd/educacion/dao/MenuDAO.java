package edu.tdd.educacion.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tdd.educacion.model.Menu;

public interface MenuDAO extends JpaRepository <Menu, String> {

}