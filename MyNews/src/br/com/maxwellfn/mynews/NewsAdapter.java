package br.com.maxwellfn.mynews;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WebCachedImageView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News> {

	public NewsAdapter(Context context, List<News> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_list_news, null);
			holder = new ViewHolder();
			holder.imgThumbnail = (WebCachedImageView) convertView
					.findViewById(R.id.imgThumbnail);
			holder.txtNome = (TextView) convertView.findViewById(R.id.txtNome);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		News news = getItem(position);

		holder.imgThumbnail.setImageUrl(news.getUrlFoto());
		holder.txtNome.setText(news.getNome());

		return convertView;
	}

	private static class ViewHolder {
		WebCachedImageView imgThumbnail;
		TextView txtNome;
	}

}
