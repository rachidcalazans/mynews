package br.com.maxwellfn.mynews;

import java.io.Serializable;

public class News implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1338834187656220329L;

	private String nome;

	private String descricao;

	private String urlFoto;

	private String urlInfo;

	public News() {
		super();
		// TODO Auto-generated constructor stub
	}

	public News(String nome, String descricao, String urlFoto, String urlInfo) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.urlFoto = urlFoto;
		this.urlInfo = urlInfo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}

	public String getUrlInfo() {
		return urlInfo;
	}

	public void setUrlInfo(String urlInfo) {
		this.urlInfo = urlInfo;
	}

	@Override
	public String toString() {
		return nome;
	}

}
