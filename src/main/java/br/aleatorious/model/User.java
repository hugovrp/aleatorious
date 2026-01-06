package br.aleatorious.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 *  Entidade que representa um usuário do sistema.
 *  Pode ser um administrador (perfil "admin") ou um estudante (perfil "user").
 * 
 *  @author Hugo Vinícius Rodrigues Pereira
 *  @version 1.0
 */
@Entity
@Table(name = "usuarios")
public class User {
	/**
	 *  Identificador único do usuário.
	 */
	@Id
	@SequenceGenerator(name="user_id", sequenceName = "user_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_id")
	@Column(name = "id_user")
	private int id;
	
	/**
	 *  Nome de usuário para login.
	 *  Campo obrigatório.
	 */
	@NotBlank(message = "O nome de usuário é obrigatório")
	@Column(name = "nome")
	private String name;
	
	/**
	 *  Perfil/tipo do usuário.
	 *  Valores possíveis: "admin" (administrador) ou "user" (estudante).
	 */
	@Column(name = "perfil")
	private String profile;
	
	/**
	 *  Senha do usuário (armazenada de forma criptografada).
	 *  Campo obrigatório.
	 */
	@NotBlank(message = "A senha é obrigatória")
	@Column(name = "senha")
	private String password;

	/**
	 *  Obtém o ID do usuário.
	 *  @return ID do usuário
	 */
	public int getId() {
		return id;
	}

	/**
	 *  Obtém o nome do usuário.
	 *  @return nome do usuário
	 */
	public String getName() {
		return name;
	}

	/**
	 *  Obtém o perfil do usuário.
	 *  @return perfil (admin ou user)
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 *  Obtém a senha criptografada do usuário.
	 *  @return senha criptografada
	 */
	public String getPassword() {
		return password;
	}

	/**
	 *  Define o ID do usuário.
	 *  @param id ID a ser definido
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 *  Define o nome do usuário.
	 *  @param name nome a ser definido
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *  Define o perfil do usuário.
	 *  @param profile perfil a ser definido (admin ou user)
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}

	/**
	 *  Define a senha do usuário.
	 *  @param password senha a ser definida (deve ser criptografada antes)
	 */
	public void setPassword(String password) {
		this.password = password;
	}	
}