package br.com.linx.model.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import br.com.linx.model.Url;

@XmlRootElement
public class Urls {
	
	private List<Url> urls = new ArrayList<Url>();
	
	public Urls() {}
	
	public Urls(List<Url> urls) {
		this.urls = urls;
	}

	public List<Url> getUrls() {
		return urls;
	}

	public void setUrls(List<Url> urls) {
		this.urls = urls;
	}

}
