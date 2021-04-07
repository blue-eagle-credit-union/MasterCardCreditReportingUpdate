package com.blueeaglecreditunion.script;

import com.corelationinc.script.*;

import java.io.PrintStream;
import java.sql.*;
import java.util.Iterator;

public class MasterCardCreditReportingUpdate {
    Script script = null;
    private Connection connection = null;
    XMLSerialize xml = null;

    public static void runScript(Script script) throws Exception {
        MasterCardCreditReportingUpdate s = new MasterCardCreditReportingUpdate(script); //change these to match the class name
        s.run();
    }

    private MasterCardCreditReportingUpdate(Script value) { //change to match the class na,e
        script = value;
    }

    private void run() throws Exception {
        //Gathers any arguments you plug into the Batch setup
        Iterator<String> iterator = script.getArgumentIterator();
        String args = "";
        while (iterator.hasNext()) {
            if (!args.isEmpty()) {
                args = args + "; ";
            }
            args = args + iterator.next();
        }
        //Open DB Connection and grab the current posting date
        connection = script.openDatabaseConnection();
        String postingDate = script.retrievePostingDateString(connection);

        //Set up output reports to Keystone
        Report report = script.openReport("Posting Report: Reset Debit Cards", Report.Format.xml);
        report.setPostingOption(true);
        PrintStream os = new PrintStream(report.getBufferedOutputStream());

        // start the XML document
        xml = new XMLSerialize();
        xml.setXMLWriter(os);
        xml.putStartDocument();
        xml.putBatchQuery(postingDate);

        //Your method calls go here


        //end the XML document
        xml.put();
        xml.putEndDocument();
    }
    //"Main" method for debugging in your IDE, this is not what gets ran in Keystone
    public static void main(String[] args) throws Throwable {
        String javaClass = "-javaClass=" + "com.blueeaglecreditunion.script.EmploymentUpdateBatchScript"; // class path name of the batch script you want to run
        String javaMethod = "-javaMethod=" + "runScript"; // method to call in the script class
        String database = "-database=" + "D0062T04"; // database to read from XX is the client number and YYY is the env ex: D0035T00
        String databaseHome = "-databaseHome="; // can set this if you need to read in a file into your program
        String jdbcDriver = "-jdbcDriver=" + "com.ibm.db2.jcc.DB2Driver"; // DB2 driverCoachella2017
        String jdbcURLPrefix = "-jdbcURLPrefix=" + "jdbc:db2://208.69.139.109:50000"; // DB2 URL connection to your DB
        String userName = "-userName=" + "cdenty"; // aix username
        String password = "-password=" + myPassword.getPassword(); //aix password Add the class myPassword(in Useful Classes) to your project and add to
        //.gitignore to protect your password
        String passwordStdInFlag = "-passwordStdInFlag=" + "";
        String userHome = "-userHome=" + "C:/Users/CDAdmin/Desktop/Test/"; // location for the output folders
        String defaultThreadQueueServerCount = "-defaultThreadQueueServerCount=" + "1"; //the default thread server count. In this case it will always be 1.
        String javaClassPath = "-javaClassPath=" + "C:/Users/CDAdmin/Documents/Batch Scripts/EmploymentUpdateBatchScript/out/artifacts/EmploymentUpdateBatchScript_jar/EmploymentUpdateBatchScript.jar"; //Where your jar is stored. Debugger needs to find it to run it
        String resultPathName = "-resultPathName=" + "C:/Users/CDAdmin/Desktop/Test/OutputReport.xml";  // default output report
        String terminatePathName = "-terminatePathName=" + ""; //this argument is not used for local debugging on your PC. This
        //represents the path name of the batch job running
        //on the server in KeyStone in case the job needs to be terminated.
        String arg = "-arg="; // can pass arguments csv file names, params etc.. ex: test_csv_file.csv

        args = new String[]{
                javaClass, javaMethod, database, databaseHome, jdbcDriver,
                jdbcURLPrefix, userName, password, passwordStdInFlag, userHome,
                defaultThreadQueueServerCount, javaClassPath, resultPathName, terminatePathName, arg
        };
        com.corelationinc.script.Script.main(args);
    }//end main
}