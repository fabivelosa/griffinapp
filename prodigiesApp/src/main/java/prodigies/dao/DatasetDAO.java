package prodigies.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import prodigiesApp.FailureEvent;

@Stateless
@LocalBean
public class DatasetDAO {

	@PersistenceContext
	private EntityManager em;
	
	public List<FailureEvent> getAllEvents(){
		Query query = em.createQuery("SELECT n FROM networkfailures n");
		return query.getResultList();
	}
	
	public FailureEvent getevent(int id) {
		return em.find(FailureEvent.class, id);
	}
	
	public void save(FailureEvent failE) {
		em.persist(failE);
	}
	
	public void update(FailureEvent failE) {
		em.merge(failE);
	}
}
