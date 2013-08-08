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

public class DbTableNews {

	private Context context;
	private static News newsToRemove;
	private static FavoriteNewsListFragment favoriteNewsListFragment;

	public DbTableNews(Context context) {
		this.context = context;
	}

	public boolean favoriteNews(News news,
			FavoriteNewsListFragment favoriteNewsListFragment) {

		DbTableNews.favoriteNewsListFragment = favoriteNewsListFragment;
		boolean addedOk = false;

		if (getNewsById(news.getId()) == null) {

			ContentValues cv = new ContentValues();
			cv.put("title", news.getTitle());
			cv.put("subreddit", news.getSubreddit());
			cv.put("url", news.getUrl());
			cv.put("thumbnail", news.getThumbnail());
			cv.put("id", news.getId());

			SQLiteDatabase db = DbHelper.getInstance(context)
					.getWritableDatabase();

			long id = db.insert(DbHelper.TB_NEWS, null, cv);

			db.close();

			if (id < 0) {
				Toast toastMsg = Toast.makeText(context,
						"Falhou tentando favoritar news.", Toast.LENGTH_LONG);
				toastMsg.setGravity(Gravity.CENTER, 0, 0);
				toastMsg.show();
			} else {
				addedOk = true;
			}

		}

		return addedOk;
	}

	private boolean unfavoriteNews(News news) {

		boolean removedOk = false;

		if (news != null) {

			SQLiteDatabase db = DbHelper.getInstance(context)
					.getWritableDatabase();

			int idRet = db.delete(DbHelper.TB_NEWS, "id = ?",
					new String[] { news.getId() });

			db.close();

			if (idRet <= 0) {
				Toast toastMsg = Toast.makeText(
						context,
						"N‹o foi poss’vel desfavoritar news (id=["
								+ news.getId() + "]).", Toast.LENGTH_LONG);

				toastMsg.setGravity(Gravity.CENTER, 0, 0);
				toastMsg.show();
			} else {
				removedOk = true;
			}
		}

		return removedOk;
	}

	public void unfavoriteNews(News news,
			FavoriteNewsListFragment favoriteNewsListFragment) {
		unfavoriteNews(news, favoriteNewsListFragment, false);
	}

	public void unfavoriteNews(News news,
			FavoriteNewsListFragment favoriteNewsListFragment,
			boolean noConfirmation) {

		DbTableNews.newsToRemove = news;
		DbTableNews.favoriteNewsListFragment = favoriteNewsListFragment;

		if (noConfirmation) {
			unfavoriteNews(news);
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

									unfavoriteNews(DbTableNews.newsToRemove);

									((NewsAdapter) DbTableNews.favoriteNewsListFragment
											.getListAdapter())
											.remove(DbTableNews.newsToRemove);
									((NewsAdapter) DbTableNews.favoriteNewsListFragment
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

		SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();

		Cursor cursor;

		cursor = db.rawQuery("select * from " + DbHelper.TB_NEWS + " where "
				+ (isDbId ? "_id" : "id") + " = ?", new String[] { id });

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

		SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from " + DbHelper.TB_NEWS, null);

		while (cursor.moveToNext()) {

			News news = loadNewsFromCursor(cursor);

			favoriteNewsList.add(news);

		}

		cursor.close();

		db.close();

		return favoriteNewsList;
	}

}
