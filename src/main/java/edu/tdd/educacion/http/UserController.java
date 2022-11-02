package edu.tdd.educacion.http;


import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.tdd.educacion.model.Bar;
import edu.tdd.educacion.model.User;
import edu.tdd.educacion.services.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PutMapping("/crearBar")
	public void crearBar(HttpSession session, @RequestBody Map<String, Object> info) {
		User user = (User) session.getAttribute("user");
		if (user==null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Debes indentificarte primero");
		JSONObject jso = new JSONObject(info);
		Bar bar = userService.crearBar(jso.getString("nombre"), user);
		session.setAttribute("barId", bar.getId());
	}
	
	@PostMapping("/login")
	public void login(HttpSession session, @RequestBody Map<String, Object> credenciales) {
		JSONObject jso = new JSONObject(credenciales);
		String userName = jso.optString("userName");
		String pwd = jso.optString("pwd");
		
		try {
			User user = userService.login(userName, pwd);
			session.setAttribute("user", user);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	

	@PutMapping("/register")
	public void register(@RequestBody Map<String, Object> credenciales) {
		JSONObject jso = new JSONObject(credenciales);
		String userName = jso.optString("userName");
		String email = jso.optString("email");
		String pwd1 = jso.optString("pwd1");
		String pwd2 = jso.optString("pwd2");
		if (!pwd1.equals(pwd2))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Error: las contraseñas no coinciden");
		if (pwd1.length()<4)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Error: la contraseña debe tener al menos cuatro caracteres");
		
		try {
			userService.register(userName, email, pwd1);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
