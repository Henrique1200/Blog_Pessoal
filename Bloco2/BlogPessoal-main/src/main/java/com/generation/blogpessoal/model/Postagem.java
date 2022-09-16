package com.generation.blogpessoal.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

	@Entity // indica ao spring que o objeto abaixo vai ser uma tabela no banco de dados
	@Table(name = "tb_postagens") // table --> dar um nome para a tabela a ser criada. Sem ela a tabela é criada com o nome do objeto

public class Postagem {
	
	
		@Id // indica que o id da tabela será uma chave primaria
		@GeneratedValue(strategy = GenerationType.IDENTITY) //generatevalue --> indica que a chave primaria será auto increment
		private Long id;
		
		@NotBlank(message = "O atributo título é Obrigatório!")
		@Size(min = 5, max = 100, message = "O atributo título deve conter no mínimo 05 e no máximo 100 caracteres") // size --> define o minimo e o maximo de letras que podem ser inseridas
		private String titulo;
		
		@NotNull(message = "O atributo texto é Obrigatório")//notnull --> define o campo de texto como campo obrigatório
		@Size(min = 10, max = 1000, message = "O atributo texto deve conter no mínimo 10 e no máximo 1000 caracteres")
		private String texto;
		
		@UpdateTimestamp
		private LocalDateTime data;
		
		@ManyToOne
		@JsonIgnoreProperties("postagem")
		private Tema tema;
		
		@ManyToOne
		@JsonIgnoreProperties("postagem")
		private Usuario usuario;
		

		public Long getId() 
		{
			return id;
		}

		public void setId(Long id) 
		{
			this.id = id;
		}

		public String getTitulo() 
		{
			return titulo;
		}

		public void setTitulo(String titulo) 
		{
			this.titulo = titulo;
		}

		public String getTexto() 
		{
			return texto;
		}

		public void setTexto(String texto) 
		{
			this.texto = texto;
		}

		public LocalDateTime getData() 
		{
			return data;
		}

		public void setData(LocalDateTime data) 
		{
			this.data = data;
		}

		public Tema getTema() 
		{
			return tema;
		}

		public void setTema(Tema tema) 
		{
			this.tema = tema;
		}

		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) 
		{
			this.usuario = usuario;
		}	
}
