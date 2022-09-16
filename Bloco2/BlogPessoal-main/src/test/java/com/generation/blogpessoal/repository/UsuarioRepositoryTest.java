package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // indicar que a classe UsuarioRepositoryTest é uma classe de teste
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // indica que o teste a ser feito vai ser um teste unitario(por calsse)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll // inserindo usuarios no meu banco de dados h2, para que eu possa testar as funções de procurar um usuário por nome e por e-mail
	void start()
	{
		usuarioRepository.deleteAll();
		usuarioRepository.save(new Usuario(0L, "João da Silva", "joao@email.com.br", "12345678", "https://i.imgur.com/FETvs20.jpg"));
		usuarioRepository.save(new Usuario(0L, "Manuela da Silva", "manuela@email.com.br", "12345678", "https://i.imgur.com/NtyGneo.jpg"));
		usuarioRepository.save(new Usuario(0L, "Adriana da Silva", "adriana@email", "12345678", "https://i.imgur.com/mB3VM2N.jpg"));
		usuarioRepository.save(new Usuario(0L, "Paulo Antunes", "paulo@email.com.br", "12345678", "https://i.imgur.com/JR7kUFU.jpg"));
	}
	
	@Test // indica o inicio do teste
	@DisplayName("Retorna 1 usuario") // indica o nome do teste - OPCIONAL
	public void deveRetornarUmUsuario()
	{
		Optional <Usuario> usuario = usuarioRepository .findByUsuario("joao@email.com.br"); // estou passando o que espero receber da API buscando um usuario por seu email
		assertTrue(usuario.get().getUsuario().equals("joao@email.com.br")); // comparando se o que eu esperava receber, foi o que de fato o teste trouxe
	}
	
	@Test
	@DisplayName("Retorna 3 usuarios")
	public void deveRetornarTresUsuarios()
	{
		List <Usuario> listaDeUsuarios = usuarioRepository .findAllByNomeContainingIgnoreCase("Silva");
		assertEquals(3, listaDeUsuarios.size());
		
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João da Silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Manuela da Silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Adriana da Silva"));
	}
	
	@AfterAll
	public void end()
	{
		usuarioRepository.deleteAll();
	}

}
