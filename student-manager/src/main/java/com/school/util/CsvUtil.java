package com.school.util;

import java.nio.file.*;
import java.util.*;

public class CsvUtil {

    public static void export(String name,List<String> lines)throws Exception{
        Files.createDirectories(Paths.get("data"));
        Files.write(Paths.get("data/"+name),lines);
    }
}



