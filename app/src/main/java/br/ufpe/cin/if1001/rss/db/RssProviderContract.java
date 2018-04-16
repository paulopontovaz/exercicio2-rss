package br.ufpe.cin.if1001.rss.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class RssProviderContract {

    public static final String _ID = BaseColumns._ID;
    public static final String TITLE = "title";
    public static final String DATE = "pubDate";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "guid";
    public static final String UNREAD = "unread";
    public static final String ITEMS_TABLE = "items";


    public final static String[] ALL_COLUMNS = {
            _ID, TITLE, DATE, DESCRIPTION, LINK, UNREAD};

    private static final Uri BASE_RSS_URI = Uri.parse("content://br.ufpe.cin.if1001.rss/");
    //URI para tabela
    public static final Uri ITEMS_LIST_URI = Uri.withAppendedPath(BASE_RSS_URI, ITEMS_TABLE);

    // Mime type para colecao de itens
    public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/RssProvider.data.text";

    // Mime type para um item especifico
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/RssProvider.data.text";

}

