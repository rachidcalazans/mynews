package br.com.maxwellfn.mynews;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import br.com.maxwellfn.mynews.NewsListFragment.OnNewsClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;

public class NewsListsFragmentManager extends SherlockFragmentActivity
		implements TabListener {

	private static final int NUM_TABS = 6;
	public static final String SAVED_INSTANCE_ABA_SELECIONADA = "abaSelecionada";
	public static final String SAVED_INSTANCE_CURRENT_TOPIC = "current_topic";
	public static final String SAVED_INSTANCE_CURRENT_NEWS = "current_news";

	public static final String CLASSIFICATION_POPULARES = "";
	public static final String CLASSIFICATION_NOVOS = "new/";
	public static final String CLASSIFICATION_SUBINDO = "rising/";
	public static final String CLASSIFICATION_CONTROVERSOS = "controversial/";
	public static final String CLASSIFICATION_NOTOPO = "top/";

	public static final String TAG_FRAGMENT_NEWS_LIST1 = "newsListFragment1";
	public static final String TAG_FRAGMENT_NEWS_LIST2 = "newsListFragment2";
	public static final String TAG_FRAGMENT_NEWS_LIST3 = "newsListFragment3";
	public static final String TAG_FRAGMENT_NEWS_LIST4 = "newsListFragment4";
	public static final String TAG_FRAGMENT_NEWS_LIST5 = "newsListFragment5";
	public static final String TAG_FRAGMENT_FAVORITE_NEWS = "favoriteNewsListFragment";

	public NewsListFragment newsListFragment1;
	public NewsListFragment newsListFragment2;
	public NewsListFragment newsListFragment3;
	public NewsListFragment newsListFragment4;
	public NewsListFragment newsListFragment5;
	public FavoriteNewsListFragment favoriteNewsListFragment;

	ActionBar actionBar;

	public void initNewsLists() {
		newsListFragment1 = (NewsListFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_NEWS_LIST1);
		newsListFragment2 = (NewsListFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_NEWS_LIST2);
		newsListFragment3 = (NewsListFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_NEWS_LIST3);
		newsListFragment4 = (NewsListFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_NEWS_LIST4);
		newsListFragment5 = (NewsListFragment) getSupportFragmentManager()
				.findFragmentByTag(TAG_FRAGMENT_NEWS_LIST5);

	}

	public NewsListFragment instanceNewsList(Topic currentTopic,
			OnNewsClickListener newsClickListener,
			CurrentNewsListFragmentData currentNewsListFragmentData) {

		NewsListFragment newsListFragment = (NewsListFragment) currentNewsListFragmentData
				.getNewsListFragment();

		if (currentNewsListFragmentData.getNewsListFragment() == null) {

			newsListFragment = NewsListFragment.novaInstancia(
					currentTopic.getUrl(),
					currentNewsListFragmentData.getClassification(), "");

		}

		addNewsList(newsListFragment, currentNewsListFragmentData.getTag());

		newsListFragment.setNewsClickListener(newsClickListener);
		return newsListFragment;
	}

	public FavoriteNewsListFragment instanceFavoriteNewsList(
			OnNewsClickListener newsClickListener,
			CurrentNewsListFragmentData currentNewsListFragmentData) {

		FavoriteNewsListFragment favoriteNewsListFragment = (FavoriteNewsListFragment) currentNewsListFragmentData
				.getNewsListFragment();

		if (currentNewsListFragmentData.getNewsListFragment() == null) {

			favoriteNewsListFragment = FavoriteNewsListFragment.novaInstancia();

		}

		addNewsList(favoriteNewsListFragment,
				currentNewsListFragmentData.getTag());

		favoriteNewsListFragment.setNewsClickListener(newsClickListener);
		return favoriteNewsListFragment;
	}

	public void replaceNewsLists(Topic topic) {

		switch (getSupportActionBar().getSelectedTab().getPosition()) {
		case 0:

			getSupportFragmentManager().beginTransaction()
					.remove(newsListFragment1).commit();

			newsListFragment1 = NewsListFragment.novaInstancia(topic.getUrl(),
					CLASSIFICATION_POPULARES, "");

			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.newsListsFrame, newsListFragment1,
							TAG_FRAGMENT_NEWS_LIST1).commit();

			break;
		case 1:

			getSupportFragmentManager().beginTransaction()
					.remove(newsListFragment2).commit();

			newsListFragment2 = NewsListFragment.novaInstancia(topic.getUrl(),
					CLASSIFICATION_NOVOS, "");
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.newsListsFrame, newsListFragment2,
							TAG_FRAGMENT_NEWS_LIST2).commit();
			break;
		case 2:

			getSupportFragmentManager().beginTransaction()
					.remove(newsListFragment3).commit();

			newsListFragment3 = NewsListFragment.novaInstancia(topic.getUrl(),
					CLASSIFICATION_SUBINDO, "");
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.newsListsFrame, newsListFragment3,
							TAG_FRAGMENT_NEWS_LIST3).commit();
			break;
		case 3:

			getSupportFragmentManager().beginTransaction()
					.remove(newsListFragment4).commit();

			newsListFragment4 = NewsListFragment.novaInstancia(topic.getUrl(),
					CLASSIFICATION_CONTROVERSOS, "");
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.newsListsFrame, newsListFragment4,
							TAG_FRAGMENT_NEWS_LIST4).commit();
			break;
		case 4:

			getSupportFragmentManager().beginTransaction()
					.remove(newsListFragment5).commit();

			newsListFragment5 = NewsListFragment.novaInstancia(topic.getUrl(),
					CLASSIFICATION_NOTOPO, "");
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.newsListsFrame, newsListFragment5,
							TAG_FRAGMENT_NEWS_LIST5).commit();
			break;
		case 5:

			getSupportFragmentManager().beginTransaction()
					.remove(favoriteNewsListFragment).commit();

			favoriteNewsListFragment = FavoriteNewsListFragment.novaInstancia();
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.newsListsFrame, favoriteNewsListFragment,
							TAG_FRAGMENT_FAVORITE_NEWS).commit();
			break;
		}

	}

	private boolean hasChangedCurrentTopic(NewsListFragment newsListFragment,
			Topic currentTopic) {
		if (newsListFragment != null
				&& newsListFragment.getCurrentTopicUrl() != null
				&& !newsListFragment.getCurrentTopicUrl().equals(
						currentTopic.getUrl())) {
			return true;

		} else {
			return false;
		}
	}

	public CurrentNewsListFragmentData getCurrentNewsListFragment() {

		CurrentNewsListFragmentData currentNewsListFragmentData = null;
		int position = actionBar.getSelectedTab().getPosition();

		switch (position) {
		case 0:
			currentNewsListFragmentData = new CurrentNewsListFragmentData(
					newsListFragment1, CLASSIFICATION_POPULARES,
					TAG_FRAGMENT_NEWS_LIST1);

			break;
		case 1:
			currentNewsListFragmentData = new CurrentNewsListFragmentData(
					newsListFragment2, CLASSIFICATION_NOVOS,
					TAG_FRAGMENT_NEWS_LIST2);
			break;
		case 2:
			currentNewsListFragmentData = new CurrentNewsListFragmentData(
					newsListFragment3, CLASSIFICATION_SUBINDO,
					TAG_FRAGMENT_NEWS_LIST3);
			break;
		case 3:
			currentNewsListFragmentData = new CurrentNewsListFragmentData(
					newsListFragment4, CLASSIFICATION_CONTROVERSOS,
					TAG_FRAGMENT_NEWS_LIST4);
			break;
		case 4:
			currentNewsListFragmentData = new CurrentNewsListFragmentData(
					newsListFragment5, CLASSIFICATION_NOTOPO,
					TAG_FRAGMENT_NEWS_LIST5);
			break;
		case 5:
			// Favoritas!
			currentNewsListFragmentData = new CurrentNewsListFragmentData(
					favoriteNewsListFragment, null, TAG_FRAGMENT_FAVORITE_NEWS);
			break;
		default:
			break;
		// N‹o Ž para chega aqui nunca!

		}

		return currentNewsListFragmentData;
	}

	class CurrentNewsListFragmentData {

		private SherlockListFragment newsListFragment;

		private String classification;

		private String tag;

		public CurrentNewsListFragmentData(
				SherlockListFragment newsListFragment, String classification,
				String tag) {
			super();
			this.newsListFragment = newsListFragment;
			this.classification = classification;
			this.tag = tag;
		}

		public SherlockListFragment getNewsListFragment() {
			return newsListFragment;
		}

		public void setNewsListFragment(SherlockListFragment newsListFragment) {
			this.newsListFragment = newsListFragment;
		}

		public String getClassification() {
			return classification;
		}

		public void setClassification(String classification) {
			this.classification = classification;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

	}

	public void updateCurrentNewsListFragment(FragmentTransaction ft,
			SherlockListFragment newsListFragmentUpdated) {

		int position = actionBar.getSelectedTab().getPosition();

		switch (position) {
		case 0:

			newsListFragment1 = (NewsListFragment) newsListFragmentUpdated;
			ft.show(newsListFragment1);
			break;
		case 1:

			newsListFragment2 = (NewsListFragment) newsListFragmentUpdated;
			ft.show(newsListFragment2);
			break;
		case 2:

			newsListFragment3 = (NewsListFragment) newsListFragmentUpdated;
			ft.show(newsListFragment3);
			break;
		case 3:

			newsListFragment4 = (NewsListFragment) newsListFragmentUpdated;
			ft.show(newsListFragment4);
			break;
		case 4:
			newsListFragment5 = (NewsListFragment) newsListFragmentUpdated;
			ft.show(newsListFragment5);
			break;
		case 5:
			favoriteNewsListFragment = (FavoriteNewsListFragment) newsListFragmentUpdated;
			ft.show(favoriteNewsListFragment);
			break;
		default:
			break;
		// N‹o Ž para chega aqui nunca!

		}

		if (newsListFragment1 != null
				&& newsListFragment1 != newsListFragmentUpdated) {
			ft.hide(newsListFragment1);
		}
		if (newsListFragment2 != null
				&& newsListFragment2 != newsListFragmentUpdated) {
			ft.hide(newsListFragment2);
		}
		if (newsListFragment3 != null
				&& newsListFragment3 != newsListFragmentUpdated) {
			ft.hide(newsListFragment3);
		}
		if (newsListFragment4 != null
				&& newsListFragment4 != newsListFragmentUpdated) {
			ft.hide(newsListFragment4);
		}
		if (newsListFragment5 != null
				&& newsListFragment5 != newsListFragmentUpdated) {
			ft.hide(newsListFragment5);
		}
		if (favoriteNewsListFragment != null
				&& favoriteNewsListFragment != newsListFragmentUpdated) {
			ft.hide(favoriteNewsListFragment);
		}

	}

	public void changeTab(FragmentTransaction ft, Topic currentTopic,
			OnNewsClickListener newsClickListener, boolean isTablet) {

		int position = actionBar.getSelectedTab().getPosition();

		CurrentNewsListFragmentData currentNewsListFragmentData = getCurrentNewsListFragment();

		setTabText(isTablet, true);

		if (currentNewsListFragmentData.getNewsListFragment() instanceof NewsListFragment
				&& hasChangedCurrentTopic(
						(NewsListFragment) currentNewsListFragmentData
								.getNewsListFragment(),
						currentTopic)) {

			replaceNewsLists(currentTopic);

		} else {

			switch (position) {
			case 0:
				newsListFragment1 = instanceNewsList(currentTopic,
						newsClickListener, currentNewsListFragmentData);
				updateCurrentNewsListFragment(ft, newsListFragment1);
				break;
			case 1:
				newsListFragment2 = instanceNewsList(currentTopic,
						newsClickListener, currentNewsListFragmentData);
				updateCurrentNewsListFragment(ft, newsListFragment2);
				break;
			case 2:
				newsListFragment3 = instanceNewsList(currentTopic,
						newsClickListener, currentNewsListFragmentData);
				updateCurrentNewsListFragment(ft, newsListFragment3);
				break;
			case 3:
				newsListFragment4 = instanceNewsList(currentTopic,
						newsClickListener, currentNewsListFragmentData);
				updateCurrentNewsListFragment(ft, newsListFragment4);
				break;
			case 4:
				newsListFragment5 = instanceNewsList(currentTopic,
						newsClickListener, currentNewsListFragmentData);
				updateCurrentNewsListFragment(ft, newsListFragment5);
				break;
			case 5:
				favoriteNewsListFragment = instanceFavoriteNewsList(
						newsClickListener, currentNewsListFragmentData);
				updateCurrentNewsListFragment(ft, favoriteNewsListFragment);
				break;
			default:
				break;
			// N‹o Ž para chega aqui nunca!

			}
		}

	}

	public void addNewsList(SherlockListFragment newsListFragment,
			String fragmentTag) {

		getSupportFragmentManager().beginTransaction()
				.add(R.id.newsListsFrame, newsListFragment, fragmentTag)
				.hide(newsListFragment).commit();

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

	}

	public static final String TAB_NOME_GRANDE_POPULARES = "populares";
	public static final String TAB_NOME_GRANDE_NOVOS = "novos";
	public static final String TAB_NOME_GRANDE_SUBINDO = "subindo";
	public static final String TAB_NOME_GRANDE_CONTROVERSOS = "controversos";
	public static final String TAB_NOME_GRANDE_NOTOPO = "no topo";
	public static final String TAB_NOME_GRANDE_FAVORITAS = "favoritas";

	public static final String TAB_NOME_ABREV_POPULARES = "pop...";
	public static final String TAB_NOME_ABREV_NOVOS = "nov...";
	public static final String TAB_NOME_ABREV_SUBINDO = "sub...";
	public static final String TAB_NOME_ABREV_CONTROVERSOS = "con...";
	public static final String TAB_NOME_ABREV_NOTOPO = "top...";
	public static final String TAB_NOME_ABREV_FAVORITAS = "fav...";

	public void setTabText(boolean isTablet, boolean forceBigName) {

		setTabText(isTablet, forceBigName, null);

	}

	public void setTabText(boolean isTablet, boolean forceBigName,
			Integer tabPosition) {

		String tabBigName = "";
		String tabShortName = "";
		int position;

		if (tabPosition == null) {
			position = (actionBar.getSelectedTab() != null ? actionBar
					.getSelectedTab().getPosition() : 0);
		} else {
			position = tabPosition.intValue();
		}

		switch (position) {
		case 0:
			tabBigName = TAB_NOME_GRANDE_POPULARES;
			tabShortName = TAB_NOME_ABREV_POPULARES;
			break;
		case 1:
			tabBigName = TAB_NOME_GRANDE_NOVOS;
			tabShortName = TAB_NOME_ABREV_NOVOS;
			break;
		case 2:
			tabBigName = TAB_NOME_GRANDE_SUBINDO;
			tabShortName = TAB_NOME_ABREV_SUBINDO;
			break;
		case 3:
			tabBigName = TAB_NOME_GRANDE_CONTROVERSOS;
			tabShortName = TAB_NOME_ABREV_CONTROVERSOS;
			break;
		case 4:
			tabBigName = TAB_NOME_GRANDE_NOTOPO;
			tabShortName = TAB_NOME_ABREV_NOTOPO;
			break;
		case 5:
			tabBigName = TAB_NOME_GRANDE_FAVORITAS;
			tabShortName = TAB_NOME_ABREV_FAVORITAS;
			break;
		default:
			break;

		}

		if (isTablet || forceBigName) {

			// Configura nome abreviado para todas as tabs
			// quando n‹o for tablet.
			if (!isTablet && forceBigName
					&& actionBar.getTabCount() >= NUM_TABS) {

				for (int i = 0; i < actionBar.getTabCount(); i++) {
					setTabText(false, false, i);
				}

			}

			actionBar.getTabAt(position).setText(tabBigName);

		} else {

			actionBar.getTabAt(position).setText(tabShortName);

		}

	}

	public void configTabs(boolean isTablet, boolean forceBigName,
			Topic currentTopic) {
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		setActionBarLogoAndSubTitle(currentTopic);

		Tab tabs[] = new Tab[NUM_TABS];

		for (int i = 0; i < tabs.length; i++) {
			tabs[i] = actionBar.newTab().setTabListener(this);
			actionBar.addTab(tabs[i]);
			setTabText(isTablet, (i == 0 ? true : false), i);

			// Configura‹o especial para a tab favoritas.
			if (i == tabs.length - 1) {
				actionBar.getTabAt(i).setIcon(
						android.R.drawable.btn_star_big_on);
			}
		}

	}

	public void saveInstanceAttr(Bundle outState, Topic currentTopic,
			News currentNews) {
		outState.putInt(SAVED_INSTANCE_ABA_SELECIONADA, getSupportActionBar()
				.getSelectedNavigationIndex());
		outState.putSerializable(SAVED_INSTANCE_CURRENT_NEWS, currentNews);
		outState.putSerializable(SAVED_INSTANCE_CURRENT_TOPIC, currentTopic);
	}

	public void recoverInstanceAttr(Bundle savedInstanceState,
			Topic currentTopic, News currentNews) {
		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState
					.getInt(SAVED_INSTANCE_ABA_SELECIONADA));
			currentNews = (News) savedInstanceState
					.getSerializable(SAVED_INSTANCE_CURRENT_NEWS);
			currentTopic = (Topic) savedInstanceState
					.getSerializable(SAVED_INSTANCE_CURRENT_TOPIC);
		}
	}

	public static boolean isURLValid(String url) {
		if (url != null && url.trim().length() > 0 && !url.contains(" ")
				&& url.startsWith("http")) {
			return true;
		} else {
			return false;
		}
	}

	public void setActionBarLogoAndSubTitle(Topic topic) {

		actionBar.setSubtitle(topic.getDisplayName());

		InputStream in = null;
		Drawable d = null;
		try {
			if (isURLValid(topic.getHeaderImg())) {
				in = new java.net.URL(topic.getHeaderImg()).openStream();
				Bitmap bitmapTopicLogo = BitmapFactory.decodeStream(in);
				d = new BitmapDrawable(getResources(), bitmapTopicLogo);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (d != null) {
			actionBar.setDisplayUseLogoEnabled(true);
			actionBar.setLogo(d);
		} else {
			actionBar.setDisplayUseLogoEnabled(false);

		}
	}
}
