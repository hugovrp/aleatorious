# 🎲 Aleatorious - Sistema de Testes Aleatórios

> Sistema web educacional desenvolvido em Java para geração e gerenciamento de testes aleatórios, permitindo criação de questões, aplicação de provas e análise de desempenho dos estudantes.

[![Java](https://img.shields.io/badge/Java-24-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring](https://img.shields.io/badge/Spring-7.0.1-green?style=for-the-badge&logo=spring)](https://spring.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-42.7.4-blue?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)
[![Hibernate](https://img.shields.io/badge/Hibernate-6.2.7-darkred?style=for-the-badge&logo=hibernate)](https://hibernate.org/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red?style=for-the-badge&logo=apachemaven)](https://maven.apache.org/)

---

## 📋 Sobre o Projeto

**Aleatorious** é um sistema completo de gerenciamento de testes educacionais que permite:

- 📚 **Cadastro de Disciplinas e Matérias** - Organização hierárquica por série e bimestre
- ❓ **Banco de Questões** - Questões de múltipla escolha com 4 alternativas
- 🎲 **Geração Aleatória de Testes** - 5 questões selecionadas aleatoriamente por disciplina/bimestre
- ✅ **Realização de Testes** - Interface intuitiva para estudantes responderem as questões
- 📊 **Relatórios e Estatísticas** - Análise de desempenho individual e coletivo
- 👥 **Gestão de Usuários** - Perfis diferenciados (Admin e Estudante)
- 🔐 **Sistema de Autenticação** - Login seguro com hash SHA-256

> **Disciplina**: Desenvolvimento de Aplicações Web  
> **Curso**: Sistemas para Internet  
> **Tipo**: Trabalho Individual

---

## 🚀 Tecnologias

### Backend
- **Java 24** - Linguagem principal
- **Spring MVC 7.0.1** - Framework web
- **Hibernate 6.2.7** - ORM para persistência
- **Jakarta Persistence API 3.2.0** - Especificação JPA
- **Jakarta Validation** - Validação de dados
- **Maven** - Gerenciamento de dependências

### Banco de Dados
- **PostgreSQL 42.7.4** - Banco de dados relacional
- **JPA/Hibernate** - Mapeamento objeto-relacional

### Frontend
- **JSP + JSTL 3.0** - Template engine
- **jQuery 3.6.0** - Manipulação DOM e AJAX
- **HTML5 + CSS3** - Interface moderna e responsiva
- **JavaScript (ES6+)** - Validações e interatividade

### Servidor
- **Apache Tomcat 10.1** - Container de servlets
- **Jakarta Servlet API** - Especificação de servlets

### Segurança
- **SHA-256** - Hash de senhas (backend e frontend)
- **Spring Interceptors** - Controle de acesso
- **Sessões HTTP** - Gerenciamento de autenticação

---

## 📦 Pré-requisitos

- [JDK 24+](https://openjdk.org/projects/jdk/24/)
- [Apache Tomcat 10.1+](https://tomcat.apache.org/download-10.cgi)
- [PostgreSQL 12+](https://www.postgresql.org/download/)
- [Maven 3.x](https://maven.apache.org/download.cgi)

---

## 🔧 Configuração do Banco de Dados

### 1. Criar o banco de dados PostgreSQL

```sql
CREATE DATABASE Aleatorious;
```

### 2. Configurar credenciais

Edite o arquivo `src/main/resources/META-INF/persistence.xml`:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/Aleatorious" />
<property name="jakarta.persistence.jdbc.user" value="seu_usuario" />
<property name="jakarta.persistence.jdbc.password" value="sua_senha" />
```

> 📝 **Nota**: O Hibernate criará automaticamente todas as tabelas necessárias através da configuração `hibernate.hbm2ddl.auto = update`

---

## 🔐 Credenciais de Acesso

### Usuários Padrão

O sistema deve ser inicializado com pelo menos um usuário administrador e alguns estudantes. Insira manualmente no banco:

#### Administrador
```sql
INSERT INTO usuarios (id_user, nome, perfil, senha) 
VALUES (1, 'admin', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9');
```

**Login:** `admin`  
**Senha:** `admin123`

#### Estudante (Exemplo)
```sql
INSERT INTO usuarios (id_user, nome, perfil, senha) 
VALUES (2, 'maria', 'user', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9');
```

**Login:** `maria`  
**Senha:** `admin123`

> ⚠️ **Segurança**: As senhas são hasheadas com SHA-256. O hash `240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9` corresponde à senha `admin123`.

---

## 💻 Arquitetura do Sistema

### Padrão MVC com Spring

```
┌──────────────┐      ┌─────────────────────┐      ┌──────────────┐
│     View     │ ───> │    Controller       │ ───> │    Model     │
│    (JSP)     │ <─── │  (Spring MVC)       │ <─── │  (Entity)    │
└──────────────┘      └─────────────────────┘      └──────────────┘
                               │                            │
                               ↓                            ↓
                      ┌─────────────────┐          ┌──────────────┐
                      │   Interceptor   │          │     DAO      │
                      │  (Auth Check)   │          │   (JPA)      │
                      └─────────────────┘          └──────────────┘
                                                            │
                                                            ↓
                                                   ┌──────────────┐
                                                   │  PostgreSQL  │
                                                   └──────────────┘
```

---

## 🎯 Funcionalidades Principais

### 1. Sistema de Autenticação

Hash de senha no **backend** usando Java Security:

```java
public static String hashPassword(String password) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
        for (byte b : encodedhash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Erro ao criptografar senha", e);
    }
}
```

**Interceptor de Autenticação**:
```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    final String URI = request.getRequestURI();
    
    // Permite acesso sem autenticação
    if (URI.endsWith("loginForm") || URI.endsWith("login") || URI.contains("resources"))
        return true;
    
    // Verifica se usuário está na sessão
    if (request.getSession().getAttribute("loggedUser") != null)
        return true;
    
    response.sendRedirect("loginForm");
    return false;
}
```

---

### 2. Gestão de Disciplinas e Matérias

**Exemplo de Cadastro**:
```java
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
        if (topic.getId() > 0) {
            dao.alter(topic);
        } else {
            dao.insert(topic);
        }
        response.put("success", true);
    } catch(Exception e) {
        response.put("success", false);
        response.put("message", "Erro no Banco de Dados: " + e.getMessage());
    }
    return response;
}
```

---

### 3. Banco de Questões

Cada questão possui:
- ✏️ Enunciado
- 🔤 4 alternativas (A, B, C, D)
- ✅ Alternativa correta
- 📚 Disciplina associada
- 📖 Matéria/tópico específico
- 🔘 Status ativo/inativo

**Estrutura da Entidade**:
```java
@Entity
@Table(name = "questoes")
public class Question {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;
    
    @Column(name = "ativo", nullable = false)
    private boolean active = true;
    
    @Size(min = 5, message = "Enunciado muito curto")
    @Column(name = "enunciado", columnDefinition = "TEXT")
    private String statement;
    
    @NotBlank(message = "A alternativa A é obrigatória")
    @Column(name = "alt_a", columnDefinition = "TEXT")
    private String altA;
    
    // ... alternativas B, C, D
    
    @NotBlank(message = "Selecione a alternativa correta")
    @Column(name = "alternativa_correta", length = 1)
    private String correctAlternative;
    
    @ManyToOne
    @JoinColumn(name = "id_disciplina")
    private Subject subject;
    
    @ManyToOne
    @JoinColumn(name = "id_materia")
    private Topic topic;
}
```

---

### 4. Geração Aleatória de Testes

O sistema gera automaticamente um teste com **5 questões aleatórias** baseado em:
- 📚 Disciplina escolhida
- 📅 Bimestre selecionado

```java
@ResponseBody
@PostMapping("generateQuiz")
public Map<String, Object> generateQuiz(
    @RequestParam("subjectId") int subjectId, 
    @RequestParam("term") int term, 
    HttpSession session
) {
    Map<String, Object> response = new HashMap<>();
    EntityManager em = new EntityManagerProvider().getEntityManager();
    
    try {
        // Busca questões disponíveis
        List<Question> availableQuestions = em.createQuery(
            "SELECT q FROM Question q WHERE q.subject.id = :sId AND q.topic.term = :term", 
            Question.class
        )
        .setParameter("sId", subjectId)
        .setParameter("term", term)
        .getResultList();
        
        // Valida quantidade mínima
        if(availableQuestions.size() < 5) {
            response.put("success", false);
            response.put("message", "Não há questões suficientes.");
            return response;
        }
        
        // Busca o tópico
        Topic topic = em.createQuery(
            "SELECT t FROM Topic t WHERE t.subject.id = :sId AND t.term = :term", 
            Topic.class
        )
        .setParameter("sId", subjectId)
        .setParameter("term", term)
        .setMaxResults(1)
        .getSingleResult();
        
        // Embaralha e seleciona 5 questões
        Collections.shuffle(availableQuestions);
        List<Question> selectedQuestions = availableQuestions.subList(0, 5);
        
        // Cria o teste
        User loggedUser = (User) session.getAttribute("loggedUser");
        
        Quiz quiz = new Quiz();
        quiz.setDate(Calendar.getInstance());
        quiz.setUser(loggedUser);
        quiz.setTopic(topic);
        quiz.setQuestions(selectedQuestions);
        quiz.setTotalQuestions(5);
        quiz.setScore(0);
        
        // Persiste
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
```

---

### 5. Realização e Correção de Testes

**Submissão do Teste**:
```java
@ResponseBody
@PostMapping("submitQuiz")
public Map<String, Object> submitQuiz(
    @RequestParam("quizId") int quizId, 
    @RequestParam Map<String, String> allParams, 
    HttpSession session
) {
    Map<String, Object> response = new HashMap<>();
    EntityManager em = new EntityManagerProvider().getEntityManager();
    
    try {
        Quiz quiz = em.find(Quiz.class, quizId);
        List<Question> questions = quiz.getQuestions();
        int score = 0;
        StringBuilder answersSaved = new StringBuilder();
        
        // Corrige cada questão
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
        
        // Atualiza teste com pontuação
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
```

---

### 6. Relatórios e Estatísticas

**Para Estudantes**:
- 📋 Histórico de testes realizados
- 📈 Pontuação de cada teste
- 📊 Percentual de acertos

**Para Administradores**:
- 👥 Desempenho de todos os estudantes
- 📊 Estatísticas por aluno
- 🎯 Taxa média de acertos
- 📉 Análise de desempenho por disciplina

```java
@RequestMapping("adminReports")
public String adminReports(@RequestParam(value="userId", required=false) Integer userId, Model model) {
    EntityManager em = new EntityManagerProvider().getEntityManager();
    
    try {
        // Busca estudante selecionado
        User student = userId != null ? em.find(User.class, userId) : null;
        model.addAttribute("selectedStudent", student);
        
        // Lista todos os estudantes
        List<User> students = em.createQuery(
            "SELECT u FROM User u WHERE u.profile = 'user' ORDER BY u.name", 
            User.class
        ).getResultList();
        model.addAttribute("students", students);
        
        // Se um estudante foi selecionado, busca seus testes
        if (student != null) {
            List<Quiz> quizzes = em.createQuery(
                "SELECT DISTINCT q FROM Quiz q " +
                "JOIN FETCH q.questions " +
                "JOIN FETCH q.topic t " +
                "JOIN FETCH t.subject " +
                "WHERE q.user.id = :uId " +
                "ORDER BY q.date DESC", 
                Quiz.class
            )
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
        }
        
    } catch(Exception e) {
        System.out.println("Erro ao buscar relatórios: " + e.getMessage());
        e.printStackTrace();
    } finally {
        em.close();
    }
    
    return "aleatorious/admin-reports-fragment";
}
```

---

## 🎨 Interface do Usuário

O sistema possui uma interface moderna e responsiva com:

- 🎨 **Single Page Application (SPA)** com AJAX
- ✨ **Navegação dinâmica** sem recarregamento de página
- 🔍 **Formulários modais** para cadastros
- ✅ **Validação em tempo real** dos formulários
- 🎯 **Dashboard diferenciado** por perfil (Admin/Estudante)
- 🔄 **Feedback visual** de operações (sucesso/erro)

### Navegação AJAX

```javascript
function loadSection(route) {
    const contextPath = '${pageContext.request.contextPath}';
    const urlCompleta = contextPath + "/" + route;
    
    $("#ajax-content").html("<p>Carregando...</p>");
    
    $.get(urlCompleta, function(data) {
        // Sucesso: Substitui o conteúdo da div pelo fragmento JSP
        $("#ajax-content").html(data);
    }).fail(function(jqXHR) {
        // Erro: Avisa o usuário se a rota falhar
        console.error("Erro:", jqXHR.status, jqXHR.responseText);
        $("#ajax-content").html("<p style='color:red'>Erro ao carregar conteúdo.</p>");
    });
}
```

---

## 🔒 Segurança

### Medidas Implementadas

- ✅ **Hash SHA-256** de senhas no backend
- ✅ **Interceptor de Autenticação** em todas as rotas protegidas
- ✅ **Validação de Sessão** via HttpSession
- ✅ **Prepared Statements** via JPA (proteção contra SQL Injection)
- ✅ **Validação de Dados** com Bean Validation
- ✅ **Tratamento de Exceções** com try-catch em operações críticas
- ✅ **Controle de Acesso** baseado em perfil (admin/user)

---

## 📊 Modelo de Dados

### Diagrama de Relacionamentos

```
┌──────────────┐       ┌──────────────┐
│   Subject    │       │     User     │
│ (Disciplina) │       │  (Usuário)   │
└──────┬───────┘       └──────┬───────┘
       │ 1                    │ 1
       │                      │
       │ N                    │ N
┌──────┴───────┐       ┌──────┴───────┐
│    Topic     │       │     Quiz     │
│  (Matéria)   │◄──────┤   (Teste)    │
└──────┬───────┘  N:1  └──────┬───────┘
       │ 1                     │
       │                       │ N
       │ N                     │
┌──────┴───────┐               │ M
│   Question   │───────────────┘
│  (Questão)   │      M:N
└──────────────┘
```

---

## 🎓 Casos de Uso

### Fluxo Admin

1. **Login** com credenciais de administrador
2. **Cadastrar Disciplinas** (ex: Matemática, Português)
3. **Cadastrar Matérias** vinculadas às disciplinas (ex: Frações - 5º ano - 2º bimestre)
4. **Cadastrar Questões** para cada matéria
5. **Cadastrar Estudantes** (perfil "user")
6. **Visualizar Relatórios** de desempenho dos estudantes

### Fluxo Estudante

1. **Login** com credenciais de estudante
2. **Gerar Teste** selecionando disciplina e bimestre
3. **Realizar Teste** respondendo as 5 questões
4. **Submeter Respostas** para correção automática
5. **Visualizar Resultado** imediato (pontuação)
6. **Consultar Histórico** no menu "Meus Relatórios"

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📝 Licença

Este projeto é um trabalho acadêmico desenvolvido para a disciplina de **Desenvolvimento de Aplicações Web** do curso de **Sistemas para Internet**.

---

## 👨‍💻 Autor

**Hugo Vinícius Rodrigues Pereira**

[![GitHub](https://img.shields.io/badge/GitHub-hugovrp-black?style=flat-square&logo=github)](https://github.com/hugovrp)