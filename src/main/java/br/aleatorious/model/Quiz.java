package br.aleatorious.model;

import java.util.Calendar;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 *  Entidade que representa um teste/quiz realizado por um usuário.
 *  Contém as questões selecionadas, respostas do estudante e pontuação obtida.
 * 
 *  @author Hugo Vinícius Rodrigues Pereira
 *  @version 1.0
 */
@Entity
@Table(name = "teste")
public class Quiz {
	/**
	 *  Identificador único do teste.
	 */
	@Id
	@SequenceGenerator(name="quiz_id", sequenceName = "quiz_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="quiz_id")
	@Column(name = "id_teste")
	private int id;
	
	/**
	 *  Data de realização do teste.
	 */
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "data_teste")
	private Calendar date;
	
	/**
	 *  Usuário que realizou o teste.
	 */
	@ManyToOne
    @JoinColumn(name = "id_user") 
    private User user;
	
	/**
	 *  Matéria/tópico do teste.
	 */
	@ManyToOne
    @JoinColumn(name = "id_materia") 
    private Topic topic;
	
	/**
	 *  Lista de questões do teste.
	 *  Relacionamento muitos-para-muitos com a entidade Question.
	 */
	@ManyToMany
    @JoinTable(
        name = "teste_questoes", 
        joinColumns = @JoinColumn(name = "id_teste"),
        inverseJoinColumns = @JoinColumn(name = "id_questao")
    )
    private List<Question> questions;
	
	/**
	 *  Respostas fornecidas pelo estudante.
	 *  Formato: sequência de letras separadas por vírgula (ex: "A,B,C,D,A").
	 */
	@Column(name = "respostas")
	private String studentAnswers;
	
	/**
	 *  Quantidade de acertos no teste.
	 */
	@Column(name = "acertos")
	private int score;
	
	/**
	 *  Quantidade total de questões no teste.
	 *  Valor padrão: 5 questões.
	 */
	@Column(name = "qntd_questoes")
	private int totalQuestions = 5;

	/**
	 *  Obtém o ID do teste.
	 *  @return ID do teste
	 */
	public int getId() {
		return id;
	}

	/**
	 *  Obtém a data de realização do teste.
	 *  @return data do teste
	 */ 
	public Calendar getDate() {
		return date;
	}

	/**
	 *  Obtém o usuário que realizou o teste.
	 *  @return usuário associado
	 */
	public User getUser() {
		return user;
	}

	/**
	 *  Obtém a matéria/tópico do teste.
	 *  @return tópico associado
	 */
	public Topic getTopic() {
		return topic;
	}

	/**
	 *  Obtém a lista de questões do teste.
	 *  @return lista de questões
	 */
	public List<Question> getQuestions() {
		return questions;
	}

	/**
	 *  Obtém as respostas do estudante.
	 *  @return string com as respostas
	 */
	public String getStudentAnswers() {
		return studentAnswers;
	}

	/**
	 *  Obtém a pontuação (número de acertos).
	 *  @return quantidade de acertos
	 */
	public int getScore() {
		return score;
	}

	/**
	 *  Obtém o total de questões do teste.
	 *  @return quantidade total de questões
	 */
	public int getTotalQuestions() {
		return totalQuestions;
	}

	/**
	 *  Define o ID do teste.
	 *  @param id ID a ser definido
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 *  Define a data de realização do teste.
	 *  @param date data a ser definida
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 *  Define o usuário que realizou o teste.
	 *  @param user usuário a ser associado
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 *  Define a matéria/tópico do teste.
	 *  @param topic tópico a ser associado
	 */ 
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	/**
	 *  Define a lista de questões do teste.
	 *  @param questions lista de questões
	 */
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	/**
	 *  Define as respostas do estudante.
	 *  @param studentAnswers respostas em formato de string
	 */
	public void setStudentAnswers(String studentAnswers) {
		this.studentAnswers = studentAnswers;
	}

	/**
	 *  Define a pontuação do teste.
	 *  @param score número de acertos
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 *  Define o total de questões do teste.
	 *  @param totalQuestions quantidade de questões
	 */
	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
}
