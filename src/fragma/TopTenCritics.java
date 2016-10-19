package fragma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TopTenCritics {

	private static String currentLine="";
	public static boolean ASC = true;
	public static boolean DESC = false;
	public static List morethan40View = new ArrayList();
	public static Map<Integer,String> userIdAgeMap = new HashMap<Integer,String>();

	public static void mapUserIdAge(BufferedReader userData,
			BufferedReader movieData, Map<String, Object> fileMap) throws NumberFormatException, IOException {
		FileInputStream userDataFile=(FileInputStream) fileMap.get("userDataFile");
		FileInputStream movieDataFile=(FileInputStream) fileMap.get("movieDataFile");
		FileInputStream ratingDataFile=(FileInputStream) fileMap.get("ratingDataFile");

		userDataFile.getChannel().position(0);
		ratingDataFile.getChannel().position(0);
		movieDataFile.getChannel().position(0);

		String movieName="";
		String movieIdList="";
		currentLine=userData.readLine();
		while(currentLine!=null){
			String[] movieLineData=currentLine.split("::");
			String userIdList = movieLineData[0];
			String ageName = movieLineData[1];
			userIdAgeMap.put(Integer.parseInt(movieIdList), movieName);
			currentLine=userData.readLine();
		}
		System.out.println(userIdAgeMap);

	}

	private static List findMoreThan40ViewsMovies(BufferedReader ratingData, BufferedReader movieData, BufferedReader userData, Map<String, Object> fileMap) throws IOException {
		// TODO Auto-generated method stub

		FileInputStream userDataFile=(FileInputStream) fileMap.get("userDataFile");
		FileInputStream movieDataFile=(FileInputStream) fileMap.get("movieDataFile");
		FileInputStream ratingDataFile=(FileInputStream) fileMap.get("ratingDataFile");
		Map<Integer, String> movieMap = (Map<Integer, String>) fileMap.get("movieMap");

		userDataFile.getChannel().position(0);
		ratingDataFile.getChannel().position(0);
		movieDataFile.getChannel().position(0);
		userData.mark(0);
		ratingData.mark(0);
		currentLine=ratingData.readLine();
		Sorter sorter = new Sorter();
		Map<String, Integer> movieIdMap=new HashMap<String,Integer>();
		Map<String,Integer> resultMovieMap = new HashMap<String,Integer>();
		Map<String,Integer> movieIntermediatoryMap = new HashMap<String,Integer>();

		String movieId="";
		int rating=0;
		while(currentLine!=null){
			String[] userLineData=currentLine.split("::");
			movieId=userLineData[0];
			if(movieIdMap.containsKey(movieId)){
				movieIdMap.put(movieId,movieIdMap.get(movieId)+1);//("LOR","2")
			}else{
				movieIdMap.put(movieId,1);//("LOR","1")
			}
			currentLine=ratingData.readLine();
		}
		for(Map.Entry<String,Integer> entry:movieIdMap.entrySet()){
			if(entry.getValue()>=40){
				morethan40View.add(entry.getKey());
			}
		}

		return morethan40View;
	}

	private static Map<String, Integer> mapMovieWithRating(
			BufferedReader ratingData, BufferedReader movieData,
			BufferedReader userData, Map<String, Object> fileMap) throws IOException {
		// TODO Auto-generated method stub
		Sorter sorter=new Sorter();
		Map<String,Integer> resultMovieMap = new HashMap<String,Integer>();
		Map<String,Integer> movieIntermediatoryMap = new HashMap<String,Integer>();
		FileInputStream ratingDataFile=(FileInputStream) fileMap.get("ratingDataFile");
		Map<String, Integer> movieIdMap=new HashMap<String,Integer>();
		ratingDataFile.getChannel().position(0);
		currentLine=ratingData.readLine();
		while((currentLine)!=null){
			String[] userLineData=currentLine.split("::");
			String userId=userLineData[0];
			String ratingMovieId=userLineData[1];
			int rating=Integer.parseInt(userLineData[2]);
			if(morethan40View.contains(userId)){
				if(movieIdMap.containsKey(ratingMovieId)){
					movieIdMap.put(ratingMovieId,movieIdMap.get(ratingMovieId)+rating);//("LOR","2")
				}else{
					movieIdMap.put(ratingMovieId,rating);//("LOR","1")
				}
			}
			currentLine=ratingData.readLine();
		}
		movieIdMap=sorter.sortByComparator(movieIdMap, DESC);

		return movieIdMap;
	}




	public void findTop10Critics(BufferedReader ratingData,
			BufferedReader movieData, BufferedReader userData,
			Map<String, Object> fileMap) throws IOException {
		// TODO Auto-generated method stub
		Map<String, Integer> movieIdMap=new HashMap<String,Integer>();
		Map<String,Integer> resultMovieMap = new HashMap<String,Integer>();
		morethan40View=findMoreThan40ViewsMovies(ratingData,movieData,userData,fileMap);
		movieIdMap=mapMovieWithRating(ratingData,movieData,userData,fileMap);

		if(fileMap.containsKey("MultiThreading")){
			writeTopTenCritics(resultMovieMap,fileMap);			
		}else{
			printTopTenCritics(movieIdMap,resultMovieMap);

		}
	}

	private void writeTopTenCritics(Map<String, Integer> resultMovieMap,
			Map<String, Object> fileMap) throws IOException {
		// TODO Auto-generated method stub
		int i=1;
		Sorter sorter = new Sorter();
		for(Map.Entry<String,Integer> entry:resultMovieMap.entrySet()){
			resultMovieMap.put(entry.getKey(), entry.getValue());
			i++;
			if(i>10){
				break;
			}
		}
		resultMovieMap =sorter.sortByComparator(resultMovieMap, DESC);
		FileWriter fstream; 
		BufferedWriter out; 
		File f = new File("C:\\FragmaDataOutput\\Top10Critics.txt");
		if(!f.exists()) { 
			f.createNewFile();
		}
		fstream = new FileWriter("C:\\FragmaDataOutput\\Top10Critics.txt"); 
		out = new BufferedWriter(fstream); 

		Iterator<Entry<String, Integer>> it = resultMovieMap.entrySet().iterator(); 
		out.write("Top 10 Critics are:");
		out.write(" ");
		out.write("User ID");
		out.write("----------------");
		while (it.hasNext() ) { 
			Entry<String, Integer> pairs = it.next(); 
			out.write(pairs.getKey() + "\n");
			System.out.println("User Id "+pairs.getKey());
		} 
		out.close(); 



	}

	private void printTopTenCritics(Map<String, Integer> movieIdMap, Map<String, Integer> resultMovieMap) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		int i=1;
		Sorter sorter = new Sorter();
		for(Map.Entry<String,Integer> entry:movieIdMap.entrySet()){
			resultMovieMap.put(entry.getKey(), entry.getValue());
			i++;
			if(i>10){
				break;
			}
		}
		resultMovieMap =sorter.sortByComparator(resultMovieMap, DESC);
		System.out.println("Top 10 Critics UserID");
		System.out.println("-----------------");
		for(Map.Entry<String,Integer> entry:resultMovieMap.entrySet()){
			System.out.println(entry.getKey());
		}	

	}		
}

