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

public class TopicListFragment extends SherlockListFragment {

	public static final String AFTER = "after";

	TopicSearchTask task;
	ProgressBar progress;
	TextView txtMensagem;
	String after;

	private OnTopicClickListener topicClickListener;

	public void setTopicClickListener(OnTopicClickListener topicClickListener) {
		this.topicClickListener = topicClickListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
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
		after = getArguments().getString(AFTER);

		return layout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
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
				task = new TopicSearchTask();
				task.execute(after);
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

		if (topicClickListener != null) {
			Topic topic = (Topic) getListAdapter().getItem(position);
			topicClickListener.onTopicClick(topic);
		}

	}

	public interface OnTopicClickListener {
		void onTopicClick(Topic topic);
	}

	class TopicSearchTask extends AsyncTask<String, Void, List<Topic>> {

		private final String URL_TEMPLATE = "http://i.reddit.com/subreddits/.json"
				+ "?after=AFTER";

		private String getURL(String after) {
			String url = URL_TEMPLATE.replace("AFTER", after);
			return url;
		}

		@Override
		protected List<Topic> doInBackground(String... params) {

			try {

				URL url = new URL(getURL(params[0]));
				Log.d("MYNEWS", url.toString());

				HttpURLConnection conexao = (HttpURLConnection) url
						.openConnection();
				conexao.setRequestMethod("GET");
				conexao.setDoInput(true);
				conexao.setConnectTimeout(15000); // 15 segundos
				conexao.connect();

				if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
					return getTopicList(conexao.getInputStream());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		private List<Topic> getTopicList(InputStream inputStream)
				throws IOException, JSONException {
			List<Topic> resultados = new ArrayList<Topic>();
			long inicio, fim;

			inicio = System.currentTimeMillis();
			// Conecta com o servidor, abre o stream e converte pra stream
			String jsonString = streamToString(inputStream);
			fim = System.currentTimeMillis();
			Log.d("MYNEWS", "(p1):" + (fim - inicio));

			inicio = System.currentTimeMillis();
			// Conecta com o servidor, abre o stream e converte pra stream
			JSONObject jsonRoot = new JSONObject(jsonString);
			fim = System.currentTimeMillis();
			Log.d("MYNEWS", "(p2):" + (fim - inicio));

			inicio = System.currentTimeMillis();

			JSONObject jsonData = jsonRoot.getJSONObject("data");
			String after = jsonData.getString("after");
			JSONArray jsonChildren = jsonData.getJSONArray("children");

			// Configura o primeiro TOPIC da lista
			// que sempre será o FRONT padrão da primeira
			// carga e que não referencia SUBREDDITS.
			Topic headerListTopic = new Topic();

			headerListTopic.setDisplayName("Tudo");
			headerListTopic.setTitle("Todas as postagens.");
			headerListTopic
					.setPublicDescription("Busca de todas as postagens sem filtro por tópicos.");
			headerListTopic.setUrl("");
			headerListTopic
					.setHeaderImg("http://www.reddit.com/static/blog_snoo.png");
			headerListTopic.setSubscribers(0);
			headerListTopic.setAfter(after);

			resultados.add(headerListTopic);

			for (int i = 0; i < jsonChildren.length(); i++) {
				Topic topic = new Topic();

				JSONObject jsonNews = jsonChildren.getJSONObject(i)
						.getJSONObject("data");

				topic.setDisplayName(jsonNews.getString("display_name"));
				topic.setTitle(jsonNews.getString("title"));
				topic.setPublicDescription(jsonNews
						.getString("public_description"));
				topic.setUrl(jsonNews.getString("url"));
				topic.setHeaderImg(jsonNews.getString("header_img"));
				topic.setSubscribers(jsonNews.getInt("subscribers"));
				topic.setAfter(after);

				boolean isTopicValid = true;

				if (!NewsListsFragmentManager.isURLValid(topic.getHeaderImg())) {
					isTopicValid = false;
					Log.d("MYNEWS", "URL Topic Image Header Inválida: url=["
							+ topic.getUrl() + "]");
				}

				if (isTopicValid) {

					resultados.add(topic);

				}
			}

			fim = System.currentTimeMillis();
			Log.d("MYNEWS", "(p3):" + (fim - inicio));

			return resultados;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			txtMensagem.setVisibility(View.INVISIBLE);
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(List<Topic> result) {
			super.onPostExecute(result);
			if (result != null) {

				TopicAdapter topicAdapter = new TopicAdapter(getActivity(),
						result);
				setListAdapter(topicAdapter);
			} else {
				txtMensagem.setVisibility(View.VISIBLE);
				txtMensagem.setText("Nenhum tópico encontrado.");

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

	public static TopicListFragment novaInstancia(String after) {
		Bundle params = new Bundle();
		params.putString(AFTER, after);

		TopicListFragment topicListFragment = new TopicListFragment();
		topicListFragment.setArguments(params);

		return topicListFragment;
	}

}
