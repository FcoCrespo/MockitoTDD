package edu.tdd.educacion.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tdd.educacion.dao.BarDAO;
import edu.tdd.educacion.dao.MenuDAO;
import edu.tdd.educacion.dao.ProductoDAO;
import edu.tdd.educacion.services.BarService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ClienteTest {
	
	@Autowired
	private BarDAO barDAO;
	
	@Autowired
	private MenuDAO menuDAO;
	
	@Autowired
	private ProductoDAO productoDAO;
	
	@Autowired
	private BarService barService;
	
	@BeforeAll
	public void setUp() throws IOException {
		String bares = this.readTextFile("bares.txt");
		String[] datos = bares.split("\n");
		for (int i=0; i<datos.length; i++) {
			String[] dato = datos[i].split("\t");
			String nombre = dato[0];
			Bar bar = new Bar();
			bar.setId("" + (i+1) );
			bar.setNombre(nombre);
						
			Menu menu = new Menu();
			menu.setBar(bar);
			bar.setMenu(menu);
			
			this.menuDAO.save(menu);
			
			this.barDAO.save(bar);
			
			if (i<3)
				this.saveProductos(menu, i+1);
		}
	}
    
	private void saveProductos(Menu menu, int index) {
		try {
			String productos = this.readTextFile(index + ".txt");
			String[] datos = productos.split("\n");
			for (int i=0; i<datos.length; i++) {
				String[] dato = datos[i].split("\t");
				String nombre = dato[0];
				Double precio = Double.parseDouble(dato[1]);
				
				Producto producto = new Producto(); 
				producto.setId(index + "_" + (i+1));
				producto.setNombre(nombre);
				producto.setPrecio(precio);
				producto.setMenu(menu);
				
				this.productoDAO.save(producto);
			}
		} catch (IOException e) {
			System.out.println("El fichero " + index );
		}		
	}
	
	@AfterAll
	public void tearDown() {
		this.productoDAO.deleteAll();
		this.barDAO.deleteAll();
		this.menuDAO.deleteAll();
	}

	@ParameterizedTest
	@CsvSource({
		"1, 10",
		"2, 13",
		"3, 14",
		"4, 0",
		"5, 0"
	})
	@Order(1) 
	public void testGetMenu(String barId, int numeroDePlatos) {
		try {
			Menu menu = barService.getMenu(barId);
		
			assertTrue(menu.size()==numeroDePlatos);
		} catch (Exception e) {
			fail("No esperaba excepción");
		}
	}
	
	@Test @Order(2)
	public void testOctavio() {
		try {
			Menu menu = barService.getMenu("2");
		
			assertTrue(menu.get(12).getNombre().equals("Croquetas Melosas de Puchero"));
		} catch (Exception e) {
			fail("No esperaba excepción");
		}
	}
	
	private String readTextFile(String fileName) throws IOException {
		 ClassLoader classLoader = getClass().getClassLoader();
		 try (InputStream fis = classLoader.getResourceAsStream(fileName)) {
			byte[] b = new byte[fis.available()];
			fis.read(b);
			return new String(b);
		 }
	}
}