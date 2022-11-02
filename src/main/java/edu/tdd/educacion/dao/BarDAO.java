package edu.tdd.educacion.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tdd.educacion.model.Bar;

public interface BarDAO extends JpaRepository <Bar, String> {

	List<Bar> findByUserId(String id);

	Bar findByNombre(String nombre);

}
