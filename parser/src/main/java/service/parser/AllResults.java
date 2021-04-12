package service.parser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

public class AllResults {
    private String keyword;
    ArrayList<SearchResult> results;

    public AllResults(String keyword, ArrayList<SearchResult> results){
        this.keyword = keyword;
        this.results = results;
    }

    public String getKeyword(){
        return this.keyword;
    }

    public ArrayList<SearchResult> getResults(){
        return this.results;
    }

    public synchronized void uploadToBD(){
        String connectionUrl = "jdbc:sqlserver://kursova-robota.database.windows.net:1433;database=Kursova;user=yaroslav@kursova-robota;password={i_am_batman10};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement()){
            String keyword = getKeyword();
            ArrayList<SearchResult> arrayList = getResults();
            UUID uuidForKeyword = UUID.randomUUID();

            String ifNotExists = "IF NOT EXISTS ( SELECT * FROM Keyword WHERE Keyword='" + keyword + "')\n" +
                    "BEGIN\n" +
                    "    INSERT INTO Keyword (UniqueID, Keyword) VALUES ('" + uuidForKeyword.toString() + "','" + keyword + "')\n" +
                    "END";
            stmt.executeUpdate(ifNotExists);

            String keywordID = "";
            String selectId = "SELECT UniqueID FROM Keyword WHERE Keyword = '" + keyword + "';";
            ResultSet rsKeywordID = stmt.executeQuery(selectId);
            while (rsKeywordID.next()) {
                keywordID = rsKeywordID.getString(1);
            }

            UUID uuidForAllResults = UUID.randomUUID();
            StringBuilder insertSearchResult = new StringBuilder();

            String insertAllResults = "INSERT INTO All_Results(UniqueID ,dateTime , Keyword_ID)" +
                    "VALUES('" + uuidForAllResults.toString() + "',SYSUTCDATETIME(),'" + keywordID + "');";
            insertSearchResult.append(insertAllResults);

            for (int i = 0; i < arrayList.size(); i++) {
                int position = i+1;
                UUID uuidForSearchResult = UUID.randomUUID();
                String snippet = arrayList.get(i).getSnippet().replace("'","''");
                if (snippet.length() > 127) {
                    snippet = snippet.substring(0, 127);
                }
                insertSearchResult.append("INSERT INTO Search_Result(UniqueID, url, snippet,position, All_Results_ID)" +
                        "VALUES('" + uuidForSearchResult.toString() + "', " +
                        "'" + arrayList.get(i).getUrl() + "'," +
                        " '" + snippet.replace("\"","") + "'," +
                        " " + position + "," +
                        " '" + uuidForAllResults.toString() + "');");
            }
            stmt.executeUpdate(insertSearchResult.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
