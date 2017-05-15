package br.com.linx.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.linx.model.Url;
import br.com.linx.model.rest.Estatistica;

/**
 * Serviço REST para estatísticas
 * 
 * @author vagner
 *
 */
@Stateless
@Path("/stats")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class EstatisticasService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * Retorna estatísticas globais do sistema
	 * 
	 * @return Response
	 */
	@GET
	public Response findAll() {
		Long hits = this.entityManager.createQuery(
				"select sum(u.hits) from Url u", Long.class).getSingleResult();
		
		Long urlCount = this.entityManager.createQuery(
				"select count(u) from Url u", Long.class).getSingleResult();
		
		List<Url> topUrls = this.entityManager
				.createQuery("select u from Url u order by u.hits desc", Url.class).setMaxResults(10).getResultList();
		
		Estatistica estatistica = new Estatistica(hits, urlCount, topUrls);
		
		return Response.ok(estatistica).build();
	}
	
	/**
	 * Retorna estatísticas de uma URL específica
	 * 
	 * @param id
	 * @return Response
	 */
	@GET
	@Path("/{id}")
	public Response find(@PathParam("id") Long id) {
		Url url = this.entityManager.find(Url.class, id);
		
		if (url != null) {
			return Response.ok(url).build();
		}
		
		return Response.status(Status.NOT_FOUND).build();
	}

}
