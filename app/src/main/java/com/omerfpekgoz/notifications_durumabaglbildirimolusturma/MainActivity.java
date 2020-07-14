package com.omerfpekgoz.notifications_durumabaglbildirimolusturma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button btnBildir;
    private NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBildir = findViewById(R.id.btnBildir);


        btnBildir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                durumaBaglı();    //Butona basıldğında bildirim gelecek
                gecikmeliBildirim();  //Butona basıldıktan 5 sn sonra bildirim gelecek

            }
        });
    }

    public void durumaBaglı() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(MainActivity.this, KarsilamaEkraniActivity.class);
        PendingIntent gidilecekIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //Orea güncelleme sonrası burası çalışacak

            String kanalId = "kanalId";  //Bildirimleri bir arada toplamak için kanal belirttik
            String kanalAd = "kanalAd";
            String kanalTanim = "kanalTanım";

            int kanalOncelik = NotificationManager.IMPORTANCE_HIGH;  //Öncelik olarak yüksek verdik


            NotificationChannel kanal = notificationManager.getNotificationChannel(kanalId);
            if (kanal == null) {  //Kanal null ise yani oluşturulmamış ise

                kanal = new NotificationChannel(kanalId, kanalAd, kanalOncelik);
                kanal.setDescription(kanalTanim);
                notificationManager.createNotificationChannel(kanal);

            }

            builder = new NotificationCompat.Builder(this, kanalId);
            builder.setContentTitle("Başlık");
            builder.setContentText("İçerik Mesaj");
            builder.setSmallIcon(R.drawable.resim);
            builder.setAutoCancel(true);  //Bildirim geldiğinde tıklanırsa otomatik kendini kapatma
            builder.setContentIntent(gidilecekIntent);  //Tıklanıldığında gidilecek ekran


        } else {     //Oreo öncesi burası çalışcacak


            builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("Başlık");
            builder.setContentText("İçerik Mesaj");
            builder.setSmallIcon(R.drawable.resim);
            builder.setAutoCancel(true);  //Bildirim geldiğinde tıklanırsa otomatik kendini kapatma
            builder.setContentIntent(gidilecekIntent);  //Tıklanıldığında gidilecek ekran
            builder.setPriority(Notification.PRIORITY_HIGH);  //Öncelik verdik

        }

        notificationManager.notify(1, builder.build());


    }

    public void gecikmeliBildirim() {  //Alarm Manager Notification için oluşturduk

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(MainActivity.this, KarsilamaEkraniActivity.class);
        PendingIntent gidilecekIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //Orea güncelleme sonrası burası çalışacak

            String kanalId = "kanalId";  //Bildirimleri bir arada toplamak için kanal belirttik
            String kanalAd = "kanalAd";
            String kanalTanim = "kanalTanım";

            int kanalOncelik = NotificationManager.IMPORTANCE_HIGH;  //Öncelik olarak yüksek verdik


            NotificationChannel kanal = notificationManager.getNotificationChannel(kanalId);
            if (kanal == null) {  //Kanal null ise yani oluşturulmamış ise

                kanal = new NotificationChannel(kanalId, kanalAd, kanalOncelik);
                kanal.setDescription(kanalTanim);
                notificationManager.createNotificationChannel(kanal);

            }

            builder = new NotificationCompat.Builder(this, kanalId);
            builder.setContentTitle("Başlık");
            builder.setContentText("İçerik Mesaj");
            builder.setSmallIcon(R.drawable.resim);
            builder.setAutoCancel(true);  //Bildirim geldiğinde tıklanırsa otomatik kendini kapatma
            builder.setContentIntent(gidilecekIntent);  //Tıklanıldığında gidilecek ekran


        } else {     //Oreo öncesi burası çalışcacak


            builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("Başlık");
            builder.setContentText("İçerik Mesaj");
            builder.setSmallIcon(R.drawable.resim);
            builder.setAutoCancel(true);  //Bildirim geldiğinde tıklanırsa otomatik kendini kapatma
            builder.setContentIntent(gidilecekIntent);  //Tıklanıldığında gidilecek ekran
            builder.setPriority(Notification.PRIORITY_HIGH);  //Öncelik verdik

        }

        Intent broadcastIntent = new Intent(MainActivity.this, BildirimYakalayici.class);  //Timer çalışınca yeni ekrana göndreceğiz

        broadcastIntent.putExtra("nesne", builder.build());
        PendingIntent gidilecekBroadcast = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        long gecikme = SystemClock.elapsedRealtime() + 5000;  //Sistem saatine 5 sn ekledik
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, gecikme, gidilecekBroadcast);

        //Eğer belli bir saat de bildirim gelmesini istersek
        //8,30 da başlayacak her 20 dk da bir bildirim verecek
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);  //Saat verdik:8
        calendar.set(Calendar.MINUTE, 30);       //Dakika verdik:30

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 20, gidilecekBroadcast); //20 dk da bir bildirim verecek


    }
}
