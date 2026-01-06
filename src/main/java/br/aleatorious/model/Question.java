package br.aleatorious.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *  Entidade que representa uma questão de múltipla escolha.
 *  Cada questão possui um enunciado, quatro alternativas (A, B, C, D) e uma alternativa correta. As questões podem ser ativadas ou desativadas.
 * 
 *  @author Hugo Vinícius Rodrigues Pereira
 *  @version 1.0
 */
@Entity
@Table(name = "questoes")
public class Question {
	/**
	 *  Identificador único da questão.
	 */
	@Id
	@SequenceGenerator(name="question_id", sequenceName = "question_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="question_id")
	@Column(name = "id_questao")
	private int id;
	
	/**
	 *  Indica se a questão está ativa no sistema.
	 *  Questões inativas não aparecem nos testes.
	 *  Valor padrão: true.
	 */
	@Column(name = "ativo", nullable = false)
	private boolean active = true; 
	
	/**
	 *  Enunciado da questão.
	 *  Deve ter no mínimo 5 caracteres.
	 */
	@Size(min = 5, message = "Enunciado muito curto")
	@Column(name = "enunciado", columnDefinition = "TEXT") 
	private String statement;
	
	/**
	 *  Texto da alternativa A.
	 *  Campo obrigatório.
	 */
	@NotBlank(message = "A alternativa A é obrigatória")
    @Column(name = "alt_a", columnDefinition = "TEXT")
    private String altA;

	/**
	 *  Texto da alternativa B.
	 *  Campo obrigatório.
	 */
    @NotBlank(message = "A alternativa B é obrigatória")
    @Column(name = "alt_b", columnDefinition = "TEXT")
    private String altB;

    /**
	 *  Texto da alternativa C.
	 *  Campo obrigatório.
	 */
    @NotBlank(message = "A alternativa C é obrigatória")
    @Column(name = "alt_c", columnDefinition = "TEXT")
    private String altC;

    /**
	 *  Texto da alternativa C.
	 *  Campo obrigatório.
	 */
    @NotBlank(message = "A alternativa D é obrigatória")
    @Column(name = "alt_d", columnDefinition = "TEXT")
    private String altD;
	
    /**
	 *  Letra da alternativa correta (A, B, C ou D).
	 *  Campo obrigatório, máximo 1 caractere.
	 */
    @NotBlank(message = "Selecione a alternativa correta")
    @Column(name = "alternativa_correta", length = 1)
    private String correctAlternative; 
	
    /**
	 *  Disciplina à qual a questão pertence.
	 *  Relacionamento muitos-para-um com Subject.
	 */
	@ManyToOne
	@JoinColumn(name = "id_disciplina")
	private Subject subject;
	
	/**
	 *  Matéria/tópico específico da questão.
	 *  Relacionamento muitos-para-um com Topic.
	 */
	@ManyToOne
	@JoinColumn(name = "id_materia")
    private Topic topic;

	/**
	 *  Obtém o ID da questão.
	 *  @return ID da questão
	 */
	public int getId() {
		return id;
	}
	
	/**
	 *  Verifica se a questão está ativa.
	 *  @return true se ativa, false se inativa
	 */
	public boolean isActive() {
	    return active;
	}

	/**
	 *  Obtém o enunciado da questão.
	 *  @return enunciado
	 */
	public String getStatement() {
		return statement;
	}

	/**
	 *  Obtém o texto da alternativa A.
	 *  @return alternativa A
	 */
	public String getAltA() {
		return altA;
	}

	/**
	 *  Obtém o texto da alternativa B.
	 *  @return alternativa B
	 */
	public String getAltB() {
		return altB;
	}

	/**
	 *  Obtém o texto da alternativa C.
	 *  @return alternativa C
	 */
	public String getAltC() {
		return altC;
	}

	/**
	 *  Obtém o texto da alternativa D.
	 *  @return alternativa D
	 */
	public String getAltD() {
		return altD;
	}

	/**
	 *  Obtém a letra da alternativa correta.
	 *  @return alternativa correta (A, B, C ou D)
	 */
	public String getCorrectAlternative() {
		return correctAlternative;
	}

	/**
	 *  Obtém a disciplina associada.
	 *  @return disciplina
	 */
	public Subject getSubject() {
		return subject;
	}

	/**
	 *  Obtém a matéria/tópico associado.
	 *  @return tópico
	 */
	public Topic getTopic() {
		return topic;
	}

	/**
	 *  Define o ID da questão.
	 *  @param id ID a ser definido
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 *  Define se a questão está ativa.
	 *  @param active true para ativar, false para desativar
	 */
	public void setActive(boolean active) {
	    this.active = active;
	}

	/**
	 *  Define o enunciado da questão.
	 *  @param statement enunciado a ser definido
	 */
	public void setStatement(String statement) {
		this.statement = statement;
	}

	/**
	 *  Define o texto da alternativa A.
	 *  @param altA texto da alternativa A
	 */
	public void setAltA(String altA) {
		this.altA = altA;
	}

	/**
	 *  Define o texto da alternativa B.
	 *  @param altB texto da alternativa B
	 */
	public void setAltB(String altB) {
		this.altB = altB;
	}

	/**
	 *  Define o texto da alternativa C.
	 *  @param altC texto da alternativa C
	 */
	public void setAltC(String altC) {
		this.altC = altC;
	}

	/**
	 *  Define o texto da alternativa D.
	 *  @param altD texto da alternativa D
	 */
	public void setAltD(String altD) {
		this.altD = altD;
	}

	/**
	 *  Define a alternativa correta.
	 *  @param correctAlternative letra da alternativa correta (A, B, C ou D)
	 */
	public void setCorrectAlternative(String correctAlternative) {
		this.correctAlternative = correctAlternative;
	}

	/**
	 *  Define a disciplina associada.
	 *  @param subject disciplina a ser associada
	 */
	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	/**
	 *  Define a matéria/tópico associado.
	 *  @param topic tópico a ser associado
	 */
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
}
	