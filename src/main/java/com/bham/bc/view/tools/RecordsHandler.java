package com.bham.bc.view.tools;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class RecordsHandler {
    public static ArrayList<Records> records;
    public static JSONArray jsonArrayToFile;
    private static JSONArray jsonArray;
    private static JSONArray albums;


    public RecordsHandler() {


    }
    static {

        jsonArrayToFile=new JSONArray();
    }

    public static ObservableList<Records> initTable(){
        File file = new File("src/main/resources/model/scores.json");
        if (!file.exists()) {
            try {
                FileOutputStream fileOutputStream=new FileOutputStream("src/main/resources/model/scores.json");
                byte[] data="[]".getBytes();
                fileOutputStream.write(data);
                fileOutputStream.flush();
                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //read from Json file
        try {
            parseJsonFile("src/main/resources/model/scores.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObservableList<Records> data = FXCollections.observableArrayList(records);
        //System.out.println("data len="+data.size());

        for (int i = 0; i < albums.length(); i++) {
            jsonArrayToFile.put(albums.get(i));
        }

        return data;
    }

    /**
     * get the string format of date
     * @return string format of date
     */

    public static String getDateOfG(){

        String[] strNow1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().split("-");

        Integer.parseInt(strNow1[0]);			//获取年
        Integer.parseInt(strNow1[1]);			//获取月
        Integer.parseInt(strNow1[2]);			//获取日

        return strNow1[2]+"/"+strNow1[1]+"/"+strNow1[0];

    }





    /**
     * sort the json array after create new records
     * @return data to fit table
     */
    public ObservableList<Records> sortAndGetData(){
        //third step is to sort after add the new records
        sort();

        try {
            writeJsonToFile("src/main/resources/model/scores.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //read from Json file
        try {
            parseJsonFile("src/main/resources/model/scores.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObservableList<Records> data = FXCollections.observableArrayList(records);
        //System.out.println("data len="+data.size());

        return data;
    }

    /**
     * to format json
     */
    public static class Tool {
        private boolean isTab = true;
        public String stringToJSON(String strJson) {
            int tabNum = 0;
            StringBuffer jsonFormat = new StringBuffer();
            int length = strJson.length();
            for (int i = 0; i < length; i++) {
                char c = strJson.charAt(i);
                if (c == '{') {
                    tabNum++;
                    jsonFormat.append(c + "\n");
                    jsonFormat.append(getSpaceOrTab(tabNum));
                } else if (c == '}') {
                    tabNum--;
                    jsonFormat.append("\n");
                    jsonFormat.append(getSpaceOrTab(tabNum));
                    jsonFormat.append(c);
                } else if (c == ',') {
                    jsonFormat.append(c + "\n");
                    jsonFormat.append(getSpaceOrTab(tabNum));
                } else {
                    jsonFormat.append(c);
                }
            }
            return jsonFormat.toString();
        }
        public String getSpaceOrTab(int tabNum) {
            StringBuffer sbTab = new StringBuffer();
            for (int i = 0; i < tabNum; i++) {
                if (isTab) {
                    sbTab.append('\t');
                } else {
                    sbTab.append("    ");
                }
            }
            return sbTab.toString();
        }
    }

    /**
     * sort the socres automatically.
     */
    public static void sort(){
        // keep the first 10 record. And when new record come, delete some old records
        int len=jsonArrayToFile.length();
        jsonArray=new JSONArray();
        if (jsonArrayToFile.length()>10){
            for (int i=0;i<len;i++){
                if (10+i<len){
                    jsonArray.put(i,jsonArrayToFile.get(10+i));
                }
                if (10+i>=len){
                    jsonArray.put(i,jsonArrayToFile.get(i-(len-10)));
                }
            }

        }else {
            jsonArray=jsonArrayToFile;
        }
        //System.out.println("jsonArray"+jsonArray.toString());
        for (int i=0;i<len;i++) {
            if (i<10){
                jsonArrayToFile.put(i,jsonArray.get(i));
            }

            if (i>=10){
                jsonArrayToFile.remove(i);
                i--;
                len--;
            }
        }



        //sort the first 10 records of array
        jsonArrayToFile=jsonArraySort(jsonArrayToFile);

        //set the rank according to the order of array
        for (int i=0;i<jsonArrayToFile.length();i++){
            JSONObject jsonObject=(JSONObject) jsonArrayToFile.get(i);
            jsonObject.put("rank",(i+1)+"");
        }
    }


    /**
     * sort the Json Array according to score
     * @param jsonArr json array to be sorted
     * @return return the sorted json array
     */
    public static JSONArray jsonArraySort(JSONArray jsonArr) {

        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonValues.add(jsonArr.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            private static final String KEY_NAME = "score";
            double score1;
            double score2;
            @Override
            public int compare(JSONObject a, JSONObject b) {
                try {
                    score1= Double.valueOf(a.getString(KEY_NAME));
                    score2= Double.valueOf(b.getString(KEY_NAME));
                } catch (JSONException e) {
                    // 处理异常
                }
                //这里是按照时间逆序排列,不加负号为正序排列
                return (int) (-score1+score2);
            }
        });
        for (int i = 0; i < jsonArr.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }

    /**
     * write Json array to the file
     * @param filename
     */
    public static void writeJsonToFile(String filename) throws Exception {
        Tool tool=new Tool();

        String jsonString=jsonArrayToFile.toString();//to string
        //System.out.println(jsonString);
        String JsonString=tool.stringToJSON(jsonString);//format string

        Files.write(Paths.get(filename), JsonString.getBytes());
    }


    /**
     * create the class for data in the table
     */
    public static class Records{
        private  SimpleStringProperty rank;
        private  SimpleStringProperty name;
        private  SimpleStringProperty score;
        private  SimpleStringProperty date;

        public Records( String name,String  score, String date) {
            this.rank=new SimpleStringProperty("0");
            this.name = new SimpleStringProperty(name);
            this.score = new SimpleStringProperty(score);
            this.date =new SimpleStringProperty(date);
        }

        /**
         * Alternate constructor which sets the current date for the record automatically
         * @param name  user's name to be put in the records list
         * @param score user's score their position in the leaderboard will depend on
         */
        public Records(String name, double score) {
            this.rank=new SimpleStringProperty("0");
            this.name = new SimpleStringProperty(name);
            this.score = new SimpleStringProperty(score + "");
            this.date =new SimpleStringProperty(getDateOfG());
        }


        public String getRank() {
            return rank.get();
        }

        public SimpleStringProperty rankProperty() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank.set(rank);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getScore() {
            return score.get();
        }

        public SimpleStringProperty scoreProperty() {
            return score;
        }

        public void setScore(String score) {
            this.score.set(score);
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public void setDate(String date) {
            this.date.set(date);
        }

        /**
         * to convert java object to JSon Object
         * @return
         */
        public JSONObject toJSON() {

            JSONObject jo = new JSONObject();
            jo.put("rank", rank.getValue());
            jo.put("name", name.getValue());
            jo.put("score",score.getValue());
            jo.put("date",date.getValue());

            return jo;
        }

        /**
         * put json object in to Json array (for Json file)
         */
        public void putIntoArray(){
            jsonArrayToFile.put(toJSON());
        }
    }

    /**
     * create a record and put into a Json array
     * @param record
     */
    public void createRecord(Records record){
        record.putIntoArray();
    }


    /**
     * read file to get a string and parse string
     * @param fileName
     * @throws IOException
     */
    public static void parseJsonFile(String fileName) throws IOException {
        FileInputStream fileInputStream=new FileInputStream(fileName);
        byte[] array=new byte[1024*1024];
        int num=fileInputStream.read(array);
        String s=new String(array);

        parse(s);
    }

    /**
     * parse the string and make a array of records
     * @param responseBody
     * @return
     */
    public static ArrayList<Records> parse(String responseBody){
        records=new ArrayList<>();
        albums = new JSONArray(responseBody);
        //System.out.println(albums.length());
        for (int i = 0; i < albums.length(); i++){
            JSONObject album = albums.getJSONObject(i);
            String rank=album.getString("rank");
            String name = album.getString("name");
            String score = album.getString("score");
            String date = album.getString("date");
            Records record=new Records(name,score,date);
            record.setRank(rank);
            records.add(i,record);
            //System.out.println(rank + " | " + name + " | " + score+" | "+date);

        }
        return records;
    }
}