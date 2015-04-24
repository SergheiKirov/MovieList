package com.kirov.movielist;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;





public class MainActivity extends ActionBarActivity {
	public ListView listfilm;
	public EditText search;
	public TextView info;
	public JSONObject jsonobject;
	 public ArrayAdapter<?> arrayAdapter;
	  public WebView pict;
	  public String[] title;
	public	String [] year;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	listfilm=  (ListView) findViewById(R.id.listFilm);	
	listfilm.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		 public void onItemClick(AdapterView<?> arg0, View v,
                   int index, long arg3) {

			 showotherMovie(index);
           	
           }
}); 
	
	
	
	pict=(WebView) findViewById(R.id.webView1);
	
	info=(TextView) findViewById(R.id.INFO);
	//info.getBackground().setAlpha(200);
	
	search=  (EditText) findViewById(R.id.Search);
	search.addTextChangedListener(new TextWatcher() {
	    @Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if(isNetworkConnected())
	    		try {
	    			OMBDrequest();
	    		} catch (UnsupportedEncodingException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	 });
   
	
	}	

	
//Function for Searce via JSON movies
	
public void OMBDrequest() throws UnsupportedEncodingException
{
	  
	       String selectedItem = search.getText().toString().replace("\\s+", "+");
	      
	        String url;
		//make JSON request
			url = "http://www.omdbapi.com/?s=" + URLEncoder.encode(selectedItem, "UTF-8");
			 if(selectedItem!=""){
	        jsonobject = JSONfunctions .getJSONfromURL(url);
	     
	        JSONArray temp=null;
			String[] f= null;
			JSONObject[] JJ=null;
					try {
						if(jsonobject!=null)
						temp=jsonobject.getJSONArray("Search"); //looking for JSONobjects
						else
							temp=null;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        
					if(temp!=null)			
					{	f=new String [temp.length()];
						JJ=new JSONObject [temp.length()];
						for(int i=0; i<f.length;i++)
							try {
								JJ[i]=temp.getJSONObject(i);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						 title=new String [JJ.length];
						 year=  new String [JJ.length];
						
						
	     for(int i=0; i<JJ.length; i++)
	     {			try {
				title[i] = JJ[i].getString("Title");
				
				 year[i] = JJ[i].getString("Year");
			
	      
	        
	        
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    }
	    
	    
	   	
					Toast.makeText(this, "I have some MOVIE for you",
					   		  Toast.LENGTH_LONG).show();
					
	        showresultsofsearch(title);
				getinfoMovie(title[0], year[0]);	
					
					}}
}

// Get info for MOVIE from list

public void getinfoMovie (String title, String Year)
	 
{
     String url;
	//make JSON request
		
			url = "http://www.omdbapi.com/?t=" + title+"&y"+Year+"&plot=full&r=json";
		url=url.replace(" ", "+");
		JSONObject JJ = JSONfunctions .getJSONfromURL(url);
	if(JJ!=null)
	{
	  String released;
	try {
		released = JJ.getString("Released");
	
     String runtime = JJ.getString("Runtime");
     String genre = JJ.getString("Genre");
     String actors =JJ.getString("Actors");
     String plot = JJ.getString("Plot");
     String imdbRating = JJ.getString("imdbRating");
     String poster = JJ.getString("Poster");
     
	showinfoMovie(poster, released,  runtime,  genre,  actors,  plot);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}


}

//Check if network is working
private boolean isNetworkConnected() {
	  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	  NetworkInfo ni = cm.getActiveNetworkInfo();
	  if (ni == null) {
	   // There are no active networks.
	   return false;
	  } else
	  {
	   
		  
		  return true;
	   }
	 }
// Controller to sho list on a screen
public void showresultsofsearch(String[] input)
{
 arrayAdapter = new ArrayAdapter<Object>( this, android.R.layout.simple_list_item_1, input);
     
     listfilm.setAdapter(arrayAdapter);




}
// show poster and info on screen
public void showinfoMovie(String poster, String released, String runtime, String genre, String actors, String Plot)
{

pict.loadUrl(poster);
info.setText( "Date of release: "+released+"\nRun time: "+ runtime+"\nGenre: "+genre+"\nActors: "+actors);
		 


}
public void showotherMovie(int index)
{
	getinfoMovie(title[index], year[index]);

}
}