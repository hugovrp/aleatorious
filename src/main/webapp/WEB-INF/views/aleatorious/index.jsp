<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <title>Aleatorious - Sistema de Testes</title>
	    <link type="text/css" href="<c:url value='/resources/css/style.css'/>" rel="stylesheet">
	    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	</head>
	<body>
	
		<div class="dashboard-container">
		    <nav class="navbar">
		        <div class="logo" onclick="window.location.reload()">
				    <strong>Aleatorious</strong>
				</div>
		        
		        <ul class="nav-links">
		            <c:if test="${sessionScope.loggedUser.profile == 'admin'}">
		                <li><a onclick="loadSection('pageSubjects')">Disciplinas</a></li>
		                <li><a onclick="loadSection('pageTopics')">Matérias</a></li>
		                <li><a onclick="loadSection('pageQuestions')">Questões</a></li>
		                <li><a onclick="loadSection('pageUsers')">Usuários</a></li>
		                <li><a onclick="loadSection('adminReports')">Relatórios das Alunas</a></li>
		            </c:if>
		            
		            <c:if test="${sessionScope.loggedUser.profile == 'user'}">
		                <li><a onclick="loadSection('showGenerateQuiz')">Gerar Teste</a></li>
		                <li><a onclick="loadSection('listReports')">Meus Relatórios</a></li>
		            </c:if>
		            
		            <li><a href="<c:url value='/logout'/>" class="logout-link">Sair</a></li>
		        </ul>
		    </nav>
		
		    <main class="main-content">
		        <div id="ajax-content" class="card-painel">
		            <h2>Bem-vinda, ${sessionScope.loggedUser.name}!</h2>
		            <p>Selecione uma opção no menu superior para gerenciar o sistema.</p>
		        </div>
		    </main>
		</div>
		
		<script>
			// AJAX: Esta é a função mestre do sistema. 
		    // Ela recebe uma rota (ex: 'pageSubjects'), faz uma requisição GET ao servidor
		    // e injeta o HTML retornado dentro da div #ajax-content.
			function loadSection(route) {
			    const contextPath = '${pageContext.request.contextPath}';
			    const urlCompleta = contextPath + "/" + route;
			
			    $("#ajax-content").html("<p>Carregando...</p>");
			    
			    $.get(urlCompleta, function(data) {
			    	// Sucesso: Substitui o conteúdo da div pelo fragmento JSP vindo do servidor
			        $("#ajax-content").html(data);
			    }).fail(function(jqXHR) {
			    	// Erro: Avisa o usuário se a rota falhar
			        console.error("Erro detalhes:", jqXHR.status, jqXHR.responseText);
			        $("#ajax-content").html("<p style='color:red'>Erro ao carregar conteúdo. Status: " + jqXHR.status + "</p>");
			    });
			}
		</script>
	</body>
</html>