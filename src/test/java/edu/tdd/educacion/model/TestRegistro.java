package edu.tdd.educacion.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tdd.educacion.excepciones.CredencialesInvalidasException;
import edu.tdd.educacion.excepciones.UsuarioYaExisteException;
import edu.tdd.educacion.services.UserService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class TestRegistro {
	
	@Autowired
	private UserService userService;
    
	@Test 
	@Order(1) 
	@DisplayName("Registro de pepe, pepe@pepe.com con contraseña pepe123")
	public void testRegistro() {
		try {
			User user = userService.register("pepe", "pepe@pepe.com", "pepe123");
		
			String pwdEncrypted = "3b048d415d60fad7f5681651e5fa27e93585041f9afc7aa6cde1b104a865f1517c682cb894a4c3b46d34b899978e6c1ec12c045c28e5202de06dd391dcc53b70";
			System.out.println(user.getPwd());
			assertTrue(user.getPwd().equals(pwdEncrypted));
		} catch (Exception e) {
			fail("No esperaba excepción");
		}
	}
	
	@Test 
	@Order(2) 
	@DisplayName("pepe se loguea bien con pepe123")
	public void testLoginValido() {
		try {
			User user = userService.login("pepe", "pepe123");
			assertNotNull(user);
		} catch (Exception e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	@DisplayName("pepe no puede entrar con Pepe123")
	public void testLoginInvalido() {
		try {
			User user = userService.login("pepe", "Pepe123");
			fail("Se esperaba CredencialesInvalidasException");
		}
		catch (CredencialesInvalidasException e) {
		}
		catch (Exception e) {
			fail("Se esperaba CredencialesInvalidasException, pero se ha lanzado " + e.getMessage());
		}
	}
	
	@Test 
	@Order(4)
	@DisplayName("jose no se puede registrar porque tiene el mismo email que el usuario pepe")
	public void testRegistroDuplicado() {
		try {
			User user = userService.register("jose", "pepe@pepe.com", "jose123");
			fail("Se esperaba UsuarioYaExisteException");
		}
		catch (UsuarioYaExisteException e) {
		}
		catch (Exception e) {
			fail("Se esperaba UsuarioYaExisteException, pero se ha lanzado " + e.getMessage());
		}
	}
}