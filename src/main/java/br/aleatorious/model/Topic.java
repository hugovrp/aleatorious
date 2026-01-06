package br.aleatorious.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 *  Entidade que representa uma matéria/tópico específico de uma disciplina.
 * 
 *  @author Hugo Vinícius Rodrigues Pereira
 *  @version 1.0
 */
@Entity
@Table(name = "materia")
public class Topic {
	/**
	 *  Identificador único da matéria.
	 */
	@Id
	@SequenceGenerator(name="topic_id", sequenceName = "topic_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="topic_id")
	@Column(name = "id_materia")
	private int id;
	
	/**
	 *  Nome da matéria/tópico.
	 */
	@Column(name = "nome_materia")
	private String name;
	
	/**
	 *  Série/ano escolar desta matéria.
	 */
	@Column(name = "serie")
	private int grade; 
	
	/**
	 *  Bimestre/período letivo desta matéria.
	 */
	@Column(name = "bimestre")
	private int term;  
	
	/**
	 *  Disciplina à qual esta matéria pertence.
	 *  Relacionamento muitos-para-um com Subject.
	 */
	@ManyToOne 
	@JoinColumn(name = "id_disciplina")
	private Subject subject;
	
	/**
	 *  Lista de questões desta matéria.
	 *  Relacionamento um-para-muitos com Question.
	 *  JsonIgnore para evitar serialização circular.
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
	private List<Question> questions;

	/**
	 *  Obtém o ID da matéria.
	 *  @return ID da matéria
	 */
	public int getId() {
		return id;
	}

	/**
	 *  Obtém o nome da matéria.
	 *  @return nome da matéria
	 */
	public String getName() {
		return name;
	}

	/**
	 *  Obtém a série/ano escolar.
	 *  @return série
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 *  Obtém o bimestre.
	 *  @return bimestre
	 */
	public int getTerm() {
		return term;
	}

	/**
	 *  Obtém a disciplina associada.
	 *  @return disciplina
	 */
	public Subject getSubject() {
		return subject;
	}

	/**
	 *  Obtém a lista de questões desta matéria.
	 *  @return lista de questões
	 */
	public List<Question> getQuestions() {
		return questions;
	}

	/**
	 *  Define o ID da matéria.
	 *  @param id ID a ser definido
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 *  Define o nome da matéria.
	 *  @param name nome a ser definido
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *  Define a série/ano escolar.
	 *  @param grade série a ser definida
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**
	 *  Define o bimestre.
	 *  @param term bimestre a ser definido
	 */
	public void setTerm(int term) {
		this.term = term;
	}

	/**
	 *  Define a disciplina associada.
	 *  @param subject disciplina a ser associada
	 */
	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	/**
	 *  Define a lista de questões.
	 *  @param questions lista de questões a ser definida
	 */
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}
