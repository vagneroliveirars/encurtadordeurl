package br.com.linx.services;

import java.net.URI;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import br.com.linx.model.Url;
import br.com.linx.model.Usuario;
import br.com.linx.model.rest.Estatistica;
import br.com.linx.util.ShortURL;

/**
 * Serviço REST para usuários
 * 
 * @author vagner
 *
 */
@Stateless
@Path("/users")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class UsuarioService {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Cria um usuário
	 * 
	 * @param usuario
	 * @return Response
	 */
	@POST
	public Response create(Usuario usuario) {
		if (this.entityManager.find(Usuario.class, usuario.getId()) != null) {
			return Response.status(Status.CONFLICT).build();
		}
		
		this.entityManager.persist(usuario);
		
		return Response.status(Status.CREATED).entity(usuario).build();
	}
	
	/**
	 * Apaga um usuário
	 * 
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		Usuario usuario = this.entityManager.find(Usuario.class, id);
		
		if (usuario == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		this.entityManager.remove(usuario);
		
		return Response.noContent().build();
	}
	
	/**
	 * Cadastra uma nova url no sistema
	 * 
	 * @param uriInfo
	 * @param idUsuario
	 * @param url
	 * @return Response
	 */
	@POST
	@Path("/{id}/urls")
	public Response createUrl(@Context UriInfo uriInfo, @PathParam("id") String idUsuario, Url url) {
		Usuario usuario = this.entityManager.find(Usuario.class, idUsuario);
		
		if (usuario == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		url.setUsuario(usuario);
		url.setHits(0l);
		
		url = this.entityManager.merge(url);
		
		// Encurta a url
		String shortUrl = ShortURL.encode(url.getId().intValue()); 
		
		UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
		URI location = uriBuilder.path("/{shortUrl}").build(shortUrl);
		
		url.setShortUrl(location.toString());
		
		url = this.entityManager.merge(url);
		
		return Response.status(Status.CREATED).entity(url).build();
	}
	
	/**
	 * Retorna estatísticas das urls de um usuário
	 * 
	 * @return Response
	 */
	@GET
	@Path("/{id}/stats")
	public Response findStatsByUser(@PathParam("id") String idUsuario) {
		Usuario usuario = this.entityManager.find(Usuario.class, idUsuario);
		
		if (usuario == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		TypedQuery<Long> hitsQuery = this.entityManager.createQuery(
				"select sum(u.hits) from Url u where u.usuario=:usuario", Long.class).setParameter("usuario", usuario);		
		Long hits = hitsQuery.getSingleResult();
		
		TypedQuery<Long> urlCountQuery = this.entityManager.createQuery(
				"select count(u) from Url u where u.usuario=:usuario", Long.class).setParameter("usuario", usuario);		
		Long urlCount = urlCountQuery.getSingleResult();
		
		TypedQuery<Url> topUrlsQuery = this.entityManager
				.createQuery(
						"select u from Url u where u.usuario=:usuario order by u.hits desc",
						Url.class).setParameter("usuario", usuario).setMaxResults(10);
		List<Url> topUrls = topUrlsQuery.getResultList();
		
		Estatistica estatistica = new Estatistica(hits, urlCount, topUrls);
		
		return Response.ok(estatistica).build();
	}
	
}
