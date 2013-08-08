package br.com.maxwellfn.mynews;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News> {

	private NewsListsFragmentManager mainActivity;

	public NewsAdapter(Context context, List<News> objects) {
		super(context, 0, objects);

		this.mainActivity = (NewsListsFragmentManager) context;

	}
	
	@Override
	public void remove(News object) {
		// TODO Auto-generated method stub
		super.remove(object);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		News news = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_list_news, null);
			holder = new ViewHolder();
			holder.imgNewsThumbnail = (ImageView) convertView
					.findViewById(R.id.imgNewsThumbnail);

			holder.imgNewsFavoriteIcon = (ImageView) convertView
					.findViewById(R.id.imgNewsFavoriteIcon);

			holder.imgNewsFavoriteIcon.setTag(news);

			holder.imgNewsFavoriteIcon
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							News news = (News) v.getTag();

							ImageView imgFavoriteIcon = (ImageView) v;

							DbRepository repo = new DbRepository(mainActivity);

							if (repo.getNewsById(news.getId()) != null) {

								repo.remove(news,
										mainActivity.favoriteNewsListFragment,
										true);

								imgFavoriteIcon
										.setImageResource(android.R.drawable.btn_star_big_off);
								
							} else {

								repo.add(news,
										mainActivity.favoriteNewsListFragment);

								imgFavoriteIcon
										.setImageResource(android.R.drawable.btn_star_big_on);

							}

						}
					});

			holder.txtNewsTitle = (TextView) convertView
					.findViewById(R.id.txtNewsTitle);

			holder.txtNewsSubreddit = (TextView) convertView
					.findViewById(R.id.txtNewsSubreddit);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mainActivity.getCurrentNewsListFragment().getNewsListFragment() instanceof NewsListFragment) {

			// Configura marcação de favorito
			DbRepository repo = new DbRepository(getContext());
			News newsFromFavorite = repo.getNewsById(news.getId());
			if (newsFromFavorite != null) {
				holder.imgNewsFavoriteIcon
						.setImageResource(android.R.drawable.btn_star_big_on);

			} else {
				holder.imgNewsFavoriteIcon
						.setImageResource(android.R.drawable.btn_star_big_off);

			}
		} else {
			holder.imgNewsFavoriteIcon.setVisibility(ImageView.INVISIBLE);
		}

		new DownloadImageTask(holder.imgNewsThumbnail).execute(news
				.getThumbnail());

		holder.txtNewsTitle.setText(news.getTitle());
		holder.txtNewsSubreddit.setText(news.getSubreddit());

		return convertView;
	}

	private static class ViewHolder {
		ImageView imgNewsThumbnail;
		ImageView imgNewsFavoriteIcon;
		TextView txtNewsTitle;
		TextView txtNewsSubreddit;
	}

}
