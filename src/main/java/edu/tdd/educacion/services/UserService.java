package edu.tdd.educacion.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import edu.tdd.educacion.dao.BarDAO;
import edu.tdd.educacion.dao.UserDAO;
import edu.tdd.educacion.excepciones.CredencialesInvalidasException;
import edu.tdd.educacion.excepciones.UsuarioYaExisteException;
import edu.tdd.educacion.model.Bar;
import edu.tdd.educacion.model.User;

@Service
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private BarDAO barDAO;
	
	public User register(String name, String email, String pwd) throws UsuarioYaExisteException {
		User user = new User(name, email, pwd);
		try {
			return userDAO.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new UsuarioYaExisteException();
		}
	}
	
	public User login(String name, String pwd) throws CredencialesInvalidasException {
		pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		User user = this.userDAO.findByNameAndPwd(name, pwd);
		if (user==null)
			throw new CredencialesInvalidasException();
		return user;
	}
	
	public Bar crearBar(String nombreBar, User user) {
		Bar bar = new Bar();
		bar.setNombre(nombreBar);
		bar.setUser(user);
		return this.barDAO.save(bar);
	}
	
}
