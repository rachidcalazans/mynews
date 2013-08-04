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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class NewsListFragment extends SherlockListFragment {

	public static final String CLASSIFICATION = "classification";
	public static final String TOPIC = "topic";
	public static final String AFTER = "after";

	private static final int MAX_FETCH_NEWS = 10;

	NewsSearchTask task;
	ProgressBar progress;
	TextView txtMensagem;

	private String classification;
	private String topic;
	private String after;

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	private OnNewsClickListener newsClickListener;

	public void setNewsClickListener(OnNewsClickListener newsClickListener) {
		this.newsClickListener = newsClickListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(CLASSIFICATION)) {
				classification = savedInstanceState.getString(CLASSIFICATION);
			}
			if (savedInstanceState.containsKey(TOPIC)) {
				topic = savedInstanceState.getString(TOPIC);
			}
			if (savedInstanceState.containsKey(AFTER)) {
				after = savedInstanceState.getString(AFTER);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.fragment_list_news, null);
		progress = (ProgressBar) layout.findViewById(R.id.progressBar1);
		txtMensagem = (TextView) layout.findViewById(R.id.txtMensagem);
		classification = getArguments().getString(CLASSIFICATION);
		topic = getArguments().getString(TOPIC);
		after = getArguments().getString(AFTER);

		return layout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(TOPIC, topic);
		outState.putString(CLASSIFICATION, classification);
		outState.putString(AFTER, after);
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
				task.execute(topic, classification, after);
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

	public static NewsListFragment novaInstancia(String topic,
			String classification, String after) {
		Bundle params = new Bundle();
		params.putString(TOPIC, topic);
		params.putString(CLASSIFICATION, classification);
		params.putString(AFTER, after);

		NewsListFragment newsListFragment = new NewsListFragment();
		newsListFragment.setArguments(params);
		newsListFragment.setTopic(topic);
		newsListFragment.setClassification(classification);
		newsListFragment.setAfter(after);

		return newsListFragment;
	}

	class NewsSearchTask extends AsyncTask<String, Void, List<News>> {

		private final String URL_TEMPLATE = "http://i.reddit.comTOPIC#CLASSIFICATION.json"
				+ "?after=AFTER";

		private String getURL(String topic, String classification, String after) {

			if (topic.length() <= 0 && classification.length() > 0) {
				topic = "/";
			}
			String url = URL_TEMPLATE.replace("TOPIC#", topic);
			if (topic.length() <= 0 && classification.length() <= 0) {
				classification = "/";
			}
			url = url.replace("CLASSIFICATION", classification);
			url = url.replace("AFTER", after);
			return url;
		}

		@Override
		protected List<News> doInBackground(String... params) {

			try {

				URL url = new URL(getURL(params[0], params[1], params[2]));
				Log.d("MYNEWS", url.toString());

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

			String after = jsonData.getString("after");

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
				news.setSubreddit(jsonNews.getString("subreddit"));
				news.setAfter(after);

				boolean isNewsValid = true;

				if (!isURLValid(news.getUrl())) {
					isNewsValid = false;
					Log.d("MYNEWS", "URL Inválida: url=[" + news.getUrl() + "]");
				}
				if (!isURLValid(news.getThumbnail())) {
					isNewsValid = false;
					Log.d("MYNEWS",
							"THUMBNAIL Inválida: thumbnail=["
									+ news.getThumbnail() + "]");
				}

				if (isNewsValid) {
					resultados.add(news);

					if (i == MAX_FETCH_NEWS - 1) {
						// Para não ficar lendo todos os posts
						// e demorar demais para apresentar a lista;
						break;
					}
				}
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
				txtMensagem.setText("Nenhuma postagem encontrada.");

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

	private boolean isURLValid(String url) {
		if (url != null && url.trim().length() > 0 && !url.contains(" ")
				&& url.startsWith("http")) {
			return true;
		} else {
			return false;
		}
	}

	public String getCurrentTopicUrl() {
		return topic;
	}

}
