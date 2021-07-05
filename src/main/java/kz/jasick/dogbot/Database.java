package kz.jasick.dogbot;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.*;
import java.util.Date;

public class Database {
    public static final String URL="jdbc:postgresql://localhost:3301/postgres";
    public static final String USER_NAME ="root";
    public static final String PASSWORD="root";
    public static void checkUser(Update update){
        Long chatID=update.getMessage().getChatId();
        String message=update.getMessage().getText();
        try {
            Connection connection= DriverManager.getConnection(URL,USER_NAME,PASSWORD);
            Statement statement=connection.createStatement();
            String q1="SELECT * FROM client WHERE chatid = '"+chatID+"'";
            ResultSet rs=statement.executeQuery(q1);
            if(rs.next()){
                Date date=new Date();
                String addMessage="INSERT INTO user"+chatID+" (date, message)" +
                        "VALUES ('"+ date +"','"+message+"');";
                statement.executeUpdate(addMessage);
            }
            else{
                String newTable="CREATE TABLE user"+chatID.toString()+"(" +
                        "    date  VARCHAR(50) NOT NULL ,"+
                        "    message VARCHAR(50) NOT NULL " +
                        ");";
                int x=statement.executeUpdate(newTable);
                Date date=new Date();
                String addMessage="INSERT INTO user"+chatID+" (date, message)" +
                        "VALUES ('"+ date +"','"+message+"');";
                String addUser="INSERT INTO client (chatid) "+
                        "VALUES ("+chatID+");";
                statement.executeUpdate(addUser);
            statement.executeUpdate(addMessage);
            }
        }
        catch (SQLException e){
        e.printStackTrace();
        throw new RuntimeException();
        }



    }
}
