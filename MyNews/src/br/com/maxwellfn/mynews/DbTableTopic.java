package br.com.maxwellfn.mynews;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.widget.Toast;

public class DbTableTopic {

	Context context;
	public static Topic topicToRemove;
	public static TopicListFragment topicListFragment;

	public DbTableTopic(Context context) {
		this.context = context;
	}

	public boolean favoriteTopic(Topic topic,
			TopicListFragment topicListFragment) {

		DbTableTopic.topicListFragment = topicListFragment;
		boolean addedOk = false;

		if (getTopicById(topic.getId()) == null) {

			ContentValues cv = new ContentValues();
			cv.put("displayname", topic.getDisplayName());
			cv.put("publicdesc", topic.getPublicDescription());
			cv.put("id", topic.getId());
			cv.put("url", topic.getUrl());
			cv.put("headerimg", topic.getHeaderImg());

			SQLiteDatabase db = DbHelper.getInstance(context)
					.getWritableDatabase();

			long id = db.insert(DbHelper.TB_TOPIC, null, cv);

			db.close();

			if (id < 0) {
				Toast toastMsg = Toast.makeText(context,
						"Falhou tentando favoritar t—pico.", Toast.LENGTH_LONG);
				toastMsg.setGravity(Gravity.CENTER, 0, 0);
				toastMsg.show();
			} else {
				addedOk = true;
			}

		}

		return addedOk;
	}

	private boolean unfavoriteTopic(Topic topic) {

		boolean removedOk = false;

		if (topic != null) {

			SQLiteDatabase db = DbHelper.getInstance(context)
					.getWritableDatabase();

			int idRet = db.delete(DbHelper.TB_TOPIC, "id = ?",
					new String[] { topic.getId() });

			db.close();

			if (idRet <= 0) {
				Toast toastMsg = Toast.makeText(context,
						"N‹o foi poss’vel desfavoritar (id=[" + topic.getId()
								+ "]).", Toast.LENGTH_LONG);

				toastMsg.setGravity(Gravity.CENTER, 0, 0);
				toastMsg.show();
			} else {
				removedOk = true;
			}
		}

		return removedOk;
	}

	public void unfavoriteTopic(Topic topic, TopicListFragment topicListFragment) {
		unfavoriteTopic(topic, topicListFragment, false);
	}

	public void unfavoriteTopic(Topic topic,
			TopicListFragment topicListFragment, boolean noConfirmation) {

		DbTableTopic.topicToRemove = topic;
		DbTableTopic.topicListFragment = topicListFragment;

		if (noConfirmation) {
			unfavoriteTopic(topic);
		} else {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);

			// set title
			alertDialogBuilder.setTitle("Confirma‹o de Remo‹o");

			// set dialog message
			alertDialogBuilder
					.setMessage("Tem certeza de que deseja remover?")
					.setCancelable(false)
					.setPositiveButton("Sim",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									unfavoriteTopic(DbTableTopic.topicToRemove);

									((TopicAdapter) DbTableTopic.topicListFragment
											.getListAdapter())
											.remove(DbTableTopic.topicToRemove);
									((TopicAdapter) DbTableTopic.topicListFragment
											.getListAdapter())
											.notifyDataSetChanged();

								}
							})
					.setNegativeButton("N‹o",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		}
	}

	/**
	 * Get topic from DB or reddit id, depending on isDbId argument.
	 * 
	 * @param id
	 * @param isDbId
	 * @return
	 */
	public Topic getTopicById(String id, boolean isDbId) {
		Topic topic = null;

		SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();

		Cursor cursor;

		cursor = db.rawQuery("select * from " + DbHelper.TB_TOPIC + " where "
				+ (isDbId ? "_id" : "id") + " = ?", new String[] { id });

		if (cursor.moveToNext()) {

			topic = loadTopicFromCursor(cursor);

		}

		cursor.close();

		db.close();

		return topic;
	}

	private Topic loadTopicFromCursor(Cursor cursor) {
		Topic topic = new Topic();
		topic.setDisplayName(cursor.getString(cursor
				.getColumnIndex("displayname")));
		topic.setPublicDescription(cursor.getString(cursor
				.getColumnIndex("publicdesc")));
		topic.setId(cursor.getString(cursor.getColumnIndex("id")));
		topic.setUrl(cursor.getString(cursor.getColumnIndex("url")));
		topic.setHeaderImg(cursor.getString(cursor.getColumnIndex("headerimg")));
		topic.setDbId(Long.valueOf(cursor.getInt(cursor.getColumnIndex("_id")))
				.intValue());
		return topic;
	}

	/**
	 * Get topic from DB id.
	 * 
	 * @param dbId
	 * @return
	 */
	public Topic getTopicByDbId(long dbId) {

		return getTopicById(String.valueOf(dbId), false);
	}

	/**
	 * Get topic from reddit news id.
	 * 
	 * @param id
	 * @return
	 */
	public Topic getTopicById(String id) {

		return getTopicById(id, false);
	}

	public List<Topic> getFavoriteTopicList() {

		List<Topic> topicList = new ArrayList<Topic>();

		SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from " + DbHelper.TB_TOPIC, null);

		while (cursor.moveToNext()) {

			Topic topic = loadTopicFromCursor(cursor);

			topicList.add(topic);

		}

		cursor.close();

		db.close();

		return topicList;
	}

	// TOPIC DB OPERATIONS

}
