package br.com.maxwellfn.mynews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import br.com.maxwellfn.mynews.NewsListFragment.OnNewsClickListener;
import br.com.maxwellfn.mynews.TopicListFragment.OnTopicClickListener;

import com.actionbarsherlock.app.ActionBar.Tab;

public class MainActivity extends NewsListsFragmentManager implements
		OnNewsClickListener, OnTopicClickListener {

	public static final String TAG_FRAGMENT_TOPIC_LIST = "topicListFragment";

	TopicListFragment topicListFragment;

	Topic currentTopic = new Topic();
	News currentNews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		setContentView(R.layout.activity_main);

		// Configura fragmento de t—picos

		topicListFragment = (TopicListFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_TOPIC_LIST);

		if (topicListFragment == null) {

			topicListFragment = TopicListFragment.novaInstancia("");
		}

		topicListFragment.setTopicClickListener(this);

		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.topicListFrame, topicListFragment,
						TAG_FRAGMENT_TOPIC_LIST).commit();

		// Fim de configura fragmento de t—picos

		if (isTablet()) {

			// Configura fragmento de favoritas

			favoriteNewsListFragment = (FavoriteNewsListFragment) getSupportFragmentManager()
					.findFragmentByTag(TAG_FRAGMENT_FAVORITE_NEWS);

			if (favoriteNewsListFragment == null) {
				favoriteNewsListFragment = FavoriteNewsListFragment
						.novaInstancia();
			}

			favoriteNewsListFragment.setNewsClickListener(this);

			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.newsListsFrame, favoriteNewsListFragment,
							TAG_FRAGMENT_FAVORITE_NEWS).commit();

			// Fim de configura fragmento de favoritos

			initNewsLists();

			CurrentNewsListFragmentData firstNewsListFragmentData = new CurrentNewsListFragmentData(
					newsListFragment1, CLASSIFICATION_POPULARES,
					TAG_FRAGMENT_NEWS_LIST1);

			newsListFragment1 = instanceNewsList(currentTopic, this,
					firstNewsListFragmentData);

			configTabs(true, false, currentTopic);

			recoverInstanceAttr(savedInstanceState, currentTopic, currentNews);

		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (isTablet()) {
			saveInstanceAttr(outState, currentTopic, currentNews);
		}
	}

	@Override
	public void onNewsClick(News news) {

		this.currentNews = news;

		Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(currentNews
				.getUrl()));
		startActivity(it);

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (isTablet()) {
			changeTab(ft, currentTopic, this, isTablet());
		}
	}

	private boolean isTablet() {
		return findViewById(R.id.newsListsFrame) != null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 0) {
			;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTopicClick(Topic topic) {

		this.currentTopic = topic;

		if (isTablet()) {
			setActionBarLogoAndSubTitle(topic);
			replaceNewsLists(currentTopic);

		} else {

			Intent i = new Intent(this, SmartphoneNewsListsActivity.class);
			i.putExtra(SAVED_INSTANCE_CURRENT_TOPIC, currentTopic);
			startActivityForResult(i, 0);
		}

	}

}
