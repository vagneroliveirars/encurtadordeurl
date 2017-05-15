package br.com.linx.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Classe que representa um usuário
 * 
 * @author vagner
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@Entity
public class Usuario {
	
	@Id
	private String id;
	
	@OneToMany(cascade = {CascadeType.ALL})
	@XmlTransient
	private List<Url> urls;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Url> getUrls() {
		return urls;
	}

	public void setUrls(List<Url> urls) {
		this.urls = urls;
	}

}
