package edu.tdd.educacion.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name = "PRODUCTOS")
public class Producto {
	@Id @Column(length = 36)
	private String id;
	private String nombre;
	private Double precio;
	
	@ManyToOne
	private Menu menu;
	
	public Producto() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@JsonIgnore
	public Menu getMenu() {
		return menu;
	}
	
}
