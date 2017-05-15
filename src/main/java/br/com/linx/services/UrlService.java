package br.com.linx.services;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.linx.model.Url;

/**
 * Serviço REST para URL
 * 
 * @author vagner
 *
 */
@Stateless
@Path("/urls")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class UrlService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * Retorna um 301 redirect para o endereço original da URL
	 * 
	 * @param id
	 * @return Response
	 */
	@GET
	@Path("/{id}")
	public Response find(@PathParam("id") Long id) {
		Url url = this.entityManager.find(Url.class, id);
		
		if (url != null) {
			// Incrementa os hits da url
			Long hits = url.getHits();
			hits++;
			url.setHits(hits);
			
			url = this.entityManager.merge(url);
			
			// Redireciona
			try {
				URI location = new URI(url.getUrl());
				return Response.status(Status.MOVED_PERMANENTLY).location(location).build();
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return Response.serverError().build();
			}
		}
		
		return Response.status(Status.NOT_FOUND).build();
	}
	
	/**
	 * Apaga uma URL do sistema
	 * 
	 * @param id
	 * @return Response
	 */
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") Long id) {
		Url url = this.entityManager.find(Url.class, id);
		
		if (url == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		this.entityManager.remove(url);
		
		return Response.noContent().build();
	}
	
}
