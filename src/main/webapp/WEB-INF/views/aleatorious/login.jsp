<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

	<head>
	    <meta charset="UTF-8">
	    <title>Aleatorious - Login</title>
	    <link type="text/css" href="resources/css/style.css" rel="stylesheet">
	    <script type="text/javascript" src="resources/js/jquery-3.7.1.js"></script>
	</head>
	
	<body class="login-body">
	    <div class="login-container">
		    <h2>Acesso ao Sistema</h2>
		    
		    <div id="error-message" class="alert-error" style="display:none;"></div>
		
		    <form id="loginForm">
		        <div class="input-group">
		            <label>Usuário</label>
		            <input type="text" name="name" id="name" placeholder="Digite seu nome">
		        </div>
		        
		        <div class="input-group">
		            <label>Senha</label>
		            <input type="password" name="password" id="password" placeholder="Digite sua senha">
		        </div>
		        
		        <button type="submit" class="btn-login">Entrar</button>
		    </form>
		</div>
		
		<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		<script>
			$(document).ready(function() {
			    $("#loginForm").submit(function(event) {
			        event.preventDefault(); // Impede o formulário de recarregar a página inteira
			
			        const formData = $(this).serialize(); // Transforma os campos do form em uma string de dados
			        const errorDiv = $("#error-message");
			
			        // Envia os dados via POST para a controller
			        $.post("login", formData, function(response) {
			            if (response.success) {
			            	// Se o servidor validar o login (JSON success: true), redireciona via JS
			                window.location.href = response.redirect;
			            } else {
			            	// Se falhar, exibe a mensagem de erro sem sair da tela de login
			                errorDiv.text(response.message).fadeIn();
			                setTimeout(() => errorDiv.fadeOut(), 3000); 
			            }
			        }, "json"); // Definindo que a resposta esperada do servidor será um objeto JSON
			    });
			});
		</script>
	</body>
</html>