<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="card">
    <h3>Gerar Novo Teste Aleatorio</h3>
    
    <form id="generateQuizForm">
        <div class="mb-15">
            <label>Disciplina:</label>
            <select name="subjectId" required class="w-100" style="padding: 8px;">
                <option value="">-- Selecione --</option>
                <c:forEach var="s" items="${subjects}">
                    <option value="${s.id}">${s.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-15">
            <label>Bimestre:</label>
            <select name="term" required class="w-100" style="padding: 8px;">
                <option value="1">1o Bimestre</option>
                <option value="2">2o Bimestre</option>
                <option value="3">3o Bimestre</option>
                <option value="4">4o Bimestre</option>
            </select>
        </div>

        <button type="submit" class="btn-login w-auto" style="background: #27ae60;">
            Gerar Teste
        </button>
    </form>
</div>

<script>
	$(document).off("submit", "#generateQuizForm").on("submit", "#generateQuizForm", function(e) {
	    e.preventDefault();
	    
	    const url = "${pageContext.request.contextPath}/generateQuiz";
	
	    // Se o teste foi gerado no banco, carrega a tela de execução do teste (takeQuiz)
	    $.post(url, $(this).serialize(), function(res) {
	        if(res.success) {
	            alert("QuestÃ£o sorteada! Iniciando teste...");
	            loadSection('takeQuiz?id=' + res.quizId);
	        } else {
	            alert(res.message);
	        }
	    }, "json").fail(function() {
	        alert("Erro 404 ou 500: Verifique se a URL " + url + " existe no Controller.");
	    });
	});
</script>