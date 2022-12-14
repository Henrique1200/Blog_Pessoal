package com.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Optional <Usuario> cadastrarUsuario(Usuario usuario) // função que verifica se o usuario já está cadastrado, criptografa a senha, e manda o obj de usuario para o banco de dados
	{
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) // verifica se o usuario já está cadastrado no banco de dados
				return Optional.empty();
		
		usuario.setSenha(criptografarSenha(usuario.getSenha())); // criptografa a senha digitada pelo usuario antes de mandar o objeto para o banco
		return Optional.of(usuarioRepository.save(usuario)); // manda o objeto de usuario para o banco de dados com a senha criptografada
	}
	
	 public Optional<Usuario> atualizarUsuario(Usuario usuario) // // função que atualiza um usuario já cadastrado
	 {
	        if(usuarioRepository.findById(usuario.getId()).isPresent()) // // função que atualiza um usuario já cadastrado
	        {
	            Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario()); // depois verifica pelo email digitado do usuario se ele já está cadastrado no meu banco de dados

	            if ((buscaUsuario.isPresent()) && ( buscaUsuario.get().getId() != usuario.getId())) // valida se o usuario está presente E se o id passado não é diferente do cadastrado no banco de dados
	                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

	            usuario.setSenha(criptografarSenha(usuario.getSenha())); // criptografa novamente a senha do usuario antes de mandar para o banco

	            return Optional.ofNullable(usuarioRepository.save(usuario)); // e por fim, insere o objeto de usuario atualizado no banco de dados 
	        }
	        return Optional.empty();
	}

	public Optional <UsuarioLogin> autenticarUsuario(Optional <UsuarioLogin> usuarioLogin)
	{
		Optional <Usuario> usuario = usuarioRepository .findByUsuario(usuarioLogin.get().getUsuario());
		if(usuario.isPresent())
		{
			if(compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha()))
			{
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				return usuarioLogin;
			}
		}
		return Optional.empty();
	}
	
	private String criptografarSenha(String senha) // função que criptografa a senha do usuario
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  // semelhante ao Scanner(); do java
		return encoder.encode(senha); // usando o metodo do bcrypt que criptografa a senha digitada e retorna a senha já criptografada
	}
	
	private boolean compararSenhas(String senhaDigitada, String senhaBanco)
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(senhaDigitada, senhaBanco);
	}
	
	private String gerarBasicToken (String usuario, String senha)
	{
		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String (tokenBase64);
	}
}

