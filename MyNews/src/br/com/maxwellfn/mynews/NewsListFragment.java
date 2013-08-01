package br.com.maxwellfn.mynews;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class NewsListFragment extends SherlockListFragment {

	public static final String NOME_ABA = "nomeAba";

	NewsSearchTask task;
	ProgressBar progress;
	TextView txtMensagem;
	String nomeAba;

	private OnNewsClickListener newsClickListener;

	public void setNewsClickListener(OnNewsClickListener newsClickListener) {
		this.newsClickListener = newsClickListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(NOME_ABA)) {
			nomeAba = savedInstanceState.getString(NOME_ABA);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.fragment_list_news, null);
		progress = (ProgressBar) layout.findViewById(R.id.progressBar1);
		txtMensagem = (TextView) layout.findViewById(R.id.txtMensagem);
		nomeAba = getArguments().getString(NOME_ABA);

		return layout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(NOME_ABA, nomeAba);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setRetainInstance(true);

		ConnectivityManager manager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info != null && info.isConnected()) {

			if (task == null) {
				task = new NewsSearchTask();
				task.execute(nomeAba);
			} else if (task.getStatus() == Status.RUNNING) {
				progress.setVisibility(View.VISIBLE);
			}

		} else {
			txtMensagem.setText("Sem conexão de dados");
			txtMensagem.setVisibility(View.VISIBLE);
			progress.setVisibility(View.INVISIBLE);

			AlertDialog dialog = new AlertDialog.Builder(getActivity())
					.setTitle("Informação")
					.setMessage("Sem conexão de dados no momento.")
					.setPositiveButton("OK", null).create();
			dialog.show();
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		if (newsClickListener != null) {
			News news = (News) getListAdapter().getItem(position);
			newsClickListener.onNewsClick(news);
		}

	}

	public interface OnNewsClickListener {
		void onNewsClick(News news);
	}

	public static NewsListFragment novaInstancia(String nomeAba) {
		Bundle params = new Bundle();
		params.putString(NOME_ABA, nomeAba);

		NewsListFragment newsListFragment = new NewsListFragment();
		newsListFragment.setArguments(params);

		return newsListFragment;
	}

	class NewsSearchTask extends AsyncTask<String, Void, List<News>> {

		@Override
		protected List<News> doInBackground(String... params) {

			try {

				URL url = new URL("http://i.reddit.com/" + params[0] + "/.json");

				HttpURLConnection conexao = (HttpURLConnection) url
						.openConnection();
				conexao.setRequestMethod("GET");
				conexao.setDoInput(true);
				conexao.setConnectTimeout(15000); // 15 segundos
				conexao.connect();

				if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
					return getNewsList(conexao.getInputStream());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		private List<News> getNewsList(InputStream inputStream)
				throws IOException, JSONException {
			List<News> resultados = new ArrayList<News>();

			String jsonString = streamToString(inputStream);

			JSONObject jsonRoot = new JSONObject(jsonString);
			JSONObject jsonData = jsonRoot.getJSONObject("data");
			JSONArray jsonChildren = jsonData.getJSONArray("children");

			for (int i = 0; i < jsonChildren.length(); i++) {
				News news = new News();

				JSONObject jsonNews = jsonChildren.getJSONObject(i)
						.getJSONObject("data");

				news.setId(jsonNews.getString("id"));
				news.setTitle(jsonNews.getString("title"));
				news.setAuthor(jsonNews.getString("author"));
				news.setThumbnail(jsonNews.getString("thumbnail"));
				news.setUrl(jsonNews.getString("url"));

				resultados.add(news);
			}

			return resultados;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			txtMensagem.setVisibility(View.INVISIBLE);
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(List<News> result) {
			super.onPostExecute(result);
			if (result != null) {

				NewsAdapter newsAdapter = new NewsAdapter(getActivity(), result);
				setListAdapter(newsAdapter);
			} else {
				txtMensagem.setVisibility(View.VISIBLE);
				txtMensagem.setText("Nenhuma notícia foi encontrada.");

			}

			progress.setVisibility(View.INVISIBLE);
		}

		private String streamToString(InputStream is) throws IOException {

			byte[] bytes = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int lidos;
			while ((lidos = is.read(bytes)) > 0) {
				baos.write(bytes, 0, lidos);
			}
			return new String(baos.toByteArray());
		}
	}

}
