package edu.tdd.educacion.model;

import io.github.benas.randombeans.api.Randomizer;

public class UserRandomizer implements Randomizer<RandomUser> {

	@Override
	public RandomUser getRandomValue() {
		return new RandomUser();
	}

}