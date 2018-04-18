package br.ufpe.cin.if1001.rss.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RssUpdatedNewsReceiver extends BroadcastReceiver {
    public static final String NOTIFY_NEW_CONTENT = "NOTIFY_NEW_CONTENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: 18/04/2018 : Adicionar funcionalidade de exibir notificação ao ver que há notícias novas.
    }
}