package me.alen_alex.tickettool.data;

import me.alen_alex.tickettool.database.mysql.MySQL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {

    private MySQL mySQL;

    public DatabaseManager(MySQL mySQL) {
        this.mySQL = mySQL;
    }

    public void createTables(){
        try {
            if(mySQL.getConnection().isClosed())
                return;
            PreparedStatement ps = mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `tickettool` (`id` int NOT NULL AUTO_INCREMENT PRIMARY KEY, `uuid` VARCHAR(50) NOT NULL ,`channelid` VARCHAR(30) NOT NULL ,`userid` VARCHAR(40) NOT NULL,`group` VARCHAR(30), `claimed` BOOLEAN NOT NULL , `claimedBy` VARCHAR(30), `users` TEXT);");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getTickets(){
        ResultSet set = null;
        try {
            if(mySQL.getConnection().isClosed())
                return null;

            PreparedStatement ps = mySQL.getConnection().prepareStatement("SELECT * FROM `tickettool`;");
            set = ps.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return set;
    }

    public int getUserTicketCount(long userID){
        int limit = 0;
        try {
            if(mySQL.getConnection().isClosed())
                return 0;
            PreparedStatement ps = mySQL.getConnection().prepareStatement("SELECT `channelid` FROM `tickettool` WHERE `userid` = '"+userID+"';",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet set = ps.executeQuery();
            if(set.last()){
                limit = set.getRow();
            }
            ps.close();
            set.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return limit;
    }

    public  void deleteATicket(long ticketID){
        try {
            if(mySQL.getConnection().isClosed())
                return;

            PreparedStatement ps = mySQL.getConnection().prepareStatement("DELETE FROM `tickettool` WHERE `channelid` = '"+ticketID+"';");
            ps.executeUpdate();
            ps.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean registerNewTicket(long userID, long chnID, UUID uuid){
        try {
            if(mySQL.getConnection().isClosed())
                return false;
            PreparedStatement ps = mySQL.getConnection().prepareStatement("INSERT INTO `tickettool` (`uuid`,`userid`,`channelid`,`claimed`) VALUES ('"+uuid.toString()+"','"+userID+"','"+chnID+"',false);");
            ps.executeUpdate();
            ps.close();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
