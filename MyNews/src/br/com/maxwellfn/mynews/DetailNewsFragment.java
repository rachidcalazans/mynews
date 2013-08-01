package br.com.maxwellfn.mynews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WebCachedImageView;
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
			TextView txtDescricao = (TextView) v.findViewById(R.id.descricao);

			WebCachedImageView imgFoto = (WebCachedImageView) v
					.findViewById(R.id.foto);

			txtNome.setText(news.getNome());
			imgFoto.setImageUrl(news.getUrlFoto());
			txtDescricao.setText(news.getDescricao());
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
