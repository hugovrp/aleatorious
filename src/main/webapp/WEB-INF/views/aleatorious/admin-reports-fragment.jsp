<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="card">
    <div class="card-header-flex">
        <h3><i class="fas fa-chart-bar"></i> Relatórios de Desempenho das Alunas</h3>
    </div>
    <hr>

    <div class="filter-container">
        <label class="filter-label-bold">Filtrar por Aluna:</label>
        <select id="studentFilter" onchange="filterByStudent(this.value)" class="filter-select">
            <option value="">Todas as Alunas (Visão Geral)</option>
            <c:forEach var="s" items="${students}">
                <option value="${s.id}" ${selectedStudent != null && selectedStudent.id == s.id ? 'selected' : ''}>
                    ${s.name}
                </option>
            </c:forEach>
        </select>
    </div>

    <c:if test="${empty selectedStudent}">
        <h4 class="mt-30 color-dark">Resumo Geral por Aluna</h4>
        
        <c:if test="${empty students}">
            <div class="empty-state-message-detailed">
                <p>Nenhuma aluna cadastrada ainda. Vá em "Usuários" para cadastrar.</p>
            </div>
        </c:if>

        <c:if test="${not empty students}">
            <table class="mt-15">
                <thead>
                    <tr class="thead-dark">
                        <th>Aluna</th>
                        <th class="td-center">Total de Testes</th>
                        <th class="td-center">Aproveitamento Médio</th>
                        <th class="td-center">Último Teste</th>
                        <th class="td-center">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="student" items="${students}">
                        <c:set var="studentTests" value="${0}" />
                        <c:set var="totalScore" value="${0}" />
                        <c:set var="totalQuestions" value="${0}" />
                        <c:set var="lastDate" value="" />
                        
                        <c:forEach var="quiz" items="${allQuizzes}">
                            <c:if test="${quiz.user.id == student.id}">
                                <c:set var="studentTests" value="${studentTests + 1}" />
                                <c:set var="totalScore" value="${totalScore + quiz.score}" />
                                <c:set var="totalQuestions" value="${totalQuestions + quiz.totalQuestions}" />
                                <c:if test="${empty lastDate}">
                                    <c:set var="lastDate" value="${quiz.date}" />
                                </c:if>
                            </c:if>
                        </c:forEach>
                        
                        <tr>
                            <td>
                                <strong class="color-dark">${student.name}</strong>
                            </td>
                            <td class="td-center">
                                <span class="score-display">
                                    ${studentTests}
                                </span>
                            </td>
                            <td class="td-center">
                                <c:choose>
                                    <c:when test="${studentTests > 0}">
                                        <c:set var="avgPercent" value="${(totalScore / totalQuestions) * 100}" />
                                        <div class="inline-block text-center">
                                            <div class="progress-bar-100">
                                                <div style="background: ${avgPercent >= 60 ? '#27ae60' : avgPercent >= 40 ? '#f39c12' : '#e74c3c'}; 
                                                            width: ${avgPercent}%; height: 100%;"></div>
                                            </div>
                                            <strong class="mt-5" style="color: ${avgPercent >= 60 ? '#27ae60' : avgPercent >= 40 ? '#f39c12' : '#e74c3c'}; display: block;">
                                                <fmt:formatNumber value="${avgPercent}" maxFractionDigits="1"/>%
                                            </strong>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-light-gray">-</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="td-center">
                                <c:choose>
                                    <c:when test="${not empty lastDate}">
                                        <fmt:formatDate value="${lastDate.time}" pattern="dd/MM/yyyy"/>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-light-gray">Nenhum</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="td-center">
                                <button onclick="filterByStudent(${student.id})" class="btn-primary-small">
                                    Ver Detalhes
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </c:if>

    <c:if test="${not empty selectedStudent}">
        <div class="report-detail-header">
            <h4>Relatório Detalhado: ${selectedStudent.name}</h4>
            
            <c:if test="${not empty totalTests}">
                <div class="report-stats-grid">
                    <div class="report-stat-item">
                        <div class="report-stat-value">${totalTests}</div>
                        <div class="report-stat-label">Testes Realizados</div>
                    </div>
                    <div class="report-stat-item">
                        <div class="report-stat-value">
                            <fmt:formatNumber value="${averagePercent}" maxFractionDigits="1" minFractionDigits="1"/>%
                        </div>
                        <div class="report-stat-label">Aproveitamento Médio</div>
                    </div>
                    <div class="report-stat-item">
                        <div class="report-stat-value">
                            <c:choose>
                                <c:when test="${averagePercent >= 70}">Ótimo</c:when>
                                <c:when test="${averagePercent >= 50}">Bom</c:when>
                                <c:otherwise>Precisa Melhorar</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="report-stat-label">Desempenho</div>
                    </div>
                </div>
            </c:if>
        </div>

        <h4 class="mt-20 color-dark">Histórico de Testes</h4>
        
        <c:choose>
            <c:when test="${empty studentQuizzes}">
                <div class="empty-state-message-detailed">
                    <p class="text-large">Esta aluna ainda não realizou nenhum teste.</p>
                </div>
            </c:when>
            <c:otherwise>
                <table class="mt-15">
                    <thead>
                        <tr class="thead-gradient">
                            <th>Data/Hora</th>
                            <th>Disciplina / Matéria</th>
                            <th class="td-center">Respostas</th>
                            <th class="td-center">Nota</th>
                            <th class="td-center">Aproveitamento</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="quiz" items="${studentQuizzes}">
                            <tr>
                                <td>
                                    <fmt:formatDate value="${quiz.date.time}" pattern="dd/MM/yyyy"/>
                                    <br>
                                    <small class="text-light-gray">
                                        <fmt:formatDate value="${quiz.date.time}" pattern="HH:mm"/>
                                    </small>
                                </td>
                                <td>
                                    <strong class="color-dark">${quiz.topic.subject.name}</strong><br>
                                    <small class="text-light-gray">${quiz.topic.name} - ${quiz.topic.term}º Bimestre</small>
                                </td>
                                <td class="td-center text-monospace">
                                    ${quiz.studentAnswers}
                                </td>
                                <td class="td-center">
                                    <span class="text-xlarge font-bold color-dark">
                                        ${quiz.score}
                                    </span> 
                                    <span class="text-light-gray">/ ${quiz.totalQuestions}</span>
                                </td>
                                <td class="td-center">
                                    <c:set var="percent" value="${(quiz.score * 100) / quiz.totalQuestions}" />
                                    
                                    <div class="progress-bar-100-thin">
                                        <div style="background: ${percent >= 70 ? '#27ae60' : percent >= 50 ? '#f39c12' : '#e74c3c'}; 
                                                    width: ${percent}%; height: 100%; transition: width 0.3s;"></div>
                                    </div>
                                    <strong class="font-bold text-large"
                                           style="color: ${percent >= 70 ? '#27ae60' : percent >= 50 ? '#f39c12' : '#e74c3c'}">
                                        <fmt:formatNumber value="${percent}" maxFractionDigits="0"/>%
                                    </strong>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>
</div>

<script>
	function filterByStudent(userId) {
		// Altera a rota para buscar relatórios de uma aluna específica
	    // e recarrega o fragmento na div principal.
	    if (userId === "") {
	        loadSection('adminReports');
	    } else {
	        loadSection('adminReportsByStudent?userId=' + userId);
	    }
	}
</script>