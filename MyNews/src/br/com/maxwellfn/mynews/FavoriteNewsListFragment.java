package br.com.maxwellfn.mynews;

import java.util.List;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.maxwellfn.mynews.NewsListFragment.OnNewsClickListener;

import com.actionbarsherlock.app.SherlockListFragment;

public class FavoriteNewsListFragment extends SherlockListFragment {

	FavoriteNewsReadTask task;
	ProgressBar progress;
	TextView txtMensagem;

	private OnNewsClickListener newsClickListener;

	public void setNewsClickListener(OnNewsClickListener newsClickListener) {
		this.newsClickListener = newsClickListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.fragment_list_news, null);
		progress = (ProgressBar) layout.findViewById(R.id.progressBar1);
		txtMensagem = (TextView) layout.findViewById(R.id.txtMensagem);
		
		return layout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
		
		setRetainInstance(true);

		if (task == null) {
			task = new FavoriteNewsReadTask();
			task.execute();
		} else if (task.getStatus() == Status.RUNNING) {
			progress.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		if (newsClickListener != null) {
			News news = (News) getListAdapter().getItem(position);
			newsClickListener.onNewsClick(news);
		}

	}

	public static FavoriteNewsListFragment novaInstancia() {
		Bundle params = new Bundle();

		FavoriteNewsListFragment newsListFragment = new FavoriteNewsListFragment();
		newsListFragment.setArguments(params);

		return newsListFragment;
	}

	class FavoriteNewsReadTask extends AsyncTask<String, Void, List<News>> {

		@Override
		protected List<News> doInBackground(String... params) {

			return getUpdatedFavoriteList(new DbRepository(getActivity()));
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

	}

	public List<News> getUpdatedFavoriteList(DbRepository repo) {

		return repo.getFavoriteNewsList();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return super.onContextItemSelected(item);
	}

}
