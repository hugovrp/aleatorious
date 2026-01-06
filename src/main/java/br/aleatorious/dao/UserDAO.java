package br.aleatorious.dao;

import java.util.List;

import br.aleatorious.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 *  Data Access Object para a entidade User.
 *  Gerencia operações específicas relacionadas a usuários, incluindo validação de login.
 * 
 *  @author Hugo Vinícius Rodrigues Pereira
 *  @version 1.0
 */
public class UserDAO {
	/**
	 *  EntityManager para realizar operações no banco de dados.
	 */
	private EntityManager entityManager;
	
	/**
	 *  Construtor padrão que inicializa o EntityManager.
	 */
	public UserDAO() {
		entityManager = new EntityManagerProvider().getEntityManager();
	}
	
	/**
	 *  Valida as credenciais de login de um usuário.
	 *  Busca no banco de dados um usuário com o nome e senha fornecidos.
	 * 
	 *  @param user objeto User contendo nome e senha para validação
	 *  @return User encontrado no banco de dados ou null se não encontrado
	 *  @throws IllegalArgumentException se o usuário fornecido for nulo
	 */
	public User validateLogin(User user) {
	    if(user == null) throw new IllegalArgumentException("Usuário não deve ser nulo");
	    
	    Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.name = :nome AND u.password = :senha", User.class);
	    
	    query.setParameter("nome", user.getName());
	    query.setParameter("senha", user.getPassword());
	    
	    @SuppressWarnings("unchecked")
		List<User> users = query.getResultList();
	    
	    // Retorna o usuário encontrado ou null se não existir
	    return users.isEmpty() ? null : users.get(0);
	}
}