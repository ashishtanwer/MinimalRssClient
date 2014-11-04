package com.acision.minimalrssclient;
import com.acision.minimalrssclient.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URL;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import java.io.InputStream;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class RssClient extends Activity {

    String rssResult = "Waiting for Data....";
    String newval="";
    String rssResult1=""; 
    boolean item = false;
    SAXParserFactory factory ;
    SAXParser saxParser ;
    XMLReader xmlReader ;
    RssHandler rssHandler  ;
    InputStream inputStream;
    //TextView rss;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.rssclient);
        
        TextView rss = (TextView) findViewById(R.id.rss);
        
        new RetrieveFeedTask().execute();
        
        
        //rss.setText(rssResult);
        //rss.setText(newval);
	}
	
	
    private class RssHandler extends DefaultHandler {

        public void startElement(String uri, String localName, String qName,
                Attributes attrs) throws SAXException {
            if (localName.equals("item"))
                item = true;

            if (!localName.equals("item") && item == true)
            {
                rssResult = rssResult + localName + ": ";
	            //Log.e("RSS", rssResult);
            }

        }

        public void endElement(String namespaceURI, String localName,
                String qName) throws SAXException {

        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String cdata = new String(ch, start, length);
            if (item == true)
            {
                rssResult = rssResult +(cdata.trim()).replaceAll("\\s+", " ")+"\t";
	            //Log.e("RSS", rssResult);
            }
            
        }
        
        public String getFeed()
        {
                return rssResult;
        }

    }
 
	class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
	    private Exception exception;
	
	    protected String doInBackground(Void... params) {		
	        try {
	        	URL url= new URL("http://ashishtanwer.wordpress.com/rss.xml");
	            SAXParserFactory factory =SAXParserFactory.newInstance();
	            SAXParser parser=factory.newSAXParser();
	            XMLReader xmlreader=parser.getXMLReader();
	            RssHandler theRSSHandler=new RssHandler();
	            xmlreader.setContentHandler(theRSSHandler);
	            InputSource is=new InputSource(url.openStream());
	            xmlreader.parse(is);
	            newval= theRSSHandler.getFeed();
	            
	            //Log.e("RSS", newval);
	        } catch (Exception e) {
	            this.exception = e;
	        }
			return newval;
	    }
	    
		@Override
		protected void onPostExecute(String newval) {
			try{
				Log.e("PostRSS", newval.substring(0, 100));
				TextView rss = (TextView) findViewById(R.id.rss);
				rss.setText(newval);
        } catch (Exception e) {
            this.exception = e;
        }
			
		}
	}
}
