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

public class DbRepository {

	private DbHelper helper;
	Context context;
	public static News newsToRemove;
	public static FavoriteNewsListFragment favoriteNewsListFragment;

	public DbRepository(Context context) {
		helper = new DbHelper(context);
		this.context = context;
	}

	public void add(News news, FavoriteNewsListFragment favoriteNewsListFragment) {

		DbRepository.favoriteNewsListFragment = favoriteNewsListFragment;

		if (getNewsById(news.getId()) == null) {

			ContentValues cv = new ContentValues();
			cv.put("title", news.getTitle());
			cv.put("subreddit", news.getSubreddit());
			cv.put("url", news.getUrl());
			cv.put("thumbnail", news.getThumbnail());
			cv.put("id", news.getId());

			SQLiteDatabase db = helper.getWritableDatabase();

			long id = db.insert(DbHelper.TABLE_NAME_FAVORITA, null, cv);

			db.close();

			if (id < 0) {
				Toast toastMsg = Toast
						.makeText(context,
								"Falhou tentando inserir favoritas.",
								Toast.LENGTH_LONG);
				toastMsg.setGravity(Gravity.CENTER, 0, 0);
				toastMsg.show();
			}

		}
	}

	public void remove(News news,
			FavoriteNewsListFragment favoriteNewsListFragment) {
		remove(news, favoriteNewsListFragment, false);
	}

	public void remove(News news,
			FavoriteNewsListFragment favoriteNewsListFragment,
			boolean noConfirmation) {

		DbRepository.newsToRemove = news;
		DbRepository.favoriteNewsListFragment = favoriteNewsListFragment;

		if (noConfirmation) {
			remove(DbRepository.newsToRemove);
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

									remove(DbRepository.newsToRemove);

									((NewsAdapter) DbRepository.favoriteNewsListFragment
											.getListAdapter())
											.remove(DbRepository.newsToRemove);
									((NewsAdapter) DbRepository.favoriteNewsListFragment
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
	 * Get news from DB or reddit id, depending on isDbId argument.
	 * 
	 * @param id
	 * @param isDbId
	 * @return
	 */
	public News getNewsById(String id, boolean isDbId) {
		News news = null;

		SQLiteDatabase db = helper.getReadableDatabase();

		Cursor cursor;

		cursor = db.rawQuery("select * from " + DbHelper.TABLE_NAME_FAVORITA
				+ " where " + (isDbId ? "_id" : "id") + " = ?",
				new String[] { id });

		if (cursor.moveToNext()) {

			news = loadNewsFromCursor(cursor);

		}

		cursor.close();

		db.close();

		return news;
	}

	private News loadNewsFromCursor(Cursor cursor) {
		News news;
		news = new News();
		news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		news.setSubreddit(cursor.getString(cursor.getColumnIndex("subreddit")));
		news.setId(cursor.getString(cursor.getColumnIndex("id")));
		news.setUrl(cursor.getString(cursor.getColumnIndex("url")));
		news.setThumbnail(cursor.getString(cursor.getColumnIndex("thumbnail")));
		news.setDbId(Long.valueOf(cursor.getInt(cursor.getColumnIndex("_id")))
				.intValue());
		return news;
	}

	/**
	 * Get news from DB id.
	 * 
	 * @param dbId
	 * @return
	 */
	public News getNewsByDbId(long dbId) {

		return getNewsById(String.valueOf(dbId), false);
	}

	/**
	 * Get news from reddit news id.
	 * 
	 * @param id
	 * @return
	 */
	public News getNewsById(String id) {

		return getNewsById(id, false);
	}

	public List<News> getFavoriteNewsList() {

		List<News> favoriteNewsList = new ArrayList<News>();

		SQLiteDatabase db = helper.getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from "
				+ DbHelper.TABLE_NAME_FAVORITA, null);

		while (cursor.moveToNext()) {

			News news = loadNewsFromCursor(cursor);

			favoriteNewsList.add(news);

		}

		cursor.close();

		db.close();

		return favoriteNewsList;
	}

	private void remove(News news) {

		if (news != null) {

			SQLiteDatabase db = helper.getWritableDatabase();

			int idRet = db.delete(DbHelper.TABLE_NAME_FAVORITA, "id = ?",
					new String[] { String.valueOf(DbRepository.newsToRemove
							.getId()) });

			db.close();

			if (idRet <= 0) {
				Toast toastMsg = Toast.makeText(context,
						"N‹o foi poss’vel excluir favorita (id=["
								+ DbRepository.newsToRemove.getId() + "]).",
						Toast.LENGTH_LONG);

				toastMsg.setGravity(Gravity.CENTER, 0, 0);
				toastMsg.show();
			}
		}
	}

}
