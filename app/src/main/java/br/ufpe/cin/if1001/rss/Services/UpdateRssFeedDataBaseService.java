package br.ufpe.cin.if1001.rss.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.ufpe.cin.if1001.rss.db.SQLiteRSSHelper;
import br.ufpe.cin.if1001.rss.domain.ItemRSS;
import br.ufpe.cin.if1001.rss.ui.MainActivity;
import br.ufpe.cin.if1001.rss.util.Constants;
import br.ufpe.cin.if1001.rss.util.ParserRSS;

//   Serviço criado para substituir a AsyncTask na MainActivity. Serve para atualizar o banco de dados,
// verificando se há novos itens e, em caso positivo, inserindo-os no banco.
public class UpdateRssFeedDataBaseService extends IntentService {
    private SQLiteRSSHelper db;

    public UpdateRssFeedDataBaseService() {
        super("UpdateRssFeedDataBaseService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean flag_problema = false;
        List<ItemRSS> items = null;
        String link = intent.getCharSequenceExtra(Constants.RSS_FEED_KEY_NAME).toString();
        db = SQLiteRSSHelper.getInstance(getApplicationContext());

        try {
            String feed = getRssFeed(link);
            items = ParserRSS.parse(feed);
            for (ItemRSS i : items) {
                Log.d("DB", "Buscando no Banco por link: " + i.getLink());
                ItemRSS item = db.getItemRSS(i.getLink());
                if (item == null) {
                    Log.d("DB", "Encontrado pela primeira vez: " + i.getTitle());
                    db.insertItem(i);
                }
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            flag_problema = true;
        }

        if(flag_problema)
            Toast.makeText(getApplicationContext(), "Houve algum problema ao carregar o feed.", Toast.LENGTH_SHORT).show();
        else {
//                new ExibirFeed().execute();
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.RssFeedReceiver.GET_ITEMS_FINISHED);
            sendBroadcast(broadcastIntent);
        }
    }

    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }
}
