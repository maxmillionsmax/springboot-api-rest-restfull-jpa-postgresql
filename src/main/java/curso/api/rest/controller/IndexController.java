package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

@CrossOrigin // libera requisições de qualquer lugar
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping(value = "/{id}/codigovenda/{venda}", produces = "application/json")
	public ResponseEntity<Usuario> relatorio(@PathVariable(value = "id") Long id,
			@PathVariable(value = "venda") Long venda) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);

	}
	
	@GetMapping(value = "/{id}", produces = "application/json")
	@CacheEvict(value = "cacheuser", allEntries = true)
	@CachePut("cacheuser")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
		
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public String delete(@PathVariable("id") Long id) {
		
		usuarioRepository.deleteById(id);
		
		return "Deletado";
	}
	
	@GetMapping(value = "/", produces = "application/json")
	@CacheEvict(value = "cacheusuarios", allEntries = true)
	@CachePut("cacheusuarios")
	public ResponseEntity<List<Usuario>> usuario() throws InterruptedException {

		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);

	}

	@DeleteMapping(value = "/{id}/venda", produces = "application/json")
	public String deletevenda(@PathVariable("id") Long id) {

		usuarioRepository.deleteById(id);

		return "Deletado venda";

	}

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}

		String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhacriptografada);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value = "/{iduser}/idvenda/{idvenda}", produces = "application/json")
	public ResponseEntity cadastrarvenda(@PathVariable Long iduser, @PathVariable Long idvenda) {

		return new ResponseEntity("id user: " + iduser + " idvenda " + idvenda, HttpStatus.OK);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping(value = "/{iduser}/idvenda/{idvenda}", produces = "application/json")
	public ResponseEntity updateVenda(@PathVariable Long iduser, @PathVariable Long idvenda) {

		return new ResponseEntity("venda atualizada", HttpStatus.OK);

	}

	@SuppressWarnings("rawtypes")
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity atualizar(@RequestBody Usuario usuario) {

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}

		Usuario userTemporario = usuarioRepository.findUserByLogin(usuario.getLogin());

		if (!userTemporario.getSenha().equals(usuario.getSenha())) { /* Senhas diferentes */
			String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhacriptografada);
		}

		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

}