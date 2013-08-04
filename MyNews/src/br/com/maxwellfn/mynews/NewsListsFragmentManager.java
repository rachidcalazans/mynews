package br.com.maxwellfn.mynews;

import android.os.Bundle;
import br.com.maxwellfn.mynews.NewsListFragment.OnNewsClickListener;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class NewsListsFragmentManager extends SherlockFragmentActivity
		implements TabListener {

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

	public NewsListFragment newsListFragment1;
	public NewsListFragment newsListFragment2;
	public NewsListFragment newsListFragment3;
	public NewsListFragment newsListFragment4;
	public NewsListFragment newsListFragment5;

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
			NewsListFragment newsListFragment, String classification,
			String fragmentTag) {

		if (newsListFragment == null) {

			newsListFragment = NewsListFragment.novaInstancia(
					currentTopic.getUrl(), classification, "");

		}

		addNewsList(newsListFragment, fragmentTag);

		newsListFragment.setNewsClickListener(newsClickListener);
		return newsListFragment;
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
							TAG_FRAGMENT_NEWS_LIST1).addToBackStack(null)
					.commit();

			break;
		case 1:

			getSupportFragmentManager().beginTransaction()
					.remove(newsListFragment2).commit();

			newsListFragment2 = NewsListFragment.novaInstancia(topic.getUrl(),
					CLASSIFICATION_NOVOS, "");
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.newsListsFrame, newsListFragment2,
							TAG_FRAGMENT_NEWS_LIST2).addToBackStack(null)
					.commit();
			break;
		case 2:

			getSupportFragmentManager().beginTransaction()
					.remove(newsListFragment3).commit();

			newsListFragment3 = NewsListFragment.novaInstancia(topic.getUrl(),
					CLASSIFICATION_SUBINDO, "");
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.newsListsFrame, newsListFragment3,
							TAG_FRAGMENT_NEWS_LIST3).addToBackStack(null)
					.commit();
			break;
		case 3:

			getSupportFragmentManager().beginTransaction()
					.remove(newsListFragment4).commit();

			newsListFragment4 = NewsListFragment.novaInstancia(topic.getUrl(),
					CLASSIFICATION_CONTROVERSOS, "");
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.newsListsFrame, newsListFragment4,
							TAG_FRAGMENT_NEWS_LIST4).addToBackStack(null)
					.commit();
			break;
		case 4:

			getSupportFragmentManager().beginTransaction()
					.remove(newsListFragment5).commit();

			newsListFragment5 = NewsListFragment.novaInstancia(topic.getUrl(),
					CLASSIFICATION_NOTOPO, "");
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.newsListsFrame, newsListFragment5,
							TAG_FRAGMENT_NEWS_LIST5).addToBackStack(null)
					.commit();
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

	public CurrentNewsListFragmentData getCurrentNewsListFragment(Tab tab) {

		CurrentNewsListFragmentData currentNewsListFragment = null;

		switch (tab.getPosition()) {
		case 0:
			currentNewsListFragment = new CurrentNewsListFragmentData(
					newsListFragment1, CLASSIFICATION_POPULARES,
					TAG_FRAGMENT_NEWS_LIST1);

			break;
		case 1:
			currentNewsListFragment = new CurrentNewsListFragmentData(
					newsListFragment2, CLASSIFICATION_NOVOS,
					TAG_FRAGMENT_NEWS_LIST2);
			break;
		case 2:
			currentNewsListFragment = new CurrentNewsListFragmentData(
					newsListFragment3, CLASSIFICATION_SUBINDO,
					TAG_FRAGMENT_NEWS_LIST3);
			break;
		case 3:
			currentNewsListFragment = new CurrentNewsListFragmentData(
					newsListFragment4, CLASSIFICATION_CONTROVERSOS,
					TAG_FRAGMENT_NEWS_LIST4);
			break;
		case 4:
			currentNewsListFragment = new CurrentNewsListFragmentData(
					newsListFragment5, CLASSIFICATION_NOTOPO,
					TAG_FRAGMENT_NEWS_LIST5);
			break;
		default:
			break;
		// N‹o Ž para chega aqui nunca!

		}

		return currentNewsListFragment;
	}

	class CurrentNewsListFragmentData {

		private NewsListFragment newsListFragment;

		private String classification;

		private String tag;

		public CurrentNewsListFragmentData(NewsListFragment newsListFragment,
				String classification, String tag) {
			super();
			this.newsListFragment = newsListFragment;
			this.classification = classification;
			this.tag = tag;
		}

		public NewsListFragment getNewsListFragment() {
			return newsListFragment;
		}

		public void setNewsListFragment(NewsListFragment newsListFragment) {
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

	public void updateCurrentNewsListFragment(Tab tab, FragmentTransaction ft,
			NewsListFragment newsListFragmentUpdated) {

		switch (tab.getPosition()) {
		case 0:

			newsListFragment1 = newsListFragmentUpdated;
			ft.show(newsListFragment1);
			break;
		case 1:

			newsListFragment2 = newsListFragmentUpdated;
			ft.show(newsListFragment2);
			break;
		case 2:

			newsListFragment3 = newsListFragmentUpdated;
			ft.show(newsListFragment3);
			break;
		case 3:

			newsListFragment4 = newsListFragmentUpdated;
			ft.show(newsListFragment4);
			break;
		case 4:

			newsListFragment5 = newsListFragmentUpdated;
			ft.show(newsListFragment5);
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

	}

	public void changeTab(Tab tab, FragmentTransaction ft, Topic currentTopic,
			OnNewsClickListener newsClickListener) {

		CurrentNewsListFragmentData currentNewsListFragmentData = getCurrentNewsListFragment(tab);

		if (hasChangedCurrentTopic(
				currentNewsListFragmentData.getNewsListFragment(), currentTopic)) {

			replaceNewsLists(currentTopic);

		} else {

			switch (tab.getPosition()) {
			case 0:
				newsListFragment1 = instanceNewsList(currentTopic,
						newsClickListener,
						currentNewsListFragmentData.getNewsListFragment(),
						currentNewsListFragmentData.getClassification(),
						currentNewsListFragmentData.getTag());
				updateCurrentNewsListFragment(tab, ft, newsListFragment1);
				break;
			case 1:
				newsListFragment2 = instanceNewsList(currentTopic,
						newsClickListener,
						currentNewsListFragmentData.getNewsListFragment(),
						currentNewsListFragmentData.getClassification(),
						currentNewsListFragmentData.getTag());
				updateCurrentNewsListFragment(tab, ft, newsListFragment2);
				break;
			case 2:
				newsListFragment3 = instanceNewsList(currentTopic,
						newsClickListener,
						currentNewsListFragmentData.getNewsListFragment(),
						currentNewsListFragmentData.getClassification(),
						currentNewsListFragmentData.getTag());
				updateCurrentNewsListFragment(tab, ft, newsListFragment3);
				break;
			case 3:
				newsListFragment4 = instanceNewsList(currentTopic,
						newsClickListener,
						currentNewsListFragmentData.getNewsListFragment(),
						currentNewsListFragmentData.getClassification(),
						currentNewsListFragmentData.getTag());
				updateCurrentNewsListFragment(tab, ft, newsListFragment4);
				break;
			case 4:
				newsListFragment5 = instanceNewsList(currentTopic,
						newsClickListener,
						currentNewsListFragmentData.getNewsListFragment(),
						currentNewsListFragmentData.getClassification(),
						currentNewsListFragmentData.getTag());
				updateCurrentNewsListFragment(tab, ft, newsListFragment5);
				break;
			default:
				break;
			// N‹o Ž para chega aqui nunca!

			}
		}

		//
		// if (tab.getPosition() == 0) {
		//
		// if (hasChangedCurrentTopic(newsListFragment1, currentTopic)) {
		//
		// replaceNewsLists(currentTopic);
		//
		// } else {
		//
		// newsListFragment1 = instanceNewsList(currentTopic,
		// newsClickListener, newsListFragment1,
		// CLASSIFICATION_POPULARES, TAG_FRAGMENT_NEWS_LIST1);
		// }
		//
		// ft.show(newsListFragment1);
		//
		// if (newsListFragment2 != null) {
		// ft.hide(newsListFragment2);
		// }
		//
		// if (newsListFragment3 != null) {
		// ft.hide(newsListFragment3);
		// }
		// if (newsListFragment4 != null) {
		// ft.hide(newsListFragment4);
		// }
		// if (newsListFragment5 != null) {
		// ft.hide(newsListFragment5);
		// }
		//
		// } else if (tab.getPosition() == 1) {
		//
		// newsListFragment2 = instanceNewsList(currentTopic,
		// newsClickListener, newsListFragment2, CLASSIFICATION_NOVOS,
		// TAG_FRAGMENT_NEWS_LIST2);
		//
		// ft.show(newsListFragment2);
		//
		// if (newsListFragment1 != null) {
		// ft.hide(newsListFragment1);
		// }
		//
		// if (newsListFragment3 != null) {
		// ft.hide(newsListFragment3);
		// }
		// if (newsListFragment4 != null) {
		// ft.hide(newsListFragment4);
		// }
		// if (newsListFragment5 != null) {
		// ft.hide(newsListFragment5);
		// }
		//
		// } else if (tab.getPosition() == 2) {
		//
		// newsListFragment3 = instanceNewsList(currentTopic,
		// newsClickListener, newsListFragment3,
		// CLASSIFICATION_SUBINDO, TAG_FRAGMENT_NEWS_LIST3);
		//
		// ft.show(newsListFragment3);
		//
		// if (newsListFragment1 != null) {
		// ft.hide(newsListFragment1);
		// }
		//
		// if (newsListFragment2 != null) {
		// ft.hide(newsListFragment2);
		// }
		// if (newsListFragment4 != null) {
		// ft.hide(newsListFragment4);
		// }
		// if (newsListFragment5 != null) {
		// ft.hide(newsListFragment5);
		// }
		//
		// } else if (tab.getPosition() == 3) {
		//
		// newsListFragment4 = instanceNewsList(currentTopic,
		// newsClickListener, newsListFragment4,
		// CLASSIFICATION_CONTROVERSOS, TAG_FRAGMENT_NEWS_LIST4);
		//
		// ft.show(newsListFragment4);
		// if (newsListFragment1 != null) {
		// ft.hide(newsListFragment1);
		// }
		//
		// if (newsListFragment2 != null) {
		// ft.hide(newsListFragment2);
		// }
		// if (newsListFragment3 != null) {
		// ft.hide(newsListFragment3);
		// }
		// if (newsListFragment5 != null) {
		// ft.hide(newsListFragment5);
		// }
		//
		// } else {
		//
		// newsListFragment5 = instanceNewsList(currentTopic,
		// newsClickListener, newsListFragment5,
		// CLASSIFICATION_NOTOPO, TAG_FRAGMENT_NEWS_LIST5);
		//
		// ft.show(newsListFragment5);
		//
		// if (newsListFragment1 != null) {
		// ft.hide(newsListFragment1);
		// }
		//
		// if (newsListFragment2 != null) {
		// ft.hide(newsListFragment2);
		// }
		// if (newsListFragment3 != null) {
		// ft.hide(newsListFragment3);
		// }
		// if (newsListFragment4 != null) {
		// ft.hide(newsListFragment4);
		// }
		// }
	}

	public void addNewsList(NewsListFragment newsListFragment,
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

	public void configTabs() {
		actionBar = getSupportActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab aba1 = actionBar.newTab().setText("populares").setTabListener(this);
		Tab aba2 = actionBar.newTab().setText("novos").setTabListener(this);
		Tab aba3 = actionBar.newTab().setText("subindo").setTabListener(this);
		Tab aba4 = actionBar.newTab().setText("controversos")
				.setTabListener(this);
		Tab aba5 = actionBar.newTab().setText("no topo").setTabListener(this);

		actionBar.addTab(aba1);
		actionBar.addTab(aba2);
		actionBar.addTab(aba3);
		actionBar.addTab(aba4);
		actionBar.addTab(aba5);
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

}
