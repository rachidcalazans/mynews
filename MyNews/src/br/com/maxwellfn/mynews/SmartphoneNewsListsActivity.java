package br.com.maxwellfn.mynews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import br.com.maxwellfn.mynews.NewsListFragment.OnNewsClickListener;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.MenuItem;

public class SmartphoneNewsListsActivity extends NewsListsFragmentManager
		implements OnNewsClickListener {

	private Topic currentTopic;
	News currentNews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentTopic = (Topic) getIntent().getSerializableExtra("topic");

		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		setContentView(R.layout.activity_lists_news_smartphone);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initNewsLists();

		// createNewsLists(currentTopic, this);

		instanceNewsList(currentTopic, this, newsListFragment1,
				CLASSIFICATION_POPULARES, TAG_FRAGMENT_NEWS_LIST1);

		// addNewsLists();

		configTabs();

		recoverInstanceAttr(savedInstanceState, currentTopic, currentNews);

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		// if (item.getItemId() == android.R.id.home) {
		// finish();
		// }

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
		changeTab(tab, ft, currentTopic, this);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		saveInstanceAttr(outState, currentTopic, currentNews);

	}
}
