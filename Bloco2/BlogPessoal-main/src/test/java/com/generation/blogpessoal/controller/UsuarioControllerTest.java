package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate; // serve para ter acesso aos verbos HTTP (get post put delete) em modo teste
	
	@Autowired
	private UsuarioService usuarioService; // serve para conseguirmos usar as funções de serviço do usuario
	
	@Autowired
	private UsuarioRepository usuarioRepository; // serve para o acesso no banco de dados h2
	
	@BeforeAll
	void start() // antes de começar o teste, limpa o banco de dados h2 e cadastra um usuario no banco
	{
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Root", "root@root.com", "rootroot", " "));
	}
	
	@Test // indica que o codigo abaixo será um teste
	@DisplayName("Cadastrar Um Usuário") // indica um nome de exibição para o teste no console do Junit
	public void deveCriarUmUsuario()
	{
		// define o que eu estou mandando para a minha api enviar para o banco de dadosdo que foi "persistido" no banco de dados h2
		HttpEntity <Usuario> corpoRequisicao = new HttpEntity <Usuario> (new Usuario(0L, "Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg"));
		
		// define o que eu vou obter como resposta
		ResponseEntity <Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		// faço a verificação se o status http da resposta foi igual a 201 created
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		
		// faço a verificação se o que mandei de nome do usuario foi o que efetivamente salvo no banco de dados
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		
		// faço a verificação se o que mandei de email do usuario foi o que efetivamente salvo no banco de dados
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDevePermitirDuplicarUsuário()
	{
		usuarioService.cadastrarUsuario(new Usuario(0L, "Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));
		
		HttpEntity <Usuario> corpoRequisicao = new HttpEntity <Usuario> (new Usuario(0L, "Maria da Silva", "maria_silva@email.com.br", "13465278", "https/i.imgur.com/T12NIp9.jpg"));
		
		ResponseEntity <Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario()
	{
		Optional <Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "https//i.imgur.com/yDRVek7.jpg"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123", "https//i.imgur.com/yDRVek7.jpg");
		
		HttpEntity <Usuario> corpoRequisicao = new HttpEntity <Usuario> (usuarioUpdate);
		
		ResponseEntity <Usuario> corpoResposta = testRestTemplate.withBasicAuth("root@root.com", "rootroot").exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosOsUsuarios()
	{
		// 
		usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "https//i.imgur.com/5M2p5Wb.jpg"));
		usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "https//i.imgur.com/Sk5SjWE.jpg"));
		
		ResponseEntity <String> resposta = testRestTemplate.withBasicAuth("root@root.com", "rootroot").exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

}
