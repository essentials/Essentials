package com.carlgo11.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.bukkit.plugin.Plugin;

public class Report {

    /*
     * File created by Carlgo11. And uploaded to https://github.com/carlgo11/report
     * Please see LICENSE on https://github.com/carlgo11/report for the terms and conditions for distribution of this code.
     */
    private Plugin plugin;
    
    public static String Main(Plugin plugin)
    {
        String topic = "Report for " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " created. The following info is gathered from the config.yml & latest.log.\n\n";
        return topic + "CONFIG: \n{\n" + config(plugin).toString() + "}\n\nLatest Log:\n{\n" + latestlog(plugin).toString() + "}";
    }

    static StringBuilder config(Plugin plugin)
    {
        BufferedReader br = null;
        StringBuilder txt = new StringBuilder();
        try {
            String line;
            br = new BufferedReader(new FileReader("" + plugin.getDataFolder() + File.separatorChar + "config.yml"));
            while ((line = br.readLine()) != null) {
                txt.append(line);
                txt.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return txt;
    }

    static StringBuilder latestlog(Plugin plugin)
    {

        BufferedReader br = null;
        StringBuilder txt = new StringBuilder();
        if(plugin.getConfig().contains("report-log")){
        if (plugin.getConfig().getBoolean("report-log")) {
            try {
                String line;
                br = new BufferedReader(new FileReader("logs" + File.separatorChar + "latest.log"));
                while ((line = br.readLine()) != null) {
                    txt.append(line);
                    txt.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            txt.append("Access denied for latest.log. Contact the Server Owner.\n");
        }
        }else{
            txt.append("The developer(s) of this plugin have forgotten to set a report-log boolean in the config. Please report this error.\n");
        }
        return txt;
    }
}
