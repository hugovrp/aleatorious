<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="card">
	<h3>Gerenciar Matérias</h3>
	
	<div id="topic-form-container">
	    <form id="topicForm">
	        <input type="hidden" name="id" id="topic-id" value="0">
	        
	        <div class="form-grid-4col">
	            <div>
	                <label>Nome da Matéria</label>
	                <input type="text" name="name" id="topic-name" required class="w-100">
	            </div>
	            <div>
	                <label>Série (Ano)</label>
	                <select name="grade" id="topic-grade" class="w-100">
	                    <option value="1">1º Ano</option>
	                    <option value="2">2º Ano</option>
	                </select>
	            </div>
	            <div>
	                <label>Bimestre</label>
	                <select name="term" id="topic-term" class="w-100">
	                    <option value="1">1º</option>
	                    <option value="2">2º</option>
	                    <option value="3">3º</option>
	                    <option value="4">4º</option>
	                </select>
	            </div>
	            <div>
	                <label>Disciplina</label>
	                <select name="subject.id" id="topic-subject-id" required class="w-100">
	                    <option value="">Selecione...</option>
	                    <c:forEach var="s" items="${subjects}">
	                        <option value="${s.id}">${s.name}</option>
	                    </c:forEach>
	                </select>
	            </div>
	        </div>
	        <div class="form-actions">
	            <button type="submit" class="btn-login w-auto">Salvar Matéria</button>
	            <button type="button" onclick="clearTopicForm()">Limpar</button>
	        </div>
	    </form>
	</div>
	
	<table>
	    <thead>
	        <tr>
	            <th>Matéria</th>
	            <th>Disciplina</th>
	            <th>Série</th>
	            <th>Bimestre</th>
	            <th>Ações</th>
	        </tr>
	    </thead>
	    <tbody>
	        <c:forEach var="t" items="${topics}">
	            <tr>
	                <td>${t.name}</td>
	                <td>${t.subject.name}</td>
	                <td>${t.grade}º Ano</td>
	                <td>${t.term}º</td>
	                <td>
	                    <button onclick="editTopic(${t.id}, '${t.name}', ${t.grade}, ${t.term}, ${t.subject.id})">Editar</button>
	                    <button class="btn-red" onclick="deleteTopic(${t.id})">Excluir</button>
	                </td>
	            </tr>
	        </c:forEach>
	    </tbody>
	</table>
</div>

<script>
	$(document).ready(function() {
		// Intercepta o envio do formulário de matérias
	    $("#topicForm").submit(function(e) {
	        e.preventDefault(); // Evita o refresh
	        
	        // Envia os dados para a Controller salvar a matéria
	        $.post("${pageContext.request.contextPath}/saveTopic", $(this).serialize(), function(response) {
	            if (response.success) {
	            	// Recarrega o fragmento de matérias para mostrar os dados atualizados
	                loadSection('pageTopics');
	            } else {
	                alert(response.message);
	            }
	        }, "json");
	    });
	});
	
	function editTopic(id, name, grade, term, subjectId) {
	    $("#topic-id").val(id);
	    $("#topic-name").val(name);
	    $("#topic-grade").val(grade);
	    $("#topic-term").val(term);
	    $("#topic-subject-id").val(subjectId);
	    window.scrollTo({ top: 0, behavior: 'smooth' });
	}
	
	function deleteTopic(id) {
	    if (confirm("Deseja excluir esta matéria?")) {
	    	// Solicita ao servidor a remoção da matéria via POST
	        $.post("${pageContext.request.contextPath}/removeTopic", { id: id }, function(response) {
	            if (response.success) loadSection('pageTopics'); // Atualiza a interface recarregando a seção
	            else alert(response.message);
	        }, "json");
	    }
	}
	
	function clearTopicForm() {
	    $("#topicForm")[0].reset();
	    $("#topic-id").val(0);
	}
</script>