package br.aleatorious.controller;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.aleatorious.dao.DAO;
import br.aleatorious.dao.EntityManagerProvider;
import br.aleatorious.dao.UserDAO;
import br.aleatorious.model.Question;
import br.aleatorious.model.Quiz;
import br.aleatorious.model.Subject;
import br.aleatorious.model.Topic;
import br.aleatorious.model.User;
import br.aleatorious.util.HashUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 *  Controlador principal da aplicação Aleatorious.
 *  Gerencia todas as operações relacionadas a autenticação, disciplinas, matérias, questões, geração de testes e relatórios.
 * 
 *  @author Hugo Vinícius Rodrigues Pereira
 *  @version 1.0
 */
@Controller
public class AleatoriousController {
	
	/**
	 *  Exibe a página inicial da aplicação.
	 * 
	 *  @return nome da view index
	 */
	@RequestMapping("aleatorious/index")
	public String index() {
	    return "aleatorious/index"; 
	}
	
	/**
     *  Exibe a página de erro 404 (Página Não Encontrada).
     * 
     *  @return caminho para a view de erro 404
     */
    @RequestMapping("/error-404")
    public String error404() {
        return "aleatorious/error-404";
    }
	
	/**
	 *  Exibe o formulário de login.
	 * 
	 *  @return nome da view de login
	 */
	@RequestMapping("loginForm")
	public String loginForm() {
		return "aleatorious/login";
	}
	
	/**
	 *  Processa a autenticação do usuário.
	 *  Valida as credenciais, criptografa a senha e autentica o usuário no sistema.
	 * 
	 *  @param user dados do usuário para login
	 *  @param result resultado da validação
	 *  @param session sessão HTTP para armazenar o usuário autenticado
	 *  @return Map contendo o resultado da operação (success, message, redirect)
	 */
    @ResponseBody 
    @RequestMapping("login")
    public Map<String, Object> login(@Valid User user, BindingResult result, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {
            response.put("success", false);
            response.put("message", result.getFieldError().getDefaultMessage());
            return response;
        }

        user.setPassword(HashUtil.hashPassword(user.getPassword()));
        User authenticated = new UserDAO().validateLogin(user);

        if(authenticated != null) {
            session.setAttribute("loggedUser", authenticated);
            response.put("success", true);
            response.put("redirect", "aleatorious/index"); 
        } else {
            response.put("success", false);
            response.put("message", "Usuário ou senha inválidos.");
        }

        return response;
    }
	
    /**
	 *  Realiza o logout do usuário, invalidando a sessão.
	 * 
	 *  @param session sessão HTTP a ser invalidada
	 *  @return redirecionamento para o formulário de login
	 */
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:loginForm";
	}
	
	/**
	 *  Exibe a página de gerenciamento de disciplinas.
	 *  Lista todas as disciplinas cadastradas no sistema.
	 * 
	 *  @param model modelo para passar dados à view
	 *  @return nome da view de disciplinas ou null em caso de erro
	 */
	@RequestMapping("pageSubjects")
	public String pageSubjects(Model model) {
	    try {
	        DAO<Subject> dao = new DAO<>(Subject.class);
	        model.addAttribute("subjects", dao.listAll());
	        return "aleatorious/subjects-fragment"; 
	    } catch(Exception e) {
	        e.printStackTrace(); 
	        return null; 
	    }
	}

	/**
	 *  Salva (insere ou atualiza) uma disciplina no banco de dados.
	 * 
	 *  @param subject disciplina a ser salva
	 *  @param result resultado da validação
	 *  @return Map contendo o resultado da operação (success, message)
	 */
	@ResponseBody
	@PostMapping("saveSubject")
	public Map<String, Object> saveSubject(@Valid Subject subject, BindingResult result) {
	    Map<String, Object> response = new HashMap<>();

	    if(result.hasErrors()) {
	        response.put("success", false);
	        response.put("message", result.getFieldError().getDefaultMessage());
	        return response;
	    }

	    try {
	        DAO<Subject> dao = new DAO<>(Subject.class);
	        if (subject.getId() > 0) {
	            dao.alter(subject);
	        } else {
	            dao.insert(subject);
	        }
	        response.put("success", true);
	    } catch(Exception e) {
	        e.printStackTrace(); 
	        response.put("success", false);
	        response.put("message", "Erro no Banco de Dados: " + e.getMessage());
	    }
	    return response;
	}
	
	/**
	 *  Remove uma disciplina do banco de dados.
	 * 
	 *  @param id ID da disciplina a ser removida
	 *  @return Map contendo o resultado da operação (success, message)
	 */
	@ResponseBody
	@PostMapping("removeSubject")
	public Map<String, Object> removeSubject(@RequestParam("id") int id) { 
	    Map<String, Object> response = new HashMap<>();
	    try {
	        DAO<Subject> dao = new DAO<>(Subject.class);
	        
	        Subject subject = dao.findById(id); 
	        
	        if(subject != null) {
	            dao.remove(subject);
	            response.put("success", true);
	        } else {
	            response.put("success", false);
	            response.put("message", "Disciplina não encontrada.");
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("message", "Erro ao excluir: " + e.getMessage());
	    }
	    return response;
	}
	
	/**
	 *  Exibe a página de gerenciamento de matérias.
	 *  Lista todas as matérias e disciplinas cadastradas.
	 * 
	 *  @param model modelo para passar dados à view
	 *  @return nome da view de matérias
	 */
	@RequestMapping("pageTopics")
	public String pageTopics(Model model) {
	    model.addAttribute("topics", new DAO<>(Topic.class).listAll());
	    model.addAttribute("subjects", new DAO<>(Subject.class).listAll()); 
	    return "aleatorious/topics-fragment";
	}
	
	/**
	 *  Salva (insere ou atualiza) uma matéria no banco de dados.
	 * 
	 *  @param topic matéria a ser salva
	 *  @param result resultado da validação
	 *  @return Map contendo o resultado da operação (success, message)
	 */
	@ResponseBody
	@PostMapping("saveTopic")
	public Map<String, Object> saveTopic(@Valid Topic topic, BindingResult result) {
	    Map<String, Object> response = new HashMap<>();
	    if(result.hasErrors()) {
	        response.put("success", false);
	        response.put("message", "Verifique os campos obrigatórios.");
	        return response;
	    }

	    try {
	        DAO<Topic> dao = new DAO<>(Topic.class);
	        if(topic.getId() > 0) {
	            dao.alter(topic);
	        } else {
	            dao.insert(topic);
	        }
	        response.put("success", true);
	    } catch(Exception e) {
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("message", "Erro ao salvar matéria.");
	    }
	    return response;
	}
	
	/**
	 *  Remove uma matéria do banco de dados.
	 *  Não permite remoção se existirem questões vinculadas à matéria.
	 * 
	 *  @param id ID da matéria a ser removida
	 *  @return Map contendo o resultado da operação (success, message)
	 */
	@ResponseBody
	@PostMapping("removeTopic")
	public Map<String, Object> removeTopic(@RequestParam("id") int id) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        DAO<Topic> dao = new DAO<>(Topic.class);
	        Topic topic = dao.findById(id);
	        if(topic != null) {
	            dao.remove(topic);
	            response.put("success", true);
	        }
	    } catch(Exception e) {
	        response.put("success", false);
	        response.put("message", "Não é possível excluir: existem questões ligadas a esta matéria.");
	    }
	    return response;
	}
	
	/**
	 *  Exibe a página de gerenciamento de questões.
	 *  Lista todas as questões e disciplinas cadastradas.
	 * 
	 *  @param model modelo para passar dados à view
	 *  @return nome da view de questões
	 */
	@RequestMapping("pageQuestions")
	public String pageQuestions(Model model) {
		model.addAttribute("questions", new DAO<>(Question.class).listAll());
        model.addAttribute("subjects", new DAO<>(Subject.class).listAll()); 
        return "aleatorious/questions-fragment";
	}
	
	/**
	 *  Busca matérias filtradas por disciplina.
	 * 
	 *  @param subjectId ID da disciplina para filtrar as matérias
	 *  @return lista de matérias da disciplina especificada
	 */
	@ResponseBody
	@RequestMapping("getTopicsBySubject")
	public List<Topic> getTopicsBySubject(@RequestParam("subjectId") int subjectId) {
	    EntityManager em = new EntityManagerProvider().getEntityManager();
	    try {
	        return em.createQuery("SELECT t FROM Topic t WHERE t.subject.id = :sId", Topic.class).setParameter("sId", subjectId).getResultList();
	    } finally {
	        em.close();
	    }
	}
	
	/**
	 *  Lista todas as questões ativas do sistema.
	 * 
	 *  @param model modelo para passar dados à view
	 *  @return nome da view de questões
	 */
	@RequestMapping("listQuestions")
	public String listQuestions(Model model) {
	    EntityManager em = new EntityManagerProvider().getEntityManager();
	    
	    List<Question> questions = em.createQuery("SELECT q FROM Question q WHERE q.active = true", Question.class).getResultList();
	    
	    model.addAttribute("questions", questions);
	    em.close();
	    
	    return "aleatorious/questions-fragment"; 
	}

	/**
	 *  Salva (insere ou atualiza) uma questão no banco de dados.
	 * 
	 *  @param question questão a ser salva
	 *  @param result resultado da validação
	 *  @return Map contendo o resultado da operação (success, message)
	 */
	@ResponseBody
	@PostMapping("saveQuestion")
	public Map<String, Object> saveQuestion(@Valid Question question, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
	    if(result.hasErrors()) {
	        response.put("success", false);
	        // Pega a primeira mensagem de erro das anotações @NotBlank ou @Size
	        response.put("message", result.getFieldError().getDefaultMessage());
	        return response;
	    }

	    try {
	        DAO<Question> dao = new DAO<>(Question.class);
	  
	        if(question.getId() > 0) {
	            dao.alter(question);
	        } else {
	            dao.insert(question);
	        }
	        response.put("success", true);
	    } catch(Exception e) {
	        response.put("success", false);
	        response.put("message", "Erro ao salvar questão: " + e.getMessage());
	    }
	    return response;
	}
	
	/**
	 *  Inativa uma questão (soft delete).
	 *  A questão não é removida fisicamente, apenas marcada como inativa.
	 * 
	 *  @param id ID da questão a ser inativada
	 *  @return Map contendo o resultado da operação (success, message)
	 */
	@ResponseBody
	@PostMapping("removeQuestion")
	public Map<String, Object> removeQuestion(@RequestParam("id") int id) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        DAO<Question> dao = new DAO<>(Question.class);
	        Question question = dao.findById(id);
	        
	        if(question != null) {
	            // Exclusão lógica
	        	question.setActive(false); 
	            dao.alter(question); 
	            
	            response.put("success", true);
	        } else {
	            response.put("success", false);
	            response.put("message", "Questão não encontrada.");
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("message", "Erro ao desativar questão.");
	    }
	    return response;
	}
	
	/**
	 *  Exibe a página de gerenciamento de usuários.
	 *  Lista todos os usuários cadastrados no sistema.
	 * 
	 *  @param model modelo para passar dados à view
	 *  @return nome da view de usuários
	 */
	@RequestMapping("pageUsers")
	public String pageUsers(Model model) {
	    model.addAttribute("users", new DAO<>(User.class).listAll());
	    return "aleatorious/users-fragment";
	}

	/**
	 *  Salva (insere ou atualiza) um usuário no banco de dados.
	 *  Criptografa a senha antes de salvar.
	 * 
	 *  @param user usuário a ser salvo
	 *  @param result resultado da validação
	 *  @return Map contendo o resultado da operação (success, message)
	 */
	@ResponseBody
	@PostMapping("saveUser")
	public Map<String, Object> saveUser(@Valid User user, BindingResult result) {
	    Map<String, Object> response = new HashMap<>();
	    
	    if(result.hasErrors()) {
	        response.put("success", false);
	        response.put("message", result.getFieldError().getDefaultMessage());
	        return response;
	    }

	    try {
	        if(user.getId() == 0) {
	            // Criptografa a senha apenas para novos usuários
	            user.setPassword(HashUtil.hashPassword(user.getPassword()));
	            new DAO<>(User.class).insert(user);
	        } else {
	            new DAO<>(User.class).alter(user);
	        }
	        
	        response.put("success", true);
	    } catch(Exception e) {
	        response.put("success", false);
	        response.put("message", "Erro ao salvar: " + e.getMessage());
	    }
	    
	    return response;
	}

	/**
	 *  Remove um usuário do banco de dados.
	 * 
	 *  @param id ID do usuário a ser removido
	 *  @return Map contendo o resultado da operação (success, message)
	 */
	@ResponseBody
	@PostMapping("removeUser")
	public Map<String, Object> removeUser(@RequestParam("id") int id) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        DAO<User> dao = new DAO<>(User.class);
	        User user = dao.findById(id);
	        
	        // Bloqueia a exclusão se for admin
	        if(user != null && user.getProfile().equals("admin")) {
	            response.put("success", false);
	            response.put("message", "O usuário administrador não pode ser excluído.");
	            return response;
	        }

	        if(user != null) {
	            dao.remove(user);
	            response.put("success", true);
	        }
	    } catch(Exception e) {
	        response.put("success", false);
	        response.put("message", "Erro ao excluir.");
	    }
	    return response;
	}

	/**
	 *  Exibe a página de relatórios administrativos.
	 *  Lista todos os estudantes e seus respectivos testes.
	 *  Disponível apenas para administradores.
	 * 
	 *  @param session sessão HTTP para verificar permissões
	 *  @param model modelo para passar dados à view
	 *  @return nome da view de relatórios administrativos ou redirecionamento ao login
	 */
	@RequestMapping("adminReports")
	public String adminReports(HttpSession session, Model model) {
	    User user = (User) session.getAttribute("loggedUser");
	    
	    // Verifica se é admin
	    if(user == null || !user.getProfile().equals("admin")) {
	        return "redirect:/loginForm";
	    }

	    EntityManager em = new EntityManagerProvider().getEntityManager();
	    
	    try {
	        List<User> students = em.createQuery("SELECT u FROM User u WHERE u.profile = 'user' ORDER BY u.name", User.class).getResultList();
	        
	        model.addAttribute("students", students);
	        
	        List<Quiz> allQuizzes = em.createQuery(
	            "SELECT DISTINCT q FROM Quiz q " +
	            "JOIN FETCH q.user u " +
	            "JOIN FETCH q.questions " +
	            "JOIN FETCH q.topic t " +
	            "JOIN FETCH t.subject " +
	            "WHERE u.profile = 'user' " +
	            "ORDER BY u.name, q.date DESC", Quiz.class)
	            .getResultList();
	        
	        model.addAttribute("allQuizzes", allQuizzes);
	        
	    } catch(Exception e) {
	        System.out.println("Erro ao buscar relatórios: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }
	    
	    return "aleatorious/admin-reports-fragment";
	}

	/**
	 *  Exibe relatórios filtrados por estudante específico.
	 *  Calcula estatísticas de desempenho do estudante selecionado.
	 * 
	 *  @param userId ID do estudante
	 *  @param session sessão HTTP para verificar permissões
	 *  @param model modelo para passar dados à view
	 *  @return nome da view de relatórios administrativos ou redirecionamento ao login
	 */
	@RequestMapping("adminReportsByStudent")
	public String adminReportsByStudent(@RequestParam("userId") int userId, HttpSession session, Model model) {
	    User admin = (User) session.getAttribute("loggedUser");
	    
	    // Verifica se é admin
	    if(admin == null || !admin.getProfile().equals("admin")) {
	        return "redirect:/loginForm";
	    }

	    EntityManager em = new EntityManagerProvider().getEntityManager();
	    
	    try {
	        // Busca o usuário específico
	        User student = em.find(User.class, userId);
	        model.addAttribute("selectedStudent", student);
	        
	        // Busca todos as usuárias para o filtro
	        List<User> students = em.createQuery("SELECT u FROM User u WHERE u.profile = 'user' ORDER BY u.name", User.class).getResultList();
	        model.addAttribute("students", students);
	        
	        // Busca os testes dessa usuária específica
	        List<Quiz> quizzes = em.createQuery(
	            "SELECT DISTINCT q FROM Quiz q " +
	            "JOIN FETCH q.questions " +
	            "JOIN FETCH q.topic t " +
	            "JOIN FETCH t.subject " +
	            "WHERE q.user.id = :uId " +
	            "ORDER BY q.date DESC", Quiz.class)
	            .setParameter("uId", userId)
	            .getResultList();
	        
	        model.addAttribute("studentQuizzes", quizzes);
	        
	        // Calcula estatísticas
	        if(!quizzes.isEmpty()) {
	            double totalScore = 0;
	            int totalQuestions = 0;
	            
	            for(Quiz q : quizzes) {
	                totalScore += q.getScore();
	                totalQuestions += q.getTotalQuestions();
	            }
	            
	            double averagePercent = (totalScore / totalQuestions) * 100;
	            model.addAttribute("averagePercent", averagePercent);
	            model.addAttribute("totalTests", quizzes.size());
	        }
	        
	    } catch(Exception e) {
	        System.out.println("Erro ao buscar relatórios por aluna: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }
	    
	    return "aleatorious/admin-reports-fragment";
	}
	
	/**
	 *  Exibe o formulário de geração de teste.
	 *  Lista todas as disciplinas disponíveis para seleção.
	 * 
	 *  @param model modelo para passar dados à view
	 *  @return nome da view de geração de teste
	 */
	@RequestMapping("/showGenerateQuiz")
	public String showGenerateQuiz(Model model) {
	    model.addAttribute("subjects", new DAO<>(Subject.class).listAll());
	    return "aleatorious/generate-quiz-fragment";
	}

	/**
	 *  Gera um novo teste aleatório com 5 questões.
	 *  Seleciona questões aleatórias baseadas na disciplina e bimestre escolhidos.
	 * 
	 *  @param subjectId ID da disciplina
	 *  @param term bimestre do conteúdo
	 *  @param session sessão HTTP para obter o usuário logado
	 *  @return Map contendo o resultado da operação (success, message, quizId)
	 */
	@ResponseBody
	@PostMapping("generateQuiz")
	public Map<String, Object> generateQuiz(@RequestParam("subjectId") int subjectId, @RequestParam("term") int term, HttpSession session) {
	    Map<String, Object> response = new HashMap<>();
	    EntityManager em = new EntityManagerProvider().getEntityManager();
	    try {
	        List<Question> availableQuestions = em.createQuery(
	            "SELECT q FROM Question q WHERE q.subject.id = :sId AND q.topic.term = :term", Question.class)
	            .setParameter("sId", subjectId)
	            .setParameter("term", term)
	            .getResultList();

	        if(availableQuestions.size() < 5) {
	            response.put("success", false);
	            response.put("message", "Não há questões suficientes.");
	            return response;
	        }

	        Topic topic = em.createQuery("SELECT t FROM Topic t WHERE t.subject.id = :sId AND t.term = :term", Topic.class)
	                        .setParameter("sId", subjectId)
	                        .setParameter("term", term)
	                        .setMaxResults(1)
	                        .getSingleResult();

	        Collections.shuffle(availableQuestions);
	        List<Question> selectedQuestions = availableQuestions.subList(0, 5);

	        User loggedUser = (User) session.getAttribute("loggedUser");
	        
	        Quiz quiz = new Quiz();
	        quiz.setDate(Calendar.getInstance());
	        quiz.setUser(loggedUser);
	        quiz.setTopic(topic); 
	        quiz.setQuestions(selectedQuestions);
	        quiz.setTotalQuestions(5);
	        quiz.setScore(0);

	        em.getTransaction().begin();
	        em.persist(quiz); 
	        em.getTransaction().commit();

	        response.put("success", true);
	        response.put("quizId", quiz.getId()); 
	    } catch(Exception e) {
	        response.put("success", false);
	        response.put("message", "Erro: " + e.getMessage());
	    } finally {
	        em.close();
	    }
	    return response;
	}
	
	/**
	 *  Exibe o teste para realização pelo estudante.
	 *  Carrega todas as questões do teste.
	 * 
	 *  @param id ID do teste
	 *  @param model modelo para passar dados à view
	 *  @return nome da view de realização do teste
	 */
	@RequestMapping("takeQuiz")
	public String takeQuiz(@RequestParam("id") int id, Model model) {
		EntityManager em = new EntityManagerProvider().getEntityManager();
	    try {
	        Quiz quiz = em.createQuery("SELECT q FROM Quiz q JOIN FETCH q.questions WHERE q.id = :id", Quiz.class).setParameter("id", id).getSingleResult();
	        
	        model.addAttribute("quiz", quiz);
	    } catch(Exception e) {
	        Quiz quiz = em.find(Quiz.class, id);
	        model.addAttribute("quiz", quiz);
	    } finally {
	        em.close();
	    }
	    return "aleatorious/take-quiz-fragment";
	}
	
	/**
	 *  Processa a submissão de um teste realizado.
	 *  Calcula a pontuação e salva as respostas do estudante.
	 * 
	 *  @param quizId ID do teste
	 *  @param allParams parâmetros contendo todas as respostas do estudante
	 *  @param session sessão HTTP
	 *  @return Map contendo o resultado da operação (success, score, total, message)
	 */
	@ResponseBody
	@PostMapping("submitQuiz")
	public Map<String, Object> submitQuiz(@RequestParam("quizId") int quizId, @RequestParam Map<String, String> allParams, HttpSession session) {
	    Map<String, Object> response = new HashMap<>();
	    EntityManager em = new EntityManagerProvider().getEntityManager();
	    
	    try {
	        Quiz quiz = em.find(Quiz.class, quizId);
	        List<Question> questions = quiz.getQuestions();
	        int score = 0;
	        StringBuilder answersSaved = new StringBuilder();

	        for(int i = 0; i < questions.size(); i++) {
	            String studentChoice = allParams.get("answers[" + i + "]"); 
	            Question q = questions.get(i);
	            
	            if(studentChoice != null && studentChoice.equals(q.getCorrectAlternative())) {
	                score++;
	            }
	            
	            // Salva a sequência de respostas (Ex: A,B,D,A,C)
	            answersSaved.append(studentChoice != null ? studentChoice : "-");
	            if(i < questions.size() - 1) answersSaved.append(",");
	        }

	        em.getTransaction().begin();
	        quiz.setScore(score);
	        quiz.setStudentAnswers(answersSaved.toString());
	        em.merge(quiz);
	        em.getTransaction().commit();

	        response.put("success", true);
	        response.put("score", score);
	        response.put("total", questions.size());
	        
	    } catch(Exception e) {
	        response.put("success", false);
	        response.put("message", "Erro: " + e.getMessage());
	    } finally {
	        em.close();
	    }
	    return response;
	}
	
	/**
	 *  Lista todos os relatórios/testes do usuário logado.
	 *  Exibe histórico de testes realizados com suas pontuações.
	 * 
	 *  @param session sessão HTTP para obter o usuário logado
	 *  @param model modelo para passar dados à view
	 *  @return nome da view de relatórios ou redirecionamento ao login
	 */
	@RequestMapping("listReports")
	public String listReports(HttpSession session, Model model) {
	    User user = (User) session.getAttribute("loggedUser");
	    if(user == null) return "redirect:/login";

	    EntityManager em = new EntityManagerProvider().getEntityManager();
	    
	    try {
	        List<Quiz> quizzes = em.createQuery(
	            "SELECT DISTINCT q FROM Quiz q " +
	            "JOIN FETCH q.questions " + 
	            "JOIN FETCH q.topic t " +
	            "JOIN FETCH t.subject " +
	            "WHERE q.user.id = :uId ORDER BY q.date DESC", Quiz.class)
	            .setParameter("uId", user.getId())
	            .getResultList();
	        
	        model.addAttribute("myQuizzes", quizzes);
	    } catch(Exception e) {
	        System.out.println("Erro ao listar relatórios: " + e.getMessage());
	    } finally {
	        em.close();
	    }
	    return "aleatorious/list-reports-fragment";
	}
}