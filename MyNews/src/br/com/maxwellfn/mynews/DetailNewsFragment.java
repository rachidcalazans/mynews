package br.com.maxwellfn.mynews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class DetailNewsFragment extends SherlockFragment {

	News news;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setHasOptionsMenu(true); // <----- IMPORTATE!!!
		View v;
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("news")) {
			news = (News) savedInstanceState.getSerializable("news");
		} else {

			news = ((News) getArguments().getSerializable("news"));
		}

		if (news != null) {
			v = inflater.inflate(R.layout.fragment_detail_news, null);

			TextView txtNome = (TextView) v.findViewById(R.id.nome);

			ImageView imgFoto = (ImageView) v.findViewById(R.id.foto);

			txtNome.setText(news.getTitle());

			new DownloadImageTask(imgFoto).execute(news.getThumbnail());

		} else {
			v = inflater.inflate(R.layout.fragment_detail_news_null, null);
		}
		return v;

	}

	public static DetailNewsFragment novaInstancia(News news) {

		Bundle params = new Bundle();
		params.putSerializable("news", news);

		DetailNewsFragment fragment = new DetailNewsFragment();
		fragment.setArguments(params);

		return fragment;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("news", news);
	}

}
