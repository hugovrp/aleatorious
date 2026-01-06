<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="card">
	<div class="card-header-flex">
	    <h3>Gerenciar Disciplinas</h3>
	</div>
	
	<div id="subject-form-container">
	    <form id="subjectForm">
	        <input type="hidden" name="id" id="subject-id" value="0">
	        <div class="form-flex-row">
	            <div class="form-flex-1">
	                <label>Nome da Disciplina</label>
	                <input type="text" name="name" id="subject-name" placeholder="Ex: Matemática" required>
	            </div>
	            <button type="submit" class="btn-login w-auto h-40" style="padding-top: -1px">Salvar</button>
	            <button type="button" onclick="clearSubjectForm()" class="h-40">Limpar</button>
	        </div>
	    </form>
	</div>
	
	<table>
	    <thead>
	        <tr>
	            <th>ID</th>
	            <th>Nome</th>
	            <th class="td-actions">Ações</th>
	        </tr>
	    </thead>
	    <tbody id="subject-table-body">
	        <c:forEach var="s" items="${subjects}">
	            <tr>
	                <td>${s.id}</td>
	                <td>${s.name}</td>
	                <td>
					    <button class="btn-edit" onclick="editSubject(${s.id}, '${s.name}')">Editar</button>
					    <button class="btn-delete" onclick="deleteSubject(${s.id})">Excluir</button>
					</td>
	            </tr>
	        </c:forEach>
	    </tbody>
	</table>
</div>

<script>
	$(document).ready(function() {
	    $("#subjectForm").submit(function(e) {
	        e.preventDefault();
	        
	       // Envia os dados da nova disciplina ou edição para o servidor
	        $.post("${pageContext.request.contextPath}/saveSubject", $(this).serialize(), function(response) {
	            console.log(response); 
	            if (response.success) {
	                alert("Disciplina salva com sucesso!");
	                // Chama a função global para recarregar apenas a tabela de disciplinas
	                loadSection('pageSubjects'); 
	            } else {
	                alert("Erro: " + response.message);
	            }
	        }, "json");
	    });
	});
	
	function editSubject(id, name) {
	    $("#subject-id").val(id);
	    $("#subject-name").val(name).focus();
	    window.scrollTo({ top: 0, behavior: 'smooth' });
	}
	
	function deleteSubject(id) {
	    if (confirm("Tem certeza que deseja excluir esta disciplina?")) {
	    	// Envia o ID para remoção e, em caso de sucesso, atualiza a tela
	        $.post("${pageContext.request.contextPath}/removeSubject", { id: id }, function(response) {
	            if (response.success) {
	                loadSection('pageSubjects');
	            } else {
	                alert(response.message);
	            }
	        }, "json");
	    }
	}
	
	function clearSubjectForm() {
	    $("#subject-id").val(0);
	    $("#subject-name").val("");
	}
</script>