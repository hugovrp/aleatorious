<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="card">
    <h3><i class="fas fa-chart-line"></i> Meus Relatórios de Desempenho</h3>
    <hr>

    <c:choose>
        <c:when test="${empty myQuizzes}">
            <div class="empty-state-message">
                <p>Você ainda não realizou nenhum teste. Vá em "Gerar Teste" para começar!</p>
            </div>
        </c:when>
        <c:otherwise>
            <table class="table-reports">
                <thead>
                    <tr class="thead-dark">
                        <th>Data</th>
                        <th>Disciplina / Matéria</th>
                        <th class="td-center">Respostas</th>
                        <th class="td-center">Nota</th>
                        <th class="td-center">Aproveitamento</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="quiz" items="${myQuizzes}">
                        <tr>
                            <td>
                                <fmt:formatDate value="${quiz.date.time}" pattern="dd/MM/yyyy HH:mm"/>
                            </td>
                            <td>
                                <strong>${quiz.topic.subject.name}</strong><br>
                                <small class="text-gray">${quiz.topic.name} (${quiz.topic.term}º Bimestre)</small>
                            </td>
                            <td class="td-center text-monospace">
                                ${quiz.studentAnswers}
                            </td>
                            <td class="td-center">
                                <span class="text-large font-bold">${quiz.score}</span> / ${quiz.totalQuestions}
                            </td>
                            <td class="td-center">
                                <c:set var="percent" value="${(quiz.score * 100) / quiz.totalQuestions}" />
                                
                                <div class="progress-bar-80">
                                    <div class="progress-fill" style="background: ${percent >= 60 ? '#27ae60' : '#e74c3c'}; width: ${percent}%;"></div>
                                </div>
                                <small class="font-bold" style="color: ${percent >= 60 ? '#27ae60' : '#e74c3c'}">
                                    ${percent}%
                                </small>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>