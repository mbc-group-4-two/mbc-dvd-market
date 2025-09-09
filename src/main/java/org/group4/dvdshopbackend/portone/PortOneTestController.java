package org.group4.dvdshopbackend.portone;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortOneTestController {

	@GetMapping("/port-one")
	public String pay() {
		return "PortOneTest.html";
	}
}
