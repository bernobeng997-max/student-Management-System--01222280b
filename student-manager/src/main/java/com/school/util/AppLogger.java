package com.school.util;

import java.io.FileWriter;
import java.time.LocalDateTime;

public class AppLogger {

    public static void log(String msg){

        try(FileWriter fw = new FileWriter("activity.log", true)){
            fw.write(LocalDateTime.now()+" : "+msg+"\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


