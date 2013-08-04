package br.com.maxwellfn.mynews;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News> {

	public NewsAdapter(Context context, List<News> objects) {
		super(context, 0, objects);
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

			holder.txtNewsTitle = (TextView) convertView
					.findViewById(R.id.txtNewsTitle);

			holder.txtNewsSubreddit = (TextView) convertView
					.findViewById(R.id.txtNewsSubreddit);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		new DownloadImageTask(holder.imgNewsThumbnail).execute(news
				.getThumbnail());

		holder.txtNewsTitle.setText(news.getTitle());
		holder.txtNewsSubreddit.setText(news.getSubreddit());

		return convertView;
	}

	private static class ViewHolder {
		ImageView imgNewsThumbnail;
		TextView txtNewsTitle;
		TextView txtNewsSubreddit;
	}

}
