package fragma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TopTwentyRatedMovies {

	private static String currentLine="";
	public static boolean ASC = true;
	public static boolean DESC = false;
	public static Map<String, Integer> top20RatedData = new HashMap<String,Integer>();
	public static List morethan40View = new ArrayList();
	public static Map<String,String> userIdAgeMap = new HashMap<String,String>();
	public static List youngUsers = new ArrayList();
	public static List youngAdultUsers = new ArrayList();
	public static List adultUsers = new ArrayList();
	public static List moviesWatchedByYoungUsers = new ArrayList();
	public static List moviesWatchedByYoungAdultUsers = new ArrayList();
	public static List moviesWatchedByAdultUsers = new ArrayList();
	public static List youngUsersMovieList = new ArrayList();
	public static List youngAdultUsersMovieList = new ArrayList();
	public static List adultUsersMovieList  = new ArrayList();

	public static List top20Movies = new ArrayList();


	public static void mapUserIdAge(BufferedReader userData,
			BufferedReader movieData, BufferedReader ratingData, Map<String, Object> fileMap) throws NumberFormatException, IOException {
		FileInputStream userDataFile=(FileInputStream) fileMap.get("userDataFile");
		FileInputStream movieDataFile=(FileInputStream) fileMap.get("movieDataFile");
		FileInputStream ratingDataFile=(FileInputStream) fileMap.get("ratingDataFile");
		userDataFile.getChannel().position(0);
		ratingDataFile.getChannel().position(0);
		movieDataFile.getChannel().position(0);

		int ageInteger=0;
		currentLine=userData.readLine();
		while(currentLine!=null){
			String[] movieLineData=currentLine.split("::");
			String userIdList = movieLineData[0];
			String age = movieLineData[2];
			ageInteger=Integer.parseInt(age);
			userIdAgeMap.put(userIdList, age);
			currentLine=userData.readLine();
			if(ageInteger<20){
				if(!youngUsers.contains(userIdList)){
					youngUsers.add(userIdList);
				}

			}else if(ageInteger>20 && ageInteger<40){
				if(!youngAdultUsers.contains(userIdList)){
					youngAdultUsers.add(userIdList);
				}

			}else if(ageInteger>40){
				if(!adultUsers.contains(userIdList)){
					adultUsers.add(userIdList);
				}

			}
		}
	}


	public static void findUserGroupMovies(BufferedReader userData, BufferedReader movieData, BufferedReader ratingData, Map<String, Object> fileMap) throws IOException{
		FileInputStream userDataFile=(FileInputStream) fileMap.get("userDataFile");
		FileInputStream movieDataFile=(FileInputStream) fileMap.get("movieDataFile");
		FileInputStream ratingDataFile=(FileInputStream) fileMap.get("ratingDataFile");
		userDataFile.getChannel().position(0);
		ratingDataFile.getChannel().position(0);
		movieDataFile.getChannel().position(0);

		currentLine=ratingData.readLine();
		while(currentLine!=null){
			String[] ratingLineData=currentLine.split("::");
			String userIdList = ratingLineData[0];
			String currentMovieIdList = ratingLineData[1];
			//			userIdAgeMap.put(userIdList, currentMovieIdList);
			currentLine=ratingData.readLine();
			int userAge=Integer.parseInt(userIdAgeMap.get(userIdList));
			if(userAge<20 && morethan40View.contains(userIdList)){
				if(youngUsers.contains(userIdList)){
					if(!youngUsersMovieList.contains(userIdList)){
						youngUsersMovieList.add(userIdList);
					}
				}
			}else if(userAge>20 && userAge<40  && morethan40View.contains(userIdList)){
				if(youngAdultUsers.contains(userIdList)){
					if(!youngAdultUsersMovieList.contains(userIdList)){
						youngAdultUsersMovieList.add(userIdList);
					}
				}
			}else if(userAge>40  && morethan40View.contains(userIdList)){
				if(adultUsers.contains(userIdList)){
					if(!adultUsersMovieList.contains(userIdList)){
						adultUsersMovieList.add(userIdList);
					}
				}
			}
		}	
	}

	public static void top20RatedData(BufferedReader ratingData,
			BufferedReader movieData, BufferedReader userData, Map<String, Object> fileMap) throws IOException {
		// TODO Auto-generated method stub
		Map<String,Integer> top20RatedMovieMap = new HashMap<String,Integer>();
		Map<String, Integer> movieIdMap=new HashMap<String,Integer>();
		morethan40View=findMoreThan40ViewsMovies(ratingData,movieData,userData,fileMap);
		movieIdMap=mapMovieWithRating(ratingData,movieData,userData,fileMap);
		top20RatedMovieMap=printTop20RatedMovies(ratingData,movieData,userData,fileMap,movieIdMap);
		System.out.println("------------------------------------");
		System.out.println(" ");
		System.out.println("Grouping Top 20 Movies With Age Group....");
		mapUserIdAge(userData, movieData,ratingData,fileMap);
		findUserGroupMovies(userData, movieData,ratingData,fileMap);
		Map<String,Integer> resultMovieMap = new HashMap<String,Integer>();
		int i=0;
		for(Map.Entry<String,Integer> entry:movieIdMap.entrySet()){
			resultMovieMap.put(entry.getKey(), entry.getValue());
			i++;
			top20Movies.add(entry.getKey());
			if(i>20){
				break;
			}
		}
		ArrayList<List> top20RatedMoviesMapAgeSegregated = new ArrayList<List>();
		top20RatedMoviesMapAgeSegregated=top20RatedMoviesSegregatedWithAge(top20RatedMovieMap,fileMap);
		System.out.println("");
		if(fileMap.containsKey("MultiThreading")){
			writeTopMoviesWithAgeGroup(top20RatedMoviesMapAgeSegregated);
		}else{
			printTopMoviesWithAgeGroup(top20RatedMoviesMapAgeSegregated);
		}
	}



	private static void writeTopMoviesWithAgeGroup(
			ArrayList<List> top20RatedMoviesMapAgeSegregated) throws IOException {
		// TODO Auto-generated method stub
		List youngUserMovieList = top20RatedMoviesMapAgeSegregated.get(0);
		List youngAdultUserMovieList = top20RatedMoviesMapAgeSegregated.get(1);
		List adultUserMovieList = top20RatedMoviesMapAgeSegregated.get(2);
		File f1 = new File("C:\\FragmaDataOutput\\TopRatedMoviesWatchedByYoung.txt");
		if(!f1.exists()) { 
			f1.createNewFile();
		}
		File f2 = new File("C:\\FragmaDataOutput\\TopRatedMoviesWatchedByYoungAdult.txt");
		if(!f2.exists()) { 
			f2.createNewFile();
		}
		File f3 = new File("C:\\FragmaDataOutput\\TopRatedMoviesWatchedByAdult.txt");
		if(!f3.exists()) { 
			f3.createNewFile();
		}
		File  youngUserMovieListWriter = new File ("C:\\FragmaDataOutput\\TopRatedMoviesWatchedByYoung.txt"); 
		File  youngAdultUserMovieListWriter = new File ("C:\\FragmaDataOutput\\TopRatedMoviesWatchedByYoungAdult.txt"); 
		File  adultUserMovieListWriter = new File ("C:\\FragmaDataOutput\\TopRatedMoviesWatchedByAdult.txt"); 
		FileWriter youngWriter = new FileWriter(youngUserMovieListWriter.getAbsoluteFile());
		BufferedWriter youngBuffer = new BufferedWriter(youngWriter);
		Iterator lisIterator=youngUserMovieList.iterator();
		while(lisIterator.hasNext()){
			youngBuffer.write((String) lisIterator.next());
		}
		youngBuffer.close();	
		FileWriter youngAdultWriter = new FileWriter(youngAdultUserMovieListWriter.getAbsoluteFile());
		BufferedWriter youngAdultBuffer = new BufferedWriter(youngAdultWriter);
		Iterator lisYouAdultIterator=youngUserMovieList.iterator();
		while(lisIterator.hasNext()){
			youngAdultBuffer.write((String) lisYouAdultIterator.next());
		}
		youngAdultBuffer.close();
		FileWriter adultWriter = new FileWriter(adultUserMovieListWriter.getAbsoluteFile());
		BufferedWriter adultBuffer = new BufferedWriter(youngAdultWriter);
		Iterator lisAdultIterator=adultUserMovieList.iterator();
		while(lisIterator.hasNext()){
			youngAdultBuffer.write((String) lisAdultIterator.next());
		}
		youngAdultBuffer.close();
	}


	private static void printTopMoviesWithAgeGroup(
			ArrayList<List> top20RatedMoviesMapAgeSegregated) {
		// TODO Auto-generated method stub
		System.out.println("Movies by Young Users");
		printArrayList(top20RatedMoviesMapAgeSegregated.get(0));
		System.out.println("");
		System.out.println("Movies by Young Adult Users");
		printArrayList(top20RatedMoviesMapAgeSegregated.get(1));
		System.out.println("");
		System.out.println("Movies by Adult Users");
		printArrayList(top20RatedMoviesMapAgeSegregated.get(2));

	}


	private static ArrayList<List> top20RatedMoviesSegregatedWithAge(
			Map<String, Integer> top20RatedMovieMap, Map<String, Object> fileMap) {
		// TODO Auto-generated method stub
		System.out.println("Printing Top 20 Movies Segregated With Age....");
		ArrayList<List> returnList = new ArrayList<List>();
		Map<Integer, String> movieMap = (Map<Integer, String>) fileMap.get("movieMap");
		for(Map.Entry<String,Integer> entry:top20RatedMovieMap.entrySet()){
			String movieIdString=entry.getKey();
			Integer interMediateMovieId=Integer.parseInt(movieIdString);
			if(top20Movies.contains(movieIdString)){
				if(!Collections.disjoint(morethan40View,youngUsersMovieList)){
					moviesWatchedByYoungUsers.add(movieMap.get(interMediateMovieId));
				}else if(!Collections.disjoint(morethan40View,youngAdultUsersMovieList)){
					moviesWatchedByYoungAdultUsers.add(movieMap.get(interMediateMovieId));
				}else if(!Collections.disjoint(morethan40View,adultUsersMovieList)){
					moviesWatchedByAdultUsers.add(movieMap.get(interMediateMovieId));
				}
			}
		}
		if(moviesWatchedByYoungUsers.size()>1){
			moviesWatchedByYoungUsers.add("No Data");
		}
		if(moviesWatchedByYoungAdultUsers.size()>1){
			moviesWatchedByYoungAdultUsers.add("No Data");
		}
		if(moviesWatchedByAdultUsers.size()>1){
			moviesWatchedByAdultUsers.add("No Data");
		}
		returnList.add(moviesWatchedByYoungUsers);
		returnList.add(moviesWatchedByYoungAdultUsers);
		returnList.add(moviesWatchedByAdultUsers);
		return returnList;
	}


	private static void printArrayList(List moviesWatchedByGroup) {
		// TODO Auto-generated method stub
		System.out.println("Movie Name:");
		System.out.println("--------------------------");
		Iterator lisIterator=moviesWatchedByGroup.iterator();
		while(lisIterator.hasNext()){
			System.out.println((String)lisIterator.next());
		}
	}


	private static Map<String, Integer> printTop20RatedMovies(
			BufferedReader ratingData, BufferedReader movieData,
			BufferedReader userData, Map<String, Object> fileMap, Map<String, Integer> movieIdMap) {
		// TODO Auto-generated method stub
		int i=0;
		Map<Integer, String> movieMap = (Map<Integer, String>) fileMap.get("movieMap");
		Map<String,Integer> resultMovieMap = new HashMap<String,Integer>();
		Sorter sorter=new Sorter();
		for(Map.Entry<String,Integer> entry:movieIdMap.entrySet()){
			resultMovieMap.put(entry.getKey(), entry.getValue());
			i++;
			if(i>20){
				break;
			}
		}
		resultMovieMap =sorter.sortByComparator(resultMovieMap, DESC);
		System.out.println("Top 20 Most Rated Movies are...");
		System.out.println("Movie Name");
		System.out.println("------------------------");
		top20RatedData=resultMovieMap;
		for(Map.Entry<String,Integer> entry:resultMovieMap.entrySet()){
			String movieIdString=entry.getKey();
			Integer interMediateMovieId=Integer.parseInt(movieIdString);
			System.out.println(movieMap.get(interMediateMovieId));
		}
		return resultMovieMap;
	}


	private static Map<String, Integer> mapMovieWithRating(
			BufferedReader ratingData, BufferedReader movieData,
			BufferedReader userData, Map<String, Object> fileMap) throws IOException {
		// TODO Auto-generated method stub
		Sorter sorter=new Sorter();
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


	private static List findMoreThan40ViewsMovies(BufferedReader ratingData, BufferedReader movieData, BufferedReader userData, Map<String, Object> fileMap) throws IOException {
		// TODO Auto-generated method stub

		FileInputStream userDataFile=(FileInputStream) fileMap.get("userDataFile");
		FileInputStream movieDataFile=(FileInputStream) fileMap.get("movieDataFile");
		FileInputStream ratingDataFile=(FileInputStream) fileMap.get("ratingDataFile");
		userDataFile.getChannel().position(0);
		ratingDataFile.getChannel().position(0);
		movieDataFile.getChannel().position(0);
		userData.mark(0);
		ratingData.mark(0);
		currentLine=ratingData.readLine();
		Sorter sorter = new Sorter();
		Map<String, Integer> movieIdMap=new HashMap<String,Integer>();
		String movieId="";
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


}
