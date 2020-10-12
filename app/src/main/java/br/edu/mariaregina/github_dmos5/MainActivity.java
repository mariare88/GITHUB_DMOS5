package br.edu.mariaregina.github_dmos5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import br.edu.mariaregina.github_dmos5.api.RetrofitService;
import br.edu.mariaregina.github_dmos5.model.Git;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_PERMISSION = 64;
    private static final String BASE_URL = "https://api.github.com/users<nome-do-usuario>/repos" ;
    private Retrofit mRetrofit;

    private EditText nomeEditText;
    private Button buscarButton;
    private ConstraintLayout dadosConstraintLayout;
    private TextView nomeTextView;
    private TextView idTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLayoutElements();
        }
     private void getLayoutElements(){
         nomeEditText = findViewById(R.id.edittext_nome);
         dadosConstraintLayout = findViewById(R.id.constraint_dados);
         nomeTextView = findViewById(R.id.textview_nome);
         idTextView = findViewById(R.id.textview_id);
         //complementoTextView = findViewById(R.id.textview_complemento);
         //bairroTextView = findViewById(R.id.textview_bairro);
         //localidadeTextView = findViewById(R.id.textview_localidade);
         //ufTextView = findViewById(R.id.textview_uf);
         buscarButton = findViewById(R.id.button_buscar);
         buscarButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_buscar:
                if(temPermissao()){
                    github_dmos5();
                }else{
                    solicitaPermissao();

                }
        }
    }
    private void github_dmos5() {
        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        String nomeString = nomeEditText.getText().toString();
        if (nomeString.length() != 8) {

            Toast.makeText(this, "nome invalido", Toast.LENGTH_SHORT).show();

        } else {
            nomeString += "/json";

            RetrofitService mRetrofitService = mRetrofit.create(RetrofitService.class);
            Call<Git> call = mRetrofitService.getDados(nomeString);
            call.enqueue(new Callback<Git>() {
                @Override
                public void onResponse(Call<Git> call, Response<Git> response) {
                    if (response.isSuccessful()) {
                        Git git = response.body();
                        updateUI(git);
                    }
                }

                @Override
                public void onFailure(Call<Git> call, Throwable t) {

                    Toast.makeText(MainActivity.this, "erro procurar nome", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private boolean temPermissao(){
           return ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;

        }



    private void solicitaPermissao(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
            final Activity activity = this;
            new AlertDialog.Builder(this)
                    .setMessage("necessario permissão a internet para busca")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton("não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.INTERNET
                    },
                    REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {

                if (permissions[i].equalsIgnoreCase(Manifest.permission.INTERNET) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    github_dmos5();
                }

            }
        }
    }

    private void updateUI(Git git){
        if(git!= null){
            dadosConstraintLayout.setVisibility(View.VISIBLE);
            nomeTextView.setText(git.getNome());

        }
    }

}