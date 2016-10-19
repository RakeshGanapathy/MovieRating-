package fragma;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class Fragm extends Thread{

	/**
	 * @param args
	 */

	private static String currentLine="";
	static Map<Integer,String> movieMap = new HashMap<Integer,String>();
	private static FileInputStream userDataFile=null;
	private static FileInputStream ratingDataFile=null;
	private static FileInputStream movieDataFile=null;
	private static BufferedReader userData =null;
	private static BufferedReader ratingData=null;
	private static BufferedReader ratingData1=null;
	private static BufferedReader movieData =null;
	public static boolean ASC = true;
	public static boolean DESC = false;


	private static void mapMovieIdMovieName(BufferedReader userData,
			BufferedReader movieData) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		userDataFile.getChannel().position(0);
		ratingDataFile.getChannel().position(0);
		movieDataFile.getChannel().position(0);

		String movieName="";
		String movieIdList="";
		currentLine=movieData.readLine();
		while(currentLine!=null){
			String[] movieLineData=currentLine.split("::");
			movieIdList=movieLineData[0];
			movieName=movieLineData[1];
			movieMap.put(Integer.parseInt(movieIdList), movieName);
			currentLine=movieData.readLine();
		}
	}



	public static void main(String[] args) throws Throwable{
		// TODO Auto-generated method stub
		String directory = "C:\\FragmaDataOutput\\";
		try{
			File directoryFile = new File(directory);
			if(directoryFile.exists()){
				directoryFile.mkdir();
			}
			userDataFile=new FileInputStream("C:\\Users\\DELL\\Desktop\\FragmaData\\users.dat");
			ratingDataFile=new FileInputStream("C:\\Users\\DELL\\Desktop\\FragmaData\\ratings.dat");
			movieDataFile=new FileInputStream("C:\\Users\\DELL\\Desktop\\FragmaData\\movies.dat");
			userData = new BufferedReader(new InputStreamReader(userDataFile));
			ratingData= new BufferedReader(new InputStreamReader(ratingDataFile));
			movieData= new BufferedReader(new InputStreamReader(movieDataFile));
		}
		catch(Exception e){
			System.out.println(e.toString());	
		}
		mapMovieIdMovieName(userData,movieData);//Priority 1
		final Map<String,Object> fileMap=new HashMap<String,Object>();
		fileMap.put("userDataFile", userDataFile);
		fileMap.put("ratingDataFile", ratingDataFile);
		fileMap.put("movieDataFile", movieDataFile);
		fileMap.put("movieMap", movieMap);
		final MostViewedData mostViewData = new MostViewedData();
		final TopTwentyRatedMovies top20Movies = new TopTwentyRatedMovies();
		final TopTenCritics top10Critics = new TopTenCritics();
		System.out.println("Loading 20 Most Viewed Movies....");
		mostViewData.mostView(userData, movieData, ratingData, fileMap);
		System.out.println("");
		top20Movies.top20RatedData(ratingData, movieData, userData,fileMap);
		System.out.println("");
		System.out.println("Loading Top Critics.....");
		top10Critics.findTop10Critics(ratingData, movieData, userData,fileMap);

		fileMap.put("MultiThreading", true);

		Thread ratingDataThread = new Thread(){
			public void run(){
				try {
					mostViewData.mostView(userData,movieData,ratingData,fileMap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
			}
		};
		Thread movieDataThread = new Thread(){
			public void run(){
				try {
					top20Movies.top20RatedData(ratingData, movieData, userData,fileMap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
			}
		};
		Thread criticsThread = new Thread(){
			public void run(){
				try {
					top10Critics.findTop10Critics(ratingData, movieData, userData,fileMap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
			}
		};

		ratingDataThread.start();
		movieDataThread.start();
		criticsThread.start();
		ratingDataThread.join();
		movieDataThread.join();
		criticsThread.join();


		System.out.println("Please find the files at C:\\FragmaDataOutput\\ ");
		System.out.println("Solution Completed");

	}

}