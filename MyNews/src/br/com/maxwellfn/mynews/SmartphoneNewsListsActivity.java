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

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;

public class SmartphoneNewsListsActivity extends NewsListsFragmentManager
		implements OnNewsClickListener, OnScrollListener {

	private Topic currentTopic;
	News currentNews;

	public static boolean isLoadingMoreNewsItens = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentTopic = (Topic) getIntent().getSerializableExtra(
				SAVED_INSTANCE_CURRENT_TOPIC);

		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		setContentView(R.layout.activity_lists_news_smartphone);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Configura fragmento de favoritas

		favoriteNewsListFragment = (FavoriteNewsListFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_FAVORITE_NEWS);

		if (favoriteNewsListFragment == null) {
			favoriteNewsListFragment = FavoriteNewsListFragment.novaInstancia();
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
				firstNewsListFragmentData, false);

		configTabs(false, false, currentTopic);

		recoverInstanceAttr(savedInstanceState, currentTopic, currentNews);

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return super.onMenuItemSelected(featureId, item);
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
		changeTab(ft, currentTopic, false);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		saveInstanceAttr(outState, currentTopic, currentNews);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		News selectedNews = (News) favoriteNewsListFragment.getListView()
				.getItemAtPosition(info.position);

		if (item.getItemId() == R.id.favoriteItemListContextMenuOptionRemove) {

			DbRepository repo = new DbRepository(this);

			repo.remove(selectedNews, favoriteNewsListFragment);

		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		CurrentNewsListFragmentData currentNewsListFragmentData = getCurrentNewsListFragment();
		SherlockListFragment currentListFragment = currentNewsListFragmentData
				.getNewsListFragment();

		if (currentListFragment instanceof NewsListFragment) {

			NewsListFragment currentNewsListFragment = (NewsListFragment) currentListFragment;

			if (firstVisibleItem + visibleItemCount >= totalItemCount
					&& totalItemCount != 0) {
				if (isLoadingMoreNewsItens == false) {
					isLoadingMoreNewsItens = true;
					currentNewsListFragment.searchForNews(
							((News) currentNewsListFragment.getListView()
									.getItemAtPosition(totalItemCount - 1))
									.getAfter(), false);
				}
			}
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}
}
