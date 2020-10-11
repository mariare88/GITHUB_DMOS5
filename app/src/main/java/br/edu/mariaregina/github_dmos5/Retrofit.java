package br.edu.mariaregina.github_dmos5;

import android.telecom.Call;

import br.edu.mariaregina.github_dmos5.model.Git;

public interface Retrofit{


    public interface RetrofitService {
        Call<Git> getDados(String nome);

    }
}
