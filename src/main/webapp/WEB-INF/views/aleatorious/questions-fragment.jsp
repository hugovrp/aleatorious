<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="card">
	<h3>Gerenciar Banco de Questões (Múltipla Escolha)</h3>

	<div class="form-container-light">
	    <form id="questionForm">
	        <input type="hidden" name="id" id="q-id" value="0">
	        
	        <div class="form-grid-2col">
	            <div>
	                <label>Disciplina</label>
	                <select id="q-subject" name="subject.id" required class="w-100" onchange="loadTopicsBySubject(this.value)">
	                    <option value="">Selecione a Disciplina...</option>
	                    <c:forEach var="s" items="${subjects}">
	                        <option value="${s.id}">${s.name}</option>
	                    </c:forEach>
	                </select>
	            </div>
	            <div>
	                <label>Matéria</label>
	                <select id="q-topic" name="topic.id" required class="w-100">
	                    <option value="">Selecione a Disciplina primeiro...</option>
	                </select>
	            </div>
	        </div>
	
	        <label>Enunciado da Questão</label>
	        <textarea name="statement" id="q-statement" rows="3" class="w-100 mb-15" placeholder="Mínimo 5 caracteres..."></textarea>
	
	        <div class="form-grid-2col-alt">
	            <div>
	                <label>Alternativa A</label>
	                <input type="text" name="altA" id="q-altA" class="w-100" required>
	            </div>
	            <div>
	                <label>Alternativa B</label>
	                <input type="text" name="altB" id="q-altB" class="w-100" required>
	            </div>
	            <div>
	                <label>Alternativa C</label>
	                <input type="text" name="altC" id="q-altC" class="w-100" required>
	            </div>
	            <div>
	                <label>Alternativa D</label>
	                <input type="text" name="altD" id="q-altD" class="w-100" required>
	            </div>
	        </div>
	
	        <div class="mb-15">
	            <label><strong>Qual é a alternativa correta?</strong></label>
	            <select name="correctAlternative" id="q-correct" required style="padding: 5px; margin-left: 10px;">
	                <option value="">Escolha...</option>
	                <option value="A">Alternativa A</option>
	                <option value="B">Alternativa B</option>
	                <option value="C">Alternativa C</option>
	                <option value="D">Alternativa D</option>
	            </select>
	        </div>
	
	        <button type="button" onclick="saveQuestion()" class="btn-save">Salvar Questão</button>
	        <button type="reset" onclick="$('#q-id').val(0)" class="btn-cancel">Limpar/Novo</button>
	    </form>
	</div>
	
	<table class="table-bordered">
	    <thead>
	        <tr class="thead-dark">
	            <th>ID</th>
	            <th>Disciplina/Matéria</th>
	            <th>Enunciado</th>
	            <th>Resposta</th>
	            <th>Ações</th>
	        </tr>
	    </thead>
	    <tbody>
	        <c:forEach var="q" items="${questions}">
	        	<c:choose>
	        		<c:when test="${q.active}">
	        			<tr>
			                <td>${q.id}</td>
			                <td><small>${q.subject.name} / ${q.topic.name}</small></td>
			                <td>${q.statement}</td>
			                <td class="td-center font-bold color-green">${q.correctAlternative}</td>
			                <td>
			                    <button class="btn-edit" 
			                        onclick="prepareEdit(this)"
			                        data-id="${q.id}"
			                        data-subject="${q.subject.id}"
			                        data-topic="${q.topic.id}"
			                        data-statement="${q.statement}"
			                        data-alt-a="${q.altA}"
			                        data-alt-b="${q.altB}"
			                        data-alt-c="${q.altC}"
			                        data-alt-d="${q.altD}"
			                        data-correct="${q.correctAlternative}">
			                        Editar
			                    </button>
			                    <button class="btn-delete" onclick="deleteQuestion(${q.id})">Apagar</button>
			                </td>
			            </tr>
	        		</c:when>
	        	</c:choose>
	        </c:forEach>
	    </tbody>
	</table>
</div>

<script>
	function loadTopicsBySubject(subjectId) {
	    if(!subjectId) return;
	    
	    // Busca matérias filtradas por disciplina. O servidor retorna um Array JSON.
	    $.get("${pageContext.request.contextPath}/getTopicsBySubject", { subjectId: subjectId }, function(data) {
	        let options = '<option value="">Selecione a Matéria...</option>';
	        
	        // Itera sobre o JSON para construir o HTML do select de matérias dinamicamente
	        data.forEach(function(topic) {
	            options += '<option value="' + topic.id + '">' + topic.name + '</option>';
	        });
	        $("#q-topic").html(options);
	    });
	}
	
	function saveQuestion() {
	    const formData = $("#questionForm").serialize();
	    
	    // Salva a questão e recarrega o fragmento de listagem
	    $.post("${pageContext.request.contextPath}/saveQuestion", formData, function(res) {
	        if (res.success) {
	            alert("Sucesso!");
	            loadSection('pageQuestions'); 
	        } else {
	            alert("Erro: " + res.message);
	        }
	    });
	}
	
	function prepareEdit(btn) {
	    const id = $(btn).data("id");
	    const subId = $(btn).data("subject");
	    const topId = $(btn).data("topic");
	    
	    $("#q-id").val(id);
	    $("#q-subject").val(subId);
	    $("#q-statement").val($(btn).data("statement"));
	    $("#q-altA").val($(btn).data("alt-a"));
	    $("#q-altB").val($(btn).data("alt-b"));
	    $("#q-altC").val($(btn).data("alt-c"));
	    $("#q-altD").val($(btn).data("alt-d"));
	    $("#q-correct").val($(btn).data("correct"));
	
	    $.get("${pageContext.request.contextPath}/getTopicsBySubject", { subjectId: subId }, function(data) {
	        let options = '<option value="">Selecione a Matéria...</option>';
	        data.forEach(function(topic) {
	            options += '<option value="' + topic.id + '">' + topic.name + '</option>';
	        });
	        $("#q-topic").html(options); 
	        $("#q-topic").val(topId);
	    });
	
	    window.scrollTo({ top: 0, behavior: 'smooth' });
	}
	
	function deleteQuestion(id) {
	    if (confirm("Deseja realmente apagar esta questão?")) {
	        $.post("${pageContext.request.contextPath}/removeQuestion", { id: id }, function(res) {
	            if (res.success) {
	                loadSection('pageQuestions');
	            } else {
	                alert("Erro ao apagar");
	            }
	        });
	    }
	}
</script>