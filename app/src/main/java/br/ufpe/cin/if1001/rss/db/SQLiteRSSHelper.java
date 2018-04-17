package br.ufpe.cin.if1001.rss.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.ufpe.cin.if1001.rss.domain.ItemRSS;


public class SQLiteRSSHelper extends SQLiteOpenHelper {
    //Nome do Banco de Dados
    private static final String DATABASE_NAME = "rss";
    //Nome da tabela do Banco a ser usada
    public static final String DATABASE_TABLE = "items";
    //Versão atual do banco
    private static final int DB_VERSION = 1;

    //alternativa
    Context c;

    private SQLiteRSSHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        c = context;
    }

    private static SQLiteRSSHelper db;

    //Definindo Singleton
    public static SQLiteRSSHelper getInstance(Context c) {
        if (db==null) {
            db = new SQLiteRSSHelper(c.getApplicationContext());
        }
        return db;
    }

    //Definindo constantes que representam os campos do banco de dados
    public static final String ITEM_ROWID = RssProviderContract._ID;
    public static final String ITEM_TITLE = RssProviderContract.TITLE;
    public static final String ITEM_DATE = RssProviderContract.DATE;
    public static final String ITEM_DESC = RssProviderContract.DESCRIPTION;
    public static final String ITEM_LINK = RssProviderContract.LINK;
    public static final String ITEM_UNREAD = RssProviderContract.UNREAD;

    //Definindo constante que representa um array com todos os campos
    public final static String[] columns = RssProviderContract.ALL_COLUMNS;

    //Definindo constante que representa o comando de criação da tabela no banco de dados
    private static final String CREATE_DB_COMMAND = "CREATE TABLE " + DATABASE_TABLE + " (" +
            ITEM_ROWID +" integer primary key autoincrement, "+
            ITEM_TITLE + " text not null, " +
            ITEM_DATE + " text not null, " +
            ITEM_DESC + " text not null, " +
            ITEM_LINK + " text not null, " +
            ITEM_UNREAD + " boolean not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Executa o comando de criação de tabela
        db.execSQL(CREATE_DB_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //estamos ignorando esta possibilidade no momento
        throw new RuntimeException("nao se aplica");
    }

	//IMPLEMENTAR ABAIXO
    //Implemente a manipulação de dados nos métodos auxiliares para não ficar criando consultas manualmente
    public long insertItem(ItemRSS item) {
        return insertItem(item.getTitle(),item.getPubDate(),item.getDescription(),item.getLink());
    }
    public long insertItem(String title, String pubDate, String description, String link) {
        SQLiteDatabase dataBase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_TITLE, title);
        values.put(ITEM_DATE, pubDate);
        values.put(ITEM_DESC, description);
        values.put(ITEM_LINK, link);
        values.put(ITEM_UNREAD, true);
        return dataBase.insert(DATABASE_TABLE,null, values);
    }
    public ItemRSS getItemRSS(String link) throws SQLException {
        SQLiteDatabase dataBase = db.getReadableDatabase();
        String where = ITEM_LINK + " LIKE '" + link + "'";
        Cursor cursor = dataBase.query(
                DATABASE_TABLE,
                columns,
                where,
                null,
                null,
                null,
                null,
                null);
        ItemRSS item = null;
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            item = new ItemRSS(
                    cursor.getString(cursor.getColumnIndexOrThrow(ITEM_TITLE)),
                    link,
                    cursor.getString(cursor.getColumnIndexOrThrow(ITEM_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ITEM_DESC))
            );
        }

        return item;
    }
    public Cursor getItems() throws SQLException {
        SQLiteDatabase dataBase = db.getReadableDatabase();
        String where = ITEM_UNREAD + " = 1";
        Cursor cursor = null;

        try {
            dataBase.beginTransaction();

            cursor = dataBase.query(
                DATABASE_TABLE,
                columns,
                where,
                null,
                null,
                null,
                ITEM_DATE + " DESC",
                null
            );

            dataBase.endTransaction();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return cursor;
    }
    public boolean markAsUnread(String link) {
        return updateUnread(link, true);
//        return true;
    }

    public boolean markAsRead(String link) {
        return updateUnread(link, false);
//        return false;
    }

    private boolean updateUnread(String link, Boolean unread) {
        SQLiteDatabase dataBase = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_UNREAD, unread);

        int result = dataBase.update(
                DATABASE_TABLE,
                values,
                ITEM_LINK + " LIKE ?",
                new String[]{ link });

        return result > 0;
    }

}
