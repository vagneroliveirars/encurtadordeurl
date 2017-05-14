package br.com.linx.model.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.linx.model.Usuario;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Usuarios {
	
	@XmlElement(name = "usuario")
	private List<Usuario> usuarios;
	
	public Usuarios() { }

	public Usuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}
