package curso.api.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity init(@RequestParam (value = "nome", required = true, defaultValue = "Nome não informado") String nome,
			@RequestParam("salario") Long salario ) {
		System.out.println("paramento sendo recebido " +nome );
		return new ResponseEntity("Olá Usuário REST Spring Boot " +nome+", Salario é  "+ salario, HttpStatus.OK);
	}

}