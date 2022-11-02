package edu.tdd.educacion.http;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import edu.tdd.educacion.services.BarService;

@RestController
@RequestMapping("payments")
public class PaymentsController {
	
	static {
		Stripe.apiKey = "sk_test_51IdXSlAa8oIZJkgAbOulAXQhreLsk3g1JKt4fdR5HxgVn3wt5vzRZBG1Gd6cmnp2FYyuMDeZETSh4gzwUd46wQIu00UWiOSgNT";
	}
	
	@Autowired
	private BarService barService;
	
	@GetMapping("/solicitarPreautorizacion")
	public String solicitarPreautorizacion(HttpSession session) {
		try {
			List<String> comanda = (List<String>) session.getAttribute("comanda");
			if (comanda==null)
				throw new Exception("No ha elegido ninguna comanda");			
			
			double total = this.barService.getTotal(comanda) * 100; // Ha de ir en céntimos
			
			PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
					.setCurrency("eur")
					.setAmount((long) total)
					.build();
			
			PaymentIntent intent = PaymentIntent.create(createParams);
			JSONObject jso = new JSONObject(intent.toJson());
			return jso.getString("client_secret");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}

