package edu.tdd.educacion.model;


import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import io.github.benas.randombeans.randomizers.text.StringRandomizer;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class TestClienteDatosAleatorios {
	
	@Autowired
	private MockMvc servidor;
	
	private JSONArray jsaUsersValidos, jsaUsersInvalidos;
	
	@BeforeAll
	void setUp() {
		this.jsaUsersValidos = new JSONArray();
		this.jsaUsersInvalidos = new JSONArray();
	}
	
	@Order(1)
	@RepeatedTest(20)
	void registrar() throws Exception {
		
		String userName = StringRandomizer.aNewStringRandomizer(10).getRandomValue();
		String email = StringRandomizer.aNewStringRandomizer(10).getRandomValue() +
		"@" + StringRandomizer.aNewStringRandomizer(10).getRandomValue() + ".com";
		String pwd = StringRandomizer.aNewStringRandomizer(10).getRandomValue();
		JSONObject jsoPepe = new JSONObject().
		put("userName", userName).
		put("email", email).
		put("pwd1", pwd).
		put("pwd2", pwd);
		RequestBuilder request = MockMvcRequestBuilders.
		put("/user/register").
		contentType("application/json").
		content(jsoPepe.toString());
		
		assumingThat(pwd.length()>=4, () -> {
			servidor.perform(request).andExpect(status().isOk());
		});
		
		assumingThat(pwd.length()<4, () -> {
			servidor.perform(request).andExpect(status().is4xxClientError());
		});
		
	}
	
	@Test @Order(2)
	void login() throws Exception {
		
		JSONObject currentUser;
		
		for (int i=0; i<this.jsaUsersValidos.length(); i++) {
			currentUser = this.jsaUsersValidos.getJSONObject(i);
			currentUser.put("pwd", currentUser.getString("pwd1"));
			currentUser.remove("pwd1");
			currentUser.remove("pwd2");
			RequestBuilder request = MockMvcRequestBuilders.
			post("/user/login").
			contentType("application/json").
			content(currentUser.toString());
			servidor.perform(request).andExpect(status().isOk());
		}
		
		for (int i=0; i<this.jsaUsersInvalidos.length(); i++) {
			currentUser = this.jsaUsersInvalidos.getJSONObject(i);
			currentUser.put("pwd", currentUser.getString("pwd1"));
			currentUser.remove("pwd1");
			currentUser.remove("pwd2");
			RequestBuilder request = MockMvcRequestBuilders.
			post("/user/login").
			contentType("application/json").
			content(currentUser.toString());
			servidor.perform(request).andExpect(status().is4xxClientError());
		}
	}
	

}
