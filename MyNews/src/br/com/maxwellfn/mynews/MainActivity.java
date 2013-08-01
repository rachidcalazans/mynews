package br.com.maxwellfn.mynews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import br.com.maxwellfn.mynews.NewsListFragment.OnNewsClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity implements
		TabListener, OnNewsClickListener {

	NewsListFragment f1;
	NewsListFragment f2;
	NewsListFragment f3;
	News news;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		setContentView(R.layout.activity_main);

		f1 = (NewsListFragment) getSupportFragmentManager().findFragmentByTag(
				"f1");
		f2 = (NewsListFragment) getSupportFragmentManager().findFragmentByTag(
				"f2");
		f3 = (NewsListFragment) getSupportFragmentManager().findFragmentByTag(
				"f3");

		if (f1 == null || f2 == null || f3 == null) {

			f1 = NewsListFragment.novaInstancia("carros_classicos");
			f2 = NewsListFragment.novaInstancia("carros_esportivos");
			f3 = NewsListFragment.novaInstancia("carros_luxo");

		}
		f1.setNewsClickListener(this);
		f2.setNewsClickListener(this);
		f3.setNewsClickListener(this);

		getSupportFragmentManager().beginTransaction()
				.add(R.id.conteudoAbas, f1, "f1")
				.add(R.id.conteudoAbas, f2, "f2").hide(f2)
				.add(R.id.conteudoAbas, f3, "f3").hide(f3).commit();

		ActionBar actionBar = getSupportActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab aba1 = actionBar.newTab().setText("CLÁSSICOS").setTabListener(this);
		Tab aba2 = actionBar.newTab().setText("ESPORTIVOS")
				.setTabListener(this);
		Tab aba3 = actionBar.newTab().setText("LUXO").setTabListener(this);

		actionBar.addTab(aba1);
		actionBar.addTab(aba2);
		actionBar.addTab(aba3);

		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState
					.getInt("abaSelecionada"));

			news = (News) savedInstanceState.getSerializable("news");

		}

		if (isTablet()) {

			DetailNewsFragment fragment = DetailNewsFragment
					.novaInstancia(news);

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.rootDetalhe, fragment, "detalhe").commit();

		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("abaSelecionada", getSupportActionBar()
				.getSelectedNavigationIndex());
		outState.putSerializable("news", news);

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (tab.getPosition() == 0) {
			ft.show(f1).hide(f2).hide(f3);
		} else if (tab.getPosition() == 1) {
			ft.show(f2).hide(f1).hide(f3);
		} else {
			ft.show(f3).hide(f1).hide(f2);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	private boolean isTablet() {
		return findViewById(R.id.rootDetalhe) != null;
	}

	@Override
	public void onNewsClick(News news) {

		this.news = news;

		if (isTablet()) {

			DetailNewsFragment fragment = DetailNewsFragment
					.novaInstancia(news);

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.rootDetalhe, fragment, "detalhe").commit();

		} else {

			Intent i = new Intent(this, DetailNewsActivity.class);
			i.putExtra("news", news);
			startActivityForResult(i, 0);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 0) {
			;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void abrirSite(View v) {
		Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrlInfo()));
		startActivity(it);
	}

}
