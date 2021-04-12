package service.parser;

import java.sql.*;

public class GetResults {
    private final String keyword;
    private String results;


    public GetResults(String keyword) {
        this.keyword = keyword;
        StringBuilder str = new StringBuilder();
        String connectionUrl = "jdbc:sqlserver://kursova-robota.database.windows.net:1433;database=Kursova;user=yaroslav@kursova-robota;password={i_am_batman10};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement()) {
            String getResult = "SELECT * FROM Search_Result INNER JOIN All_Results ON Search_Result.All_Results_ID = All_Results.UniqueID " +
                    "WHERE All_Results_ID IN (" +
                    "SELECT UniqueID FROM All_Results WHERE Keyword_ID = (" +
                    "SELECT UniqueID FROM Keyword WHERE keyword='" + keyword + "'))" +
                    "ORDER BY (position);";
            ResultSet res = stmt.executeQuery(getResult);
            str.append("{\"results\":[");
            while (res.next()) {
                str.append("{\"position\":" + res.getInt("position") + ", \"datetime\":\"" +
                        res.getTimestamp("dateTime")
                        + "\", \"url\":\"" +
                        res.getString("url") + "\", \"snippet\":\"" +
                        res.getString("snippet")+"\"},");
            }
            str.deleteCharAt(str.length()-1);
            str.append("]}");
        } catch (SQLException throwables) {

        }
        if(str.length() != 13){
            this.results = str.toString();
        }else{
            this.results = "Not Found";
        }

    }

    public String getResults(){
        return this.results;
    }
}
