package me.alen_alex.tickettool.data;

import me.alen_alex.tickettool.configuration.Configuration;
import me.alen_alex.tickettool.database.mysql.MySQL;

public class SQLDatabase {

    public MySQL initMySQL(){
        return new MySQL("jdbc:mysql://"+ Configuration.getSqlHost()+":"+Configuration.getSqlPort()+"/"+Configuration.getSqlDatabase()+"?autoReconnect=true",Configuration.getSqlUsername(),Configuration.getSqlPassword(),false);
    }

}
