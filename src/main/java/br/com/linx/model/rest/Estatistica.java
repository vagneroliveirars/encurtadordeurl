package br.com.linx.model.rest;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.linx.model.Url;

/**
 * Classe que representa uma estatística
 * 
 * @author vagner
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Estatistica {

	private Long hits;

	private Long urlCount;

	private List<Url> topUrls;
	
	public Estatistica() {}
	
	public Estatistica(Long hits, Long urlCount, List<Url> topUrls) {
		this.hits = hits;
		this.urlCount = urlCount;
		this.topUrls = topUrls;
	}
	
	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public Long getUrlCount() {
		return urlCount;
	}

	public void setUrlCount(Long urlCount) {
		this.urlCount = urlCount;
	}

	public List<Url> getTopUrls() {
		return topUrls;
	}

	public void setTopUrls(List<Url> topUrls) {
		this.topUrls = topUrls;
	}

}
