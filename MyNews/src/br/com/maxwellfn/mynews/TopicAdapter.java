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

public class TopicAdapter extends ArrayAdapter<Topic> {

	private MainActivity mainActivity;

	public TopicAdapter(Context context, List<Topic> objects) {
		super(context, 0, objects);

		this.mainActivity = (MainActivity) context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Topic topic = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_list_topic, null);
			holder = new ViewHolder();
			holder.imgHeaderImg = (ImageView) convertView
					.findViewById(R.id.imgTopicHeaderImage);

			holder.txtDisplayName = (TextView) convertView
					.findViewById(R.id.txtTopicDisplayName);
			holder.txtPublicDescription = (TextView) convertView
					.findViewById(R.id.txtTopicPublicDescription);

			holder.imgTopicFavoriteIcon = (ImageView) convertView
					.findViewById(R.id.imgTopicFavoriteIcon);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Configura marcação de favorito
		DbRepository repo = new DbRepository(getContext());
		Topic topicFromFavorite = repo.getTopicById(topic.getId());
		if (topicFromFavorite != null || topic.getId().equals("tudo")) {
			holder.imgTopicFavoriteIcon
					.setImageResource(android.R.drawable.btn_star_big_on);

		} else {
			holder.imgTopicFavoriteIcon
					.setImageResource(android.R.drawable.btn_star_big_off);

		}

		new DownloadImageTask(holder.imgHeaderImg)
				.execute(topic.getHeaderImg());

		holder.txtDisplayName.setText(topic.getDisplayName());
		holder.txtPublicDescription.setText(topic.getPublicDescription());

		holder.imgTopicFavoriteIcon.setTag(topic);

		holder.imgTopicFavoriteIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Topic topic = (Topic) v.getTag();

				ImageView imgFavoriteIcon = (ImageView) v;

				DbRepository repo = new DbRepository(mainActivity);

				if (!topic.getId().equals("tudo")) {

					if (repo.getTopicById(topic.getId()) != null) {

						repo.unfavoriteTopic(topic,
								mainActivity.topicListFragment, true);

						imgFavoriteIcon
								.setImageResource(android.R.drawable.btn_star_big_off);

					} else {

						repo.favoriteTopic(topic,
								mainActivity.topicListFragment);

						imgFavoriteIcon
								.setImageResource(android.R.drawable.btn_star_big_on);

					}
				}

			}
		});

		return convertView;
	}

	private static class ViewHolder {
		ImageView imgHeaderImg;
		TextView txtDisplayName;
		TextView txtPublicDescription;
		ImageView imgTopicFavoriteIcon;
	}

}
