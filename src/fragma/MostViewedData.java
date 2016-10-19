package fragma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MostViewedData {

	private static String currentLine="";

	public static boolean ASC = true;
	public static boolean DESC = false;

	public static String mostView(BufferedReader userData, BufferedReader movieData, BufferedReader ratingData, Map<String, Object> fileMap) throws IOException{
		FileInputStream userDataFile=(FileInputStream) fileMap.get("userDataFile");
		FileInputStream movieDataFile=(FileInputStream) fileMap.get("movieDataFile");
		FileInputStream ratingDataFile=(FileInputStream) fileMap.get("ratingDataFile");
		userDataFile.getChannel().position(0);
		ratingDataFile.getChannel().position(0);
		movieDataFile.getChannel().position(0);
		System.out.println("Loading Most Viewed Movies.....");
		currentLine=ratingData.readLine();
		Map<String,Integer> movieIdMap=new HashMap<String,Integer>();
		Map<String,Integer> resultMovieMap = new HashMap<String,Integer>();
		movieIdMap=matchMovieWithTimesWatched(movieIdMap,ratingData);
		resultMovieMap=topTenMostWatchMovies(movieIdMap);
		System.out.println("Top Ten Most Viewed Movies are");
		if(fileMap.containsKey("MultiThreading")){
			writeTopTenMostViewedMovies(resultMovieMap,fileMap);		
		}else{
			printTopTenMostViewedMovies(resultMovieMap,fileMap);
			
		}

		return null;

	}

	private static void writeTopTenMostViewedMovies(
			Map<String, Integer> resultMovieMap, Map<String, Object> fileMap) throws IOException {
		// TODO Auto-generated method stub
		Map<String,Integer> result=new HashMap<String,Integer>();
		Map<Integer, String> movieMap = (Map<Integer, String>) fileMap.get("movieMap");
		Sorter sorter = new Sorter();
		for(Map.Entry<String,Integer> entry:resultMovieMap.entrySet()){
			String movieIdString=entry.getKey();
			Integer interMediateMovieId=Integer.parseInt(movieIdString);
			result.put(movieMap.get(interMediateMovieId),interMediateMovieId);
		}
		result=sorter.sortKeyByComparator(result, ASC);
		FileWriter fstream; 
		BufferedWriter out; 
		File f1 = new File("C:\\FragmaDataOutput\\Top10MostWatchedMovies.txt");
		if(!f1.exists()) { 
			f1.createNewFile();
		}
		fstream = new FileWriter("C:\\FragmaDataOutput\\Top10MostWatchedMovies.txt"); 
		out = new BufferedWriter(fstream); 

		Iterator<Entry<String, Integer>> it = result.entrySet().iterator(); 
		out.write("Top 10 Most Viewed Movies are:");
		out.write(" ");
		out.write("Movies Name");
		out.write("----------------");
		while (it.hasNext() ) { 
			Entry<String, Integer> pairs = it.next(); 
			out.write(movieMap.get(pairs.getValue()) + "\n"); 
		} 
		out.close(); 
	}

	private static void printTopTenMostViewedMovies(
			Map<String, Integer> resultMovieMap, Map<String, Object> fileMap) {
		// TODO Auto-generated method stub
		Map<String,Integer> result=new HashMap<String,Integer>();
		Map<Integer, String> movieMap = (Map<Integer, String>) fileMap.get("movieMap");
		System.out.println("");
		System.out.println("Movie Name");
		System.out.println("----------");
		Sorter sorter = new Sorter();
		for(Map.Entry<String,Integer> entry:resultMovieMap.entrySet()){
			String movieIdString=entry.getKey();
			Integer interMediateMovieId=Integer.parseInt(movieIdString);
			result.put(movieMap.get(interMediateMovieId),interMediateMovieId);
		}
		result=sorter.sortKeyByComparator(result, ASC);
		for(Map.Entry<String,Integer> entry:result.entrySet()){
			Integer movieIdInt=entry.getValue();
			System.out.println(movieMap.get(movieIdInt));
		}



	}

	private static Map<String, Integer> topTenMostWatchMovies(
			Map<String, Integer> movieIdMap) {
		// TODO Auto-generated method stub
		Map<String,Integer> resultMovieMap = new HashMap<String,Integer>();
		int i=1;
		Sorter sorter=new Sorter();
		for(Map.Entry<String,Integer> entry:movieIdMap.entrySet()){
			resultMovieMap.put(entry.getKey(), entry.getValue());
			i++;
			if(i>10){
				break;
			}
		}
		resultMovieMap=sorter.sortKeyByComparator(resultMovieMap, ASC);
		return resultMovieMap;
	}

	private static Map<String, Integer> matchMovieWithTimesWatched(Map<String, Integer> movieIdMap, BufferedReader ratingData) throws IOException {
		// TODO Auto-generated method stub
		Sorter sorter = new Sorter();
		while(currentLine!=null){
			String[] userLineData=currentLine.split("::");
			String movieId=userLineData[1];
			if(movieIdMap.containsKey(movieId)){
				movieIdMap.put(movieId,movieIdMap.get(movieId)+1);//("LOR","2")
			}else{
				movieIdMap.put(movieId,1);//("LOR","1")
			}
			currentLine=ratingData.readLine();
		}
		movieIdMap=sorter.sortByComparator(movieIdMap, DESC);

		return movieIdMap;
	}

}
