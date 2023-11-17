package com.melsasagin.kutuphaneapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<BooksItem> booksItemArrayList;
    DatabaseReference databaseReference;

    public BooksRecyclerAdapter(Context context, ArrayList<BooksItem> booksItemArrayList) {
        this.context = context;
        this.booksItemArrayList = booksItemArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    /*
    1. onCreateViewHolder() -> XML'imizi (bkz:book_item.xml) bağlama işlemini yapacağımız methoddur.
    2. onBindViewHolder() -> Görünümleri tutması için oluşturduğumuz ViewHolder sınıfımız bağlandığında; XML / Layoutumuz içerisinde hangi verileri göstermek istediğimizi söyleyen methoddur.
    3. getItemCount() -> XML'imizin kaç defa oluşturulacağını söylediğimiz methoddur.
    */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);//layoutinflate örneği context nesnesinden (MainActivity) alınır
        View view = layoutInflater.inflate(R.layout.book_item, parent, false);
        //attachToRoot değeri false olarak ayarlanırsa, inflate edilen düzen dosyası belirtilen kök düğümüne bağlanmaz
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BooksItem books = booksItemArrayList.get(position);

        holder.textName.setText("Kitap adı : " + books.getKitapName());
        holder.textAuthor.setText("Yazar : " + books.getKitapAuthor());
        holder.textYayinevi.setText("Yayınevi : " + books.getKitapYayinevi());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Lütfen bir işlem seçiniz")
                        .setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                showUpdateDialog(context, books.getKitapID(), books.getKitapName(), books.getKitapAuthor(), books.getKitapYayinevi());
                            }
                        })
                        .setNegativeButton("Sil", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                showDeleteDialog(context, books.getKitapID());
                            }
                        })
                        .setNeutralButton("Vazgeç", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return booksItemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {//görünümleri tutan yardımcı bir sınıftır
        TextView textName;
        TextView textAuthor;
        TextView textYayinevi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textAuthor = itemView.findViewById(R.id.textAuthor);
            textYayinevi = itemView.findViewById(R.id.textYayinevi);
        }
    }

        public void showUpdateDialog(Context context, String id, String name, String author, String yayinevi) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.setCancelable(false);//Dialog penceresinin dışarıya tıklama veya geri tuşuna basarak kapatılmasını engeller
            dialog.setContentView(R.layout.alert_dialog_add_new_book);

            EditText textName = dialog.findViewById(R.id.textName);
            EditText textAuthor = dialog.findViewById(R.id.textAuthor);
            EditText textYayinevi = dialog.findViewById(R.id.textYayinevi);

            textName.setText(name);
            textAuthor.setText(author);
            textYayinevi.setText(yayinevi);

            Button buttonUpdate = dialog.findViewById(R.id.buttonAdd);

            buttonUpdate.setText(R.string.update);

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newName = textName.getText().toString();
                    String newAuthor = textAuthor.getText().toString();
                    String newYayinevi = textYayinevi.getText().toString();

                    if (newName.isEmpty() || newAuthor.isEmpty() || newYayinevi.isEmpty()) {
                        Toast.makeText(context, "Lütfen tüm detayları giriniz", Toast.LENGTH_SHORT).show();
                    } else {
                        if (newName.equals(name) && newAuthor.equals(author) && newYayinevi.equals(yayinevi)) {
                            Toast.makeText(context, "Herhangi bir değişiklik yapmadınız", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child("Books").child(id).setValue(new BooksItem(id, newName, newAuthor, newYayinevi));
                            Toast.makeText(context, "Kitap detayları başarıyla güncellendi !", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

        public void showDeleteDialog(Context context, String id) {
            databaseReference.child("Books").child(id).removeValue();
            Toast.makeText(context, "Kitap başarıyla silindi !", Toast.LENGTH_SHORT).show();
        }
}
