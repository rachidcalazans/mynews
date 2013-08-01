package br.com.maxwellfn.mynews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class DetailNewsActivity extends SherlockFragmentActivity {

	private News news;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		news = (News) getIntent().getSerializableExtra("news");

		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		setContentView(R.layout.activity_detail_news);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		DetailNewsFragment fragment = DetailNewsFragment.novaInstancia(news);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.rootDetalhe, fragment, "detalheTag").commit();
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		// if (item.getItemId() == android.R.id.home) {
		// finish();
		// }

		return super.onMenuItemSelected(featureId, item);
	}

	public void abrirSite(View v) {
		Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
		startActivity(it);
	}
}
