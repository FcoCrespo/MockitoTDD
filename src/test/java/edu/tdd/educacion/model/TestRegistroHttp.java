package edu.tdd.educacion.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.tdd.educacion.dao.BarDAO;
import edu.tdd.educacion.dao.UserDAO;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class TestRegistroHttp {
	
	@Autowired
	private MockMvc servidor;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private BarDAO barDAO;


	private HttpSession session;

	private HttpSession sessionCliente;
	
	@Test @Order(1)
	void registrar() throws Exception {
		JSONObject jsoPepe = new JSONObject().
				put("userName", "pepe").
				put("email", "pepe@pepe.com").
				put("pwd1", "pepe123").
				put("pwd2", "pepe123");
		
		RequestBuilder request = MockMvcRequestBuilders.
				put("/user/register").
				contentType("application/json").
				content(jsoPepe.toString());
		
		servidor.perform(request).andExpect(status().isOk());
		
		JSONObject jsoJose = new JSONObject().
				put("userName", "jose").
				put("email", "pepe@pepe.com").
				put("pwd1", "pepe123").
				put("pwd2", "pepe123");
		
		request = MockMvcRequestBuilders.
				put("/user/register").
				contentType("application/json").
				content(jsoJose.toString());
		
		servidor.perform(request).andExpect(status().is4xxClientError());
	}
	
	
	@Test @Order(2)
	void login() throws Exception {
		JSONObject jsoPepe = new JSONObject().
			put("userName", "pepe").
			put("pwd", "pepe123");
		
		RequestBuilder request = MockMvcRequestBuilders.
				post("/user/login").
				contentType("application/json").
				content(jsoPepe.toString());
		
		MvcResult result = servidor.perform(request).
				andExpect(status().isOk()).
				andReturn();
		
		this.session = result.getRequest().getSession();
	}
	
	@Order(3) @Test
	void crearBar() throws Exception {
		JSONObject jsoBarPepe = new JSONObject().
			put("nombre", "Bar Pepe");
		
		RequestBuilder request = MockMvcRequestBuilders.
				put("/user/crearBar").
				contentType("application/json").
				content(jsoBarPepe.toString()).
				session((MockHttpSession) this.session);
		
		servidor.perform(request).andExpect(status().isOk());
	}
	
	@Order(4) @Test
	void checkUser() {
		User user = userDAO.findByName("pepe");
		List<Bar> bares = this.barDAO.findByUserId(user.getId());
		
		assertTrue(bares.size()==1);
		
		Bar bar = bares.get(0);
		assertTrue(bar.getUser().getId().equals(user.getId()));
	}
	
	@Order(5)
	@ParameterizedTest
	@CsvFileSource(
		delimiter = '\t',
		resources = "/1.txt"		
	)
	void addProductos(String nombre, String precio) throws Exception {
		JSONObject jsoProducto = new JSONObject().
				put("nombre", nombre).
				put("precio", Double.parseDouble(precio));
		
		RequestBuilder request = MockMvcRequestBuilders.
				put("/bar/addProducto").
				contentType("application/json").
				content(jsoProducto.toString()).
				session((MockHttpSession) this.session);
		
		servidor.perform(request).andExpect(status().isOk());
	}
	
	@Order(6)
	@Test
	void crearComanda() throws Exception {
		Bar bar = this.barDAO.findByNombre("Bar Pepe");
		String barId = bar.getId();
		
		RequestBuilder request = MockMvcRequestBuilders.
				get("/bar/getProductos/" + barId);
		
		MvcResult result = servidor.perform(request).andExpect(status().isOk()).andReturn();
		this.sessionCliente = result.getRequest().getSession();
		MockHttpServletResponse response = result.getResponse();
		String payload = response.getContentAsString();
		JSONArray jsa = new JSONArray(payload);
		
		assertEquals(10, jsa.length());
		
		/* En este bucle elegimos una unidad de cada producto ofertado por este bar
		 * llamando a getProductos/ID_PRODUCTO.
		 * El servicio getProductos nos debe devolver el precio total de la comanda,
		 * cosa que comprobamos en el assertEquals que ponemos al final del bucle. 
		 */
		double precioTotalEsperado = 0;
		JSONObject jsoProducto;
		for (int i=0; i<jsa.length(); i++) {
			jsoProducto = jsa.getJSONObject(i);
			precioTotalEsperado = precioTotalEsperado + jsoProducto.getDouble("precio");
			request = MockMvcRequestBuilders.
					put("/bar/elegirProducto/" + jsoProducto.getString("id")).
					session((MockHttpSession) this.sessionCliente);
			result = servidor.perform(request).andExpect(status().isOk()).andReturn();
			response = result.getResponse();
			payload = response.getContentAsString();
			
			double precioTotalObtenido = Double.parseDouble(payload);
			assertEquals(precioTotalEsperado, precioTotalObtenido);
		}
	}
	
	@Order(7)
	@Test
	void solicitarPreautorizacion() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.
				get("/payments/solicitarPreautorizacion").
				session((MockHttpSession) this.sessionCliente);
		
		MvcResult result = servidor.perform(request).andExpect(status().isOk()).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String payload = response.getContentAsString();
		assertTrue(payload.length()>0);
	}
	
}