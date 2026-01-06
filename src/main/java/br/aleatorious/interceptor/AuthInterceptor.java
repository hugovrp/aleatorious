package br.aleatorious.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor de autenticação para controlar o acesso às páginas da aplicação.
 * Verifica se o usuário está autenticado antes de permitir o acesso aos recursos.
 * Permite acesso livre apenas às páginas de login e recursos estáticos.
 * 
 * @author Hugo Vinícius Rodrigues Pereira
 * @version 1.0
 */
public class AuthInterceptor implements HandlerInterceptor {

	/**
	 *  Intercepta as requisições antes de serem processadas pelos controllers.
	 *  Verifica se o usuário está autenticado na sessão.
	 * 
	 *  Permite acesso sem autenticação para:
	 *  - URIs que terminam com "loginForm"
	 *  - URIs que terminam com "login"
	 *  - URIs que contêm "resources" (arquivos estáticos)
	 * 
	 *  @param request requisição HTTP recebida
	 *  @param response resposta HTTP a ser enviada
	 *  @param handler handler que processará a requisição
	 *  @return true se a requisição pode prosseguir, false caso contrário
	 *  @throws Exception se ocorrer erro durante o processamento
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
				
		final String URI = request.getRequestURI();	
		
		if (URI.endsWith("loginForm") || URI.endsWith("login") || URI.contains("resources"))
			return true;
			
		if (request.getSession().getAttribute("loggedUser") != null)
			return true;
		
		response.sendRedirect("loginForm");
		
		return false;
	}
}
