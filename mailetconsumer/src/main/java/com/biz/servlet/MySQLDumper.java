package com.biz.servlet;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLDumper {

private static String ip="35.186.182.51";
private static String port="3306";
private static String database="requestprocessing";
private static String user="root";
private static String pass="root";
private static String path="D:\\rp.sql";

public static void export(){
String dumpCommand = "mysqldump " + database + " -h " + ip + " -u " + user +" -p" + pass;
Runtime rt = Runtime.getRuntime();
File test=new File(path);
PrintStream ps;

try{
Process child = rt.exec(dumpCommand);
ps=new PrintStream(test);
InputStream in = child.getInputStream();
int ch;
while ((ch = in.read()) != -1) {
ps.write(ch);
System.out.write(ch); //to view it by console
}

InputStream err = child.getErrorStream();
while ((ch = err.read()) != -1) {
System.out.write(ch);
}
}catch(Exception exc) {
exc.printStackTrace();
}
}

public static void main(String args[]){
//export();
	try{  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://35.186.182.51:3306/requestprocessing","root","root");  
		System.out.println(con);
		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery("select * from emp");  
		while(rs.next())  
		System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
		con.close();  
		}catch(Exception e){ }  
		
}
}
