package edu.tdd.educacion.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.tdd.educacion.model.Producto;

public interface ProductoDAO extends JpaRepository <Producto, String> {

	List<Producto> findByMenuId(String id);

}

