<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <title>Aleatorious - Página Não Encontrada</title>
	    <link type="text/css" href="<c:url value='/resources/css/style.css'/>" rel="stylesheet">
	</head>
	<body>
		<div class="error-container">
		    <div class="logo-text">Aleatorious</div>
		    
		    <div class="error-content">
		        <h1 class="error-code">404</h1>
		        <h2 class="error-title">Página Não Encontrada</h2>
		        <p class="error-message">
		            Desculpe, a página que você está procurando não existe ou foi movida.<br>
		            Verifique o endereço digitado ou retorne à página inicial.
		        </p>
		        
		        <div class="button-group">
		            <a href="javascript:history.back()" class="btn-back">Voltar</a>
		        </div>
		    </div>
		</div>
	</body>
</html>
