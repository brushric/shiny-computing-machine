package edu.msu.brushric.theflockinggame;

import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Gamer on 3/18/2015.
 */
public class Cloud {
    private static final String MAGIC = "NechAtHa6RuzeR8x";
    private static final String LOGIN_URL = "http://webdev.cse.msu.edu/~sarteleb/cse476/toucan/toucan-login.php";
    private static final String NEW_USER_URL = "http://webdev.cse.msu.edu/~sarteleb/cse476/toucan/toucan-new-user.php";
    private static final String UTF8 = "UTF-8";

    /**
     * Skip the XML parser to the end tag for whatever
     * tag we are currently within.
     * @param xml the parser
     * @throws java.io.IOException
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public static void skipToEndTag(XmlPullParser xml)
            throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG &&
                tag != XmlPullParser.END_DOCUMENT);
    }

    /**
     * Login as a user in the cloud.
     * @return reference to an input stream or null if this fails
     */
    public boolean loginUser(String user, String password) {
        // Create a get query
        String query = LOGIN_URL + "?user=" + user + "&magic=" + MAGIC + "&password=" + password;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            InputStream stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "toucan");

                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            stream.close();
            return true;

        } catch (MalformedURLException e) {
            // Should never happen
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Login as a user in the cloud.
     * @return reference to an input stream or null if this fails
     */
    public boolean addNewUser(String user, String password) {
        // Create a get query
        String query = NEW_USER_URL + "?user=" + user + "&magic=" + MAGIC + "&password=" + password;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            InputStream stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "toucan");

                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            stream.close();
            return loginUser(user, password);

        } catch (MalformedURLException e) {
            // Should never happen
            return false;
        } catch (IOException ex) {
            return false;
        }


    }
}
