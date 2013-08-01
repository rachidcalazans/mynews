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

	NewsSearchTask task;
	ProgressBar progress;
	TextView txtMensagem;
	String tipoCarro;

	private OnNewsClickListener newsClickListener;

	public void setNewsClickListener(OnNewsClickListener newsClickListener) {
		this.newsClickListener = newsClickListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("tipoCarro")) {
			tipoCarro = savedInstanceState.getString("tipoCarro");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.fragment_list_news, null);
		progress = (ProgressBar) layout.findViewById(R.id.progressBar1);
		txtMensagem = (TextView) layout.findViewById(R.id.txtMensagem);
		tipoCarro = getArguments().getString("tipoCarro");

		return layout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tipoCarro", tipoCarro);
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
				task.execute(tipoCarro);
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
		void onNewsClick(News carro);
	}

	public static NewsListFragment novaInstancia(String tipoCarro) {
		Bundle params = new Bundle();
		params.putString("tipoCarro", tipoCarro);

		NewsListFragment newsListFragment = new NewsListFragment();
		newsListFragment.setArguments(params);

		return newsListFragment;
	}

	class NewsSearchTask extends AsyncTask<String, Void, List<News>> {

		@Override
		protected List<News> doInBackground(String... params) {

			try {

				URL url = new URL("http://livroandroid.com.br/livro/carros/"
						+ params[0] + ".json");

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
			JSONObject jsonObjCarros = jsonRoot.getJSONObject("carros");
			JSONArray jsonArrCarros = jsonObjCarros.getJSONArray("carro");

			for (int i = 0; i < jsonArrCarros.length(); i++) {
				News news = new News();

				JSONObject jsonObjCarro = jsonArrCarros.getJSONObject(i);

				news.setNome(jsonObjCarro.getString("nome"));
				news.setDescricao(jsonObjCarro.getString("desc"));
				news.setUrlInfo(jsonObjCarro.getString("url_info"));
				news.setUrlFoto(jsonObjCarro.getString("url_foto"));

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

				NewsAdapter carAdapter = new NewsAdapter(getActivity(), result);
				setListAdapter(carAdapter);
			} else {
				txtMensagem.setVisibility(View.VISIBLE);
				txtMensagem.setText("Nenhum carro foi encontrado.");

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
