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
 *  Entidade que representa uma disciplina escolar.
 *  Exemplo: Matemática, Português, História, etc.
 * 
 *  @author Hugo Vinícius Rodrigues Pereira
 *  @version 1.0
 */
@Entity
@Table(name = "disciplina")
public class Subject {
	/**
	 *  Identificador único da disciplina.
	 */
	@Id
	@SequenceGenerator(name="subject_id", sequenceName = "subject_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="subject_id")
	@Column(name = "id_disciplina")
	private int id;
	
	/**
	 *  Nome da disciplina.
	 *  Campo obrigatório.
	 */
	@NotBlank(message = "O nome da disciplina é obrigatório")
	@Column(name = "nome_disciplina")
	private String name;

	/**
	 *  Obtém o ID da disciplina.
	 *  @return ID da disciplina
	 */
	public int getId() {
		return id;
	}

	/**
	 *  Obtém o nome da disciplina.
	 *  @return nome da disciplina
	 */
	public String getName() {
		return name;
	}

	/**
	 *  Define o ID da disciplina.
	 *  @param id ID a ser definido
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 *  Define o nome da disciplina.
	 *  @param name nome a ser definido
	 */
	public void setName(String name) {
		this.name = name;
	}
}
