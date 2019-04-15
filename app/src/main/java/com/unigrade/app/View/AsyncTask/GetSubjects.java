package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;

public class GetSubjects extends AsyncTask<Integer, Integer, Integer> {

    //É onde acontece o processamento
    //Este método é executado em uma thread a parte,
    //ou seja ele não pode atualizar a interface gráfica,
    //por isso ele chama o método onProgressUpdate,
    // o qual é executado pela
    //UI thread.
    @Override
    protected Integer doInBackground(Integer... params) {

            //Notifica o Android de que ele precisa
            //fazer atualizações na
            //tela e chama o método onProgressUpdate
            //para fazer a atualização da interface gráfica
            //passando o valor do
            //contador como parâmetro.
            publishProgress();

            return 1;
    }

    // É invocado para fazer uma atualização na
    // interface gráfica

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

}