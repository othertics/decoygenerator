import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import Database_utility.Database;
import Database_utility.resultDatabase;

public class decoyGenerator {
	public static void main(String arg[]) throws IOException{
		String inputDatabaseFile = "";	//input target database file path and name.
		String outputDatabaseFile = "";	// input output database file path and name.
		
		int flag = 3;	//0 == reverse, 1 == pseudo reverse, 2 == shuffle 3 == pseudo shuffle

		BufferedReader databaseResult = new BufferedReader(new FileReader(inputDatabaseFile));
		PrintWriter printResult = new PrintWriter(new BufferedWriter(new FileWriter(outputDatabaseFile)));
		
		String line;
		String lineString = "";
		String preHeader = "";
		int tcount = 0;
		int dcount = 0;
		
		while((line = databaseResult.readLine()) != null){
			if(line.startsWith(">")) {
				if(lineString != "") {
					printResult.println(lineString);
					tcount++;
					printResult.println(">XXX_" + preHeader.substring(1));
					dcount++;
					if(flag == 0) printResult.println(reverseDecoy(lineString));
					else if(flag == 1) printResult.println(pseudoReverseDecoy(lineString));
					else if(flag == 2) printResult.println(shuffleDecoy(lineString));
					else if(flag == 3) printResult.println(pseudoShuffleDecoy(lineString));
					
					lineString = "";
				}
				preHeader = line;
				printResult.println(line);
			}
			else {
				line = line.replaceAll("\r|\n|\r\n|\n\r","");
				lineString = lineString + line;
			}
		}
		printResult.println(lineString);
		tcount++;
		printResult.println(">XXX_" + preHeader.substring(1));
		dcount++;
		
		if(flag == 0) printResult.println(reverseDecoy(lineString));	
		else if(flag == 1) printResult.println(pseudoReverseDecoy(lineString));
		else if(flag == 2) printResult.println(shuffleDecoy(lineString));
		else if(flag == 3) printResult.println(pseudoShuffleDecoy(lineString));
		
		databaseResult.close();
		printResult.close();
		
		
		System.out.println("target : " + tcount);
		System.out.println("decoy : " + dcount);
		System.out.println("END decoy maker");	
	}
	
	public static String reverseDecoy(String targetProtein){
		StringBuffer targetString = new StringBuffer(targetProtein);
		
		return targetString.reverse().toString();
	}
	

	public static String pseudoReverseDecoy(String targetProtein){
		ArrayList<Character> decoyList = new ArrayList<Character>();
		String decoyString = "";
		int swap = 0;
		
		for(int i=0;i<targetProtein.length();i++){
			if(targetProtein.charAt(i) == 'K' || targetProtein.charAt(i) == 'R'){
				Collections.reverse(decoyList);
				for(int j=0;j<decoyList.size();j++) decoyString = decoyString + decoyList.get(j);
				decoyString = decoyString + targetProtein.charAt(i);
				swap = 1;
			}
			if(swap == 1){
				decoyList = new ArrayList<Character>();
				swap = 0;
			}
			else decoyList.add(targetProtein.charAt(i));
		}
		
		Collections.reverse(decoyList);
		for(int i=0;i<decoyList.size();i++) decoyString = decoyString + decoyList.get(i);

		return decoyString;
	}
	
	public static String shuffleDecoy(String targetProtein){
		ArrayList<Character> decoyList = new ArrayList<Character>();
		String decoyString = "";
		
		for(int i=0;i<targetProtein.length();i++) decoyList.add(targetProtein.charAt(i));
		Collections.shuffle(decoyList);
		
		for(int i=0;i<decoyList.size();i++) decoyString = decoyString + decoyList.get(i);
		
		return decoyString;
		
	}
	
	public static String pseudoShuffleDecoy(String targetProtein){
		ArrayList<Character> decoyList = new ArrayList<Character>();
		String decoyString = "";
		int swap = 0;
		
		for(int i=0;i<targetProtein.length();i++){
			if(targetProtein.charAt(i) == 'K' || targetProtein.charAt(i) == 'R'){
				Collections.shuffle(decoyList);
				for(int j=0;j<decoyList.size();j++) decoyString = decoyString + decoyList.get(j);
				decoyString = decoyString + targetProtein.charAt(i);
				swap = 1;
			}
			if(swap == 1) {
				decoyList = new ArrayList<Character>();
				swap = 0;
			}
			else decoyList.add(targetProtein.charAt(i));
		}
		
		Collections.shuffle(decoyList);
		for(int i=0;i<decoyList.size();i++) decoyString = decoyString + decoyList.get(i);

		return decoyString;
	}

}
