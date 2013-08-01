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
			holder.imgThumbnail = (ImageView) convertView
					.findViewById(R.id.imgThumbnail);

			holder.txtNome = (TextView) convertView.findViewById(R.id.txtNome);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		new DownloadImageTask(holder.imgThumbnail).execute(news.getThumbnail());
		
		holder.txtNome.setText(news.getTitle());

		return convertView;
	}

	private static class ViewHolder {
		ImageView imgThumbnail;
		TextView txtNome;
	}

}
