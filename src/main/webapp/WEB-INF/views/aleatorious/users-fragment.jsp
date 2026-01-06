<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="card">
	<h3>Gerenciar Usuários (Admin)</h3>
	
	<div class="form-container-bg">
	    <form id="userForm">
		    <input type="hidden" name="id" id="u-id" value="0">
		    <input type="hidden" name="profile" value="user">
		    
		    <div class="form-grid-2col">
		        <div>
		            <label>Nome da Filha (Login)</label>
		            <input type="text" name="name" id="u-name" required class="w-100">
		        </div>
		        <div>
		            <label>Senha</label>
		            <input type="password" name="password" id="u-password" required class="w-100">
		        </div>
		    </div>
		    <div class="form-actions">
		        <button type="submit" class="btn-login w-auto">Cadastrar Usuária</button>
		        <button type="button" onclick="clearUserForm()">Limpar</button>
		    </div>
		</form>
	</div>
	
	<table>
	    <thead>
	        <tr>
	            <th>Nome</th>
	            <th>Perfil</th>
	            <th>Ações</th>
	        </tr>
	    </thead>
	    <tbody>
		    <c:forEach var="u" items="${users}">
		        <tr>
		            <td>${u.name}</td>
		            <td>
		                <span class="badge ${u.profile == 'admin' ? 'bg-warning' : 'bg-info'}">
		                    ${u.profile == 'admin' ? 'Administradora' : 'Usuária'}
		                </span>
		            </td>
		            <td>
		                <c:if test="${u.profile != 'admin'}">
		                    <button onclick="editUser(${u.id}, '${u.name}')">Editar</button>
		                    <button class="btn-red" onclick="deleteUser(${u.id})">Excluir</button>
		                </c:if>
		                <c:if test="${u.profile == 'admin'}">
		                    <small class="protected-label">(Protegido)</small>
		                </c:if>
		            </td>
		        </tr>
		    </c:forEach>
		</tbody>
	</table>
</div>

<script>
	// Intercepta o envio do formulário de usuário
	$(document).off("submit", "#userForm").on("submit", "#userForm", function(e) {
	    e.preventDefault(); // Impede o recarregamento da página
	    
	    // Envia os dados serializados via POST para a rota de salvamento
	    $.post("${pageContext.request.contextPath}/saveUser", $(this).serialize(), function(res) {
	        if(res.success) {
	        	// Se houver sucesso, recarrega apenas o fragmento de usuários para atualizar a tabela
	            loadSection('pageUsers'); 
	            alert("Usuária cadastrada com sucesso!");
	        } else {
	            alert(res.message);
	        }
	    }, "json");
	});
	
	function editUser(id, name, profile) {
	    $("#u-id").val(id);
	    $("#u-name").val(name);
	    $("#u-profile").val(profile);
	    $("#u-password").attr("required", false); 
	    $("#u-password").attr("placeholder", "Deixe em branco para manter a atual");
	    window.scrollTo({ top: 0, behavior: 'smooth' });
	}
	
	function deleteUser(id) {
	    if (confirm("Deseja excluir este usuário?")) {
	    	// Envia uma requisição POST com o ID do usuário para remoção
	        $.post("${pageContext.request.contextPath}/removeUser", { id: id }, function(res) {
	            if(res.success) loadSection('pageUsers');
	        }, "json");
	    }
	}
	
	function clearUserForm() {
	    $("#u-id").val(0);
	    $("#u-name").val("");
	    $("#u-password").val("");
	}
</script>