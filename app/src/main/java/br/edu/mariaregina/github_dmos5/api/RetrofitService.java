package br.edu.mariaregina.github_dmos5.api;


import android.telecom.Call;

import br.edu.mariaregina.github_dmos5.model.Git;

public interface RetrofitService {

    Call<Git> getDados(String nome);

        }



