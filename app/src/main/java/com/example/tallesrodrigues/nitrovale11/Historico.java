package com.example.tallesrodrigues.nitrovale11;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

/**
 * Created by TallesRodrigues on 7/25/2016.
 */
public class Historico extends AppCompatActivity {

    // http://www.devmedia.com.br/criando-um-crud-com-android-studio-e-sqlite/32815
    private ListView myList;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        /*
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);


        Button sendEmail = (Button) findViewById(R.id.sendEmail);

        alertDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendEmail();
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setMessage("Enviar dados?");



        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        }); */

        DatabaseController crud = new DatabaseController(getBaseContext());
        cursor = crud.loadData();

        String[] nomeCampos = new String[] {"R","G","B","SPAD"};
        int[] idViews = new int[] {R.id.textView2, R.id.textView3,R.id.textView4,R.id.textView5};

       // Log.e("Intensity ", String.valueOf(cursor.getDouble(4)));
       // Log.e("SPAD 1 ", String.valueOf(cursor.getDouble(5)));

        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(getBaseContext(),
                R.layout.list_view_row,cursor,nomeCampos,idViews, 0);
        myList = (ListView)findViewById(R.id.listView);
        myList.setAdapter(adaptador);

        sendEmail();
    }


    private void sendEmail() {
        //Getting content for email
       // String email = mEmailView.getText().toString();
        String email= "tallesnr@gmail.com";
        String subject = "ClassBook";
        String message = "R\tG\tB\tIntensity\tSPAD\n";
        Cursor aux = cursor;

        aux.moveToPosition(-1);
        while(aux.moveToNext()){
            message = message+ aux.getDouble(1)
                    +"\t"+aux.getDouble(2)
                    +"\t"+aux.getDouble(3)
                    +"\t"+aux.getDouble(4)
                    +"\t"+aux.getDouble(5)
                    +"\n";
        }
        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }
}
