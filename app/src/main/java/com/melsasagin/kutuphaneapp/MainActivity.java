package com.melsasagin.kutuphaneapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<BooksItem> booksItemArrayList;
    BooksRecyclerAdapter adapter;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);//RecyclerView'ın boyutunu sabitleyerek performansı arttırmak hedeflenir
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//rw içindeki öğelerin lineer (dikey) gözükmesini sağlar

        booksItemArrayList = new ArrayList<>();

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                viewDialogAdd.showDialog(MainActivity.this);
            }
        });
        readData();
    }

    private void readData() {
        databaseReference.child("Books").orderByChild("kitapName").addValueEventListener(new ValueEventListener() {
            //orderByChild ile kitap isimleri alfabetik bir sırada listelenmiş olacak
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                booksItemArrayList.clear();//mesela 10 kitabım olsun ve 1 kitap daha ekledim
                //o zaman eskilerle ve yeniden listeyle beraber 21 tane kitap gözükür bu yüzden önceki listeyi silmek zorundayım
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BooksItem books = dataSnapshot.getValue(BooksItem.class);
                    booksItemArrayList.add(books);
                }
                adapter = new BooksRecyclerAdapter(MainActivity.this, booksItemArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();//değişiklikler adaptöre bildirilir
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public class ViewDialogAdd {
        public void showDialog(Context context) {
            final Dialog dialog = new Dialog(context);//context uygulama ortamı hakkında bilgi sağlar ve uygulamanın diğer bileşenleriyle etkileşim kurabilmesini sağlar
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.setCancelable(false);//Dialog penceresinin dışarıya tıklama veya geri tuşuna basarak kapatılmasını engeller
            dialog.setContentView(R.layout.alert_dialog_add_new_book);

            EditText textName = dialog.findViewById(R.id.textName);
            EditText textAuthor = dialog.findViewById(R.id.textAuthor);
            EditText textYayinevi = dialog.findViewById(R.id.textYayinevi);

            Button buttonAdd = dialog.findViewById(R.id.buttonAdd);

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = "kitap" + new Date().getTime();//kayıtlar farksız olsun diye tarihi zamana çevirdim
                    String name = textName.getText().toString();
                    String author = textAuthor.getText().toString();
                    String yayinevi = textYayinevi.getText().toString();

                    if (name.isEmpty() || author.isEmpty() || yayinevi.isEmpty()) {
                        Toast.makeText(context, "Lütfen tüm detayları giriniz", Toast.LENGTH_SHORT).show();
                    } else {
                        databaseReference.child("Books").child(id).setValue(new BooksItem(id, name, author, yayinevi));
                        Toast.makeText(context, "Kayıt gerçekleştirildi !", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//Dialog nesnesinin penceresine uygulanan arka plan transparan belirlenir
            dialog.show();
        }
    }
}