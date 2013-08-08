package br.com.maxwellfn.mynews;

import java.util.List;

import android.content.Context;

public class DbRepository {

	private Context context;
	private DbTableNews dbTableNews;
	private DbTableTopic dbTableTopic;

	public DbRepository(Context context) {
		this.context = context;
		this.dbTableNews = new DbTableNews(this.context);
		this.dbTableTopic = new DbTableTopic(this.context);
	}

	// =========================================================
	// =============== TABLE NEWS OPERATIONS ===================
	// =========================================================
	public boolean favoriteNews(News news,
			FavoriteNewsListFragment favoriteNewsListFragment) {
		return dbTableNews.favoriteNews(news, favoriteNewsListFragment);
	}

	public void unfavoriteNews(News news,
			FavoriteNewsListFragment favoriteNewsListFragment) {
		dbTableNews.unfavoriteNews(news, favoriteNewsListFragment);
	}

	public void unfavoriteNews(News news,
			FavoriteNewsListFragment favoriteNewsListFragment,
			boolean noConfirmation) {
		dbTableNews.unfavoriteNews(news, favoriteNewsListFragment,
				noConfirmation);
	}

	public News getNewsById(String id, boolean isDbId) {
		return dbTableNews.getNewsById(id, isDbId);
	}

	public News getNewsByDbId(long dbId) {
		return dbTableNews.getNewsByDbId(dbId);
	}

	public News getNewsById(String id) {
		return dbTableNews.getNewsById(id);
	}

	public List<News> getFavoriteNewsList() {
		return dbTableNews.getFavoriteNewsList();
	}

	// =========================================================
	// =============== TABLE TOPIC OPERATIONS ==================
	// =========================================================

	public boolean favoriteTopic(Topic topic,
			TopicListFragment topicListFragment) {
		return dbTableTopic.favoriteTopic(topic, topicListFragment);
	}

	public void unfavoriteTopic(Topic topic, TopicListFragment topicListFragment) {
		dbTableTopic.unfavoriteTopic(topic, topicListFragment);
	}

	public void unfavoriteTopic(Topic topic,
			TopicListFragment topicListFragment, boolean noConfirmation) {
		dbTableTopic.unfavoriteTopic(topic, topicListFragment, noConfirmation);
	}

	public Topic getTopicById(String id, boolean isDbId) {
		return dbTableTopic.getTopicById(id, isDbId);
	}

	public Topic getTopicByDbId(long dbId) {
		return dbTableTopic.getTopicByDbId(dbId);
	}

	public Topic getTopicById(String id) {
		return dbTableTopic.getTopicById(id);
	}

	public List<Topic> getFavoriteTopicList() {
		return dbTableTopic.getFavoriteTopicList();
	}

}
