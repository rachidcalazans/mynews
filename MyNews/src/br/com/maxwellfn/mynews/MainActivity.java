package br.com.maxwellfn.mynews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import br.com.maxwellfn.mynews.NewsListFragment.OnNewsClickListener;
import br.com.maxwellfn.mynews.TopicListFragment.OnTopicClickListener;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockListFragment;

public class MainActivity extends NewsListsFragmentManager implements
		OnNewsClickListener, OnTopicClickListener, OnScrollListener {

	// -dns-server 10.0.192.18,10.193.12.40 -http-proxy
	// CORREIOSNET\80129633:cagada!123Ect2@10.193.112.18:80
	//

	public static final String TAG_FRAGMENT_TOPIC_LIST = "topicListFragment";

	public TopicListFragment topicListFragment;

	public static boolean isLoadingMoreNewsItens = false;
	public static boolean isLoadingMoreTopicItens = false;

	Topic currentTopic = new Topic();
	News currentNews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		setContentView(R.layout.activity_main);

		// Configura fragmento de tópicos

		topicListFragment = (TopicListFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_TOPIC_LIST);

		if (topicListFragment == null) {

			topicListFragment = TopicListFragment.novaInstancia("");
		}

		topicListFragment.setTopicClickListener(this);
		topicListFragment.setEndLessScrollListener(this);

		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.topicListFrame, topicListFragment,
						TAG_FRAGMENT_TOPIC_LIST).commit();

		// Fim de configura fragmento de tópicos

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

			newsListFragment1 = instanceNewsList(currentTopic,
					firstNewsListFragmentData, true);

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
	public boolean onContextItemSelected(android.view.MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		News selectedNews = (News) favoriteNewsListFragment.getListView()
				.getItemAtPosition(info.position);

		if (item.getItemId() == R.id.favoriteItemListContextMenuOptionRemove) {

			DbRepository repo = new DbRepository(this);

			repo.unfavoriteNews(selectedNews, favoriteNewsListFragment);

		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (isTablet()) {
			changeTab(ft, currentTopic, isTablet());
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
			new DownloadLogoTopicImageTask().execute(currentTopic
					.getHeaderImg());
			actionBar.setSubtitle(currentTopic.getDisplayName());
			replaceNewsLists(currentTopic, true);

		} else {

			Intent i = new Intent(this, SmartphoneNewsListsActivity.class);
			i.putExtra(SAVED_INSTANCE_CURRENT_TOPIC, currentTopic);
			startActivityForResult(i, 0);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (isTablet()) {
			getMenuInflater().inflate(R.menu.context_menu, menu);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (view == topicListFragment.getListView()) {
			// O scrolling foi na lista de tópicos

			if (firstVisibleItem + visibleItemCount == totalItemCount
					&& totalItemCount != 0) {
				if (isLoadingMoreTopicItens == false) {
					isLoadingMoreTopicItens = true;
					topicListFragment.searchForTopics(
							((Topic) topicListFragment.getListView()
									.getItemAtPosition(totalItemCount - 1))
									.getAfter(), false);
				}
			}

		} else {

			// Programação defensiva, pois em smartphones nunca deve chegar
			// aqui!
			if (isTablet()) {

				CurrentNewsListFragmentData currentNewsListFragmentData = getCurrentNewsListFragment();
				SherlockListFragment currentListFragment = currentNewsListFragmentData
						.getNewsListFragment();

				// O scrolling foi em alguma das listas de News.

				if (currentListFragment instanceof NewsListFragment) {

					NewsListFragment currentNewsListFragment = (NewsListFragment) currentListFragment;

					if (firstVisibleItem + visibleItemCount == totalItemCount
							&& totalItemCount != 0) {
						if (isLoadingMoreNewsItens == false) {
							isLoadingMoreNewsItens = true;
							currentNewsListFragment.searchForNews(
									((News) currentNewsListFragment
											.getListView().getItemAtPosition(
													totalItemCount - 1))
											.getAfter(), false);
						}
					}
				}
			}

		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

}
