<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="card">
    <h2>Resolvendo Teste #${quiz.id}</h2>
    
    <c:if test="${not empty quiz}">
        <p>Assunto: ${quiz.topic.subject.name} - MatÃ©ria: ${quiz.topic.name}</p>
        <hr>
        
        <form id="quizForm">
            <input type="hidden" name="quizId" value="${quiz.id}">
            
            <c:forEach var="q" varStatus="status" items="${quiz.questions}">
                <div class="quiz-question-container">
                    <p><strong>QuestÃ£o ${status.count}:</strong> ${q.statement}</p>
                    
                    <div class="quiz-options">
                        <label class="opt-label"><input type="radio" name="answers[${status.index}]" value="A" required> A) ${q.altA}</label>
                        <label class="opt-label"><input type="radio" name="answers[${status.index}]" value="B"> B) ${q.altB}</label>
                        <label class="opt-label"><input type="radio" name="answers[${status.index}]" value="C"> C) ${q.altC}</label>
                        <label class="opt-label"><input type="radio" name="answers[${status.index}]" value="D"> D) ${q.altD}</label>
                    </div>
                </div>
            </c:forEach>

            <button type="submit" class="btn-login w-auto" style="padding: 10px 20px;">Finalizar</button>
        </form>
    </c:if>
    <c:if test="${empty quiz}">
        <p style="color: red;">Erro ao carregar os dados do teste. ID recebido: ${param.id}</p>
    </c:if>
</div>

<script>
	$(document).off("submit", "#quizForm").on("submit", "#quizForm", function(e) {
	    e.preventDefault();
	    if(!confirm("Deseja encerrar o teste e salvar suas respostas?")) return;
	
	    $.post("${pageContext.request.contextPath}/submitQuiz", $(this).serialize(), function(res) {
	        if(res.success) {
	            alert("Teste concluÃ­do! VocÃª acertou " + res.score + " de " + res.total);
	            
	            // Após finalizar o teste, pula para a tela de relatórios
	            loadSection('listReports'); 
	        } else {
	            alert("Erro ao processar: " + res.message);
	        }
	    });
	});
</script>