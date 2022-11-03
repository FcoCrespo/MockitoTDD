package edu.tdd.educacion.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.tdd.educacion.dao.BarDAO;
import edu.tdd.educacion.dao.ProductoDAO;
import edu.tdd.educacion.model.Bar;
import edu.tdd.educacion.model.Menu;
import edu.tdd.educacion.model.Producto;

@Service
public class BarService {
	
	@Autowired
	private BarDAO barDAO;
	
	@Autowired
	private ProductoDAO productoDAO;

	public Menu getMenu(String barId) {
		Bar bar = barDAO.findById(barId).get();
		Menu menu = bar.getMenu();
		
		List<Producto> productos = this.productoDAO.findByMenuId(menu.getId());
		menu.setProductos(productos);
		return menu;
	}

	public void addProducto(String nombre, double precio, String barId) {
		Bar bar = barDAO.findById(barId).get();
		Menu menu = bar.getMenu();
		Producto producto = new Producto();
		producto.setNombre(nombre);
		producto.setPrecio(precio);
		producto.setMenu(menu);
		this.productoDAO.save(producto);
	}

	public List<Producto> getProductos(String barId) {
		return this.getMenu(barId).getProductos();
	}

	public double elegirProducto(List<String> comanda, String productoId) {
		comanda.add(productoId);
		return this.getTotal(comanda);
	}

	public double getTotal(List<String> comanda) {
		double total = 0;
		for (String id : comanda) {
			Producto producto = this.productoDAO.findById(id).get();
			total+= producto.getPrecio();
		}
		return total;		
	}
}