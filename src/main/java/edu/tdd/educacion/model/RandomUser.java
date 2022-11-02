package edu.tdd.educacion.model;

import org.json.JSONObject;
import io.github.benas.randombeans.api.Randomizer;
import io.github.benas.randombeans.randomizers.text.StringRandomizer;

public class RandomUser extends User {
	private String pwdLimpia;
	private boolean valido;
	
	public RandomUser() {
		super();
		this.setName(StringRandomizer.
		aNewStringRandomizer(10).getRandomValue());
		this.setEmail(StringRandomizer.
		aNewStringRandomizer(10).getRandomValue()+"@"+
		StringRandomizer.aNewStringRandomizer(10).
		getRandomValue() + ".com");
		this.pwdLimpia = StringRandomizer.
		aNewStringRandomizer(10).getRandomValue();
		this.valido = this.pwdLimpia.length()>=4;
	}
	
	public boolean isValido() {
		return valido;
	}
	
	public JSONObject toRegistro() {
		return new JSONObject().
		put("userName", this.getName()).
		put("email", this.getEmail()).
		put("pwd1", this.pwdLimpia).
		put("pwd2", this.pwdLimpia);
	}
	
	public JSONObject toLogin() {
		return new JSONObject().
		put("userName", this.getName()).
		put("pwd", this.pwdLimpia);
	}
}