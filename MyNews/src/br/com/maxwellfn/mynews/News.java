package br.com.maxwellfn.mynews;

import java.io.Serializable;

public class News implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1338834187656220329L;

	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String title;

	private String author;

	private String thumbnail;

	private String url;

	private String numComments;

	private String subreddit;

	private String created;

	private String domain;

	private String score;

	private String downs;

	private String ups;

	private String over18;

	private String after;

	private int dbId;
	
	private boolean isFavorite;
	
	

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public int getDbId() {
		return dbId;
	}

	public void setDbId(int dbId) {
		this.dbId = dbId;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	@Override
	public String toString() {
		return title;
	}

	public String getTitle() {
		return getField(title);
	}

	private String getField(String name) {
		return (name != null ? name.trim() : "");
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getThumbnail() {
		if (thumbnail != null && thumbnail.trim().length() > 0
				&& !thumbnail.equalsIgnoreCase("default")) {
			return thumbnail;
		} else {
			return "http://www.reddit.com/static/blog_snoo.png";
		}
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getUrl() {
		return getField(url);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNumComments() {
		return numComments;
	}

	public void setNumComments(String numComments) {
		this.numComments = numComments;
	}

	public String getSubreddit() {
		return getField(subreddit);
	}

	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getDowns() {
		return downs;
	}

	public void setDowns(String downs) {
		this.downs = downs;
	}

	public String getUps() {
		return ups;
	}

	public void setUps(String ups) {
		this.ups = ups;
	}

	public String getOver18() {
		return over18;
	}

	public void setOver18(String over18) {
		this.over18 = over18;
	}

	public News(String title, String subreddit) {
		super();
		this.title = title;
		this.subreddit = subreddit;
	}

	public News() {
		super();
		// TODO Auto-generated constructor stub
	}

}
