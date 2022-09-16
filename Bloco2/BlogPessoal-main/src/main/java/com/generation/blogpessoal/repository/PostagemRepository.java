package com.generation.blogpessoal.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.generation.blogpessoal.model.Postagem;


	@Repository
	public interface PostagemRepository extends JpaRepository<Postagem, Long> {
		
	public List <Postagem> findAllByTituloContainingIgnoreCase/*(@Param("titulo")*/(String titulo);
	// ^ mesma coisa que SELECT * FROM tb_postagens WHERE titulo LIKE "%%"; ^
	}
