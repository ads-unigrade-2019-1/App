package com.unigrade.app.View.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.unigrade.app.Controller.SubjectsParser;
import com.unigrade.app.Model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetSubjects extends AsyncTask<String, Integer, List> {

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    //É onde acontece o processamento
    //Este método é executado em uma thread a parte,
    //ou seja ele não pode atualizar a interface gráfica,
    //por isso ele chama o método onProgressUpdate,
    // o qual é executado pela
    //UI thread.
    @Override
    protected List doInBackground(String... params) {
        String stringUrl = params[0];
        String result = "";
        List<String> parsedResult = new ArrayList<>();
        List<Subject> subjects;
        String inputLine;

        try {
            //Criar um objeto URL contendo nossa url
            URL myUrl = new URL(stringUrl);

            //Criar a conexão
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

            //Setando os metodos e timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Conectar-se a nossa url
            connection.connect();

            //Criar um novo inputStreamReader

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());


            //Cria um novo buffered reader e String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();


            //Checa se a linha que estamos lendo é nula
            while((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);

            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();

            SubjectsParser parser = new SubjectsParser();
            subjects = parser.sParser(result);

            for(Subject subject: subjects){
                parsedResult.add(subject.getCodigoMateria() + " - " + subject.getNomeMateria());
            }

        }
        catch(IOException e){
            e.printStackTrace();
            parsedResult = null;
        }



        //Notifica o Android de que ele precisa
        //fazer atualizações na
        //tela e chama o método onProgressUpdate
        //para fazer a atualização da interface gráfica
        //passando o valor do
        //contador como parâmetro
        publishProgress();
        return parsedResult;

    }

    // É invocado para fazer uma atualização na
    // interface gráfica
    @Override
    protected void onProgressUpdate(Integer... values) {

    }

}