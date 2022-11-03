package edu.tdd.educacion.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tdd.educacion.model.Producto;
import edu.tdd.educacion.services.BarService;

@RestController
@RequestMapping("bar")
public class BarController {
	
	@Autowired
	private BarService barService;
	
	@PutMapping("/addProducto")
	public void addProducto(HttpSession session, @RequestBody Map<String, Object> info) {
		String barId = session.getAttribute("barId").toString();
		JSONObject jso = new JSONObject(info);
		barService.addProducto(jso.getString("nombre"), jso.getDouble("precio"), barId);
	}
	
	@GetMapping("/getProductos/{barId}")
	public List<Producto> getProductos(@PathVariable String barId) {
		return this.barService.getProductos(barId);
	}
	
	@PutMapping("/elegirProducto/{productoId}")
	public double elegirProducto(HttpSession session, @PathVariable String productoId) {
		List<String> comanda = (List<String>) session.getAttribute("comanda");
		if (comanda==null) {
			comanda = new ArrayList<>();
			session.setAttribute("comanda", comanda);
		}
		return this.barService.elegirProducto(comanda, productoId);
	}
}
