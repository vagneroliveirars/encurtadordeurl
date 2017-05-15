package br.com.linx.services;

import java.net.URI;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import br.com.linx.model.rest.Usuarios;
import br.com.linx.util.ShortURL;

@Stateless
@Path("/users")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class UsuarioService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@GET
	public Response findAll() {
		List<Usuario> usuarios = this.entityManager
				.createQuery("select u from Usuario u", Usuario.class)				
				.getResultList();
		
		return Response.ok(new Usuarios(usuarios)).build();
	}
	
	@GET
	@Path("/{id}")
	public Response find(@PathParam("id") String id) {
		Usuario usuario = this.entityManager.find(Usuario.class, id);
		
		if (usuario != null) {
			return Response.ok(usuario).build();			
		}
		
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@POST
	public Response create(@Context UriInfo uriInfo, Usuario usuario) {
		if (this.entityManager.find(Usuario.class, usuario.getId()) != null) {
			return Response.status(Status.CONFLICT).build();
		}
		
		this.entityManager.persist(usuario);
		
		UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
		URI location = uriBuilder.path("/{id}").build(usuario.getId());
		
		return Response.created(location).build();
	}
	
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
		
		String shortUrl = ShortURL.encode(url.getId().intValue()); 
		
		UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
		URI location = uriBuilder.path("/{shortUrl}").build(shortUrl);
		
		url.setShortUrl(location.toString());
		
		url = this.entityManager.merge(url);
		
		return Response.status(Status.CREATED).entity(url).build();
	}	
	
}
