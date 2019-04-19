public class Get {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private String url;
    private String result;

    public Get(String url){
        this.url = url;
    }

    public String get(){

        result = null;

        try {
      
            URL myUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

            //Set configuration to connection
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            String inputLine;
            //Check if it's reading a null lline
            while((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);

            }

            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();

            SubjectsParser parser = new SubjectsParser();
            subjects = parser.sParser(result);
            
        } catch(IOException e){
            e.printStackTrace();
        }

        return result;

    }
}