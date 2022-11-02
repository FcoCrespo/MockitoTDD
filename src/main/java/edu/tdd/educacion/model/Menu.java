package edu.tdd.educacion.model;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name = "MENUS")
public class Menu {
	@Id @Column(length = 36)
	private String id;
	
    @OneToOne(mappedBy = "menu")
	private Bar bar;
    
    @Transient
    private List<Producto> productos;
    
    public Menu() {
    	this.id = UUID.randomUUID().toString();
    	this.productos = new ArrayList<>();
	}
   
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	public Bar getBar() {
		return bar;
	}

	public void setBar(Bar bar) {
		this.bar = bar;
	}
	
	public List<Producto> getProductos() {
		return productos;
	}
	
	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
	
	public int size() {
		return this.productos.size();
	}

	public Producto get(int index) {
		return this.productos.get(index);
	}

}
