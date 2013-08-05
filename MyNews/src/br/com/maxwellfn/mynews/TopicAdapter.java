package br.com.maxwellfn.mynews;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicAdapter extends ArrayAdapter<Topic> {

	public TopicAdapter(Context context, List<Topic> objects) {
		super(context, 0, objects);
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		new DownloadImageTask(holder.imgHeaderImg)
				.execute(topic.getHeaderImg());

		holder.txtDisplayName.setText(topic.getDisplayName());
		holder.txtPublicDescription.setText(topic.getPublicDescription());

		return convertView;
	}

	private static class ViewHolder {
		ImageView imgHeaderImg;
		TextView txtDisplayName;
		TextView txtPublicDescription;
	}

}
