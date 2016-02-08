package com.androbro.weatherxmlparsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by user on 2/4/2016.
 */
public class WeatherHttpClient {

    public NodeList returnNodes(){
        String testURL = "http://w1.weather.gov/xml/current_obs/KORD.xml";
        try {
            URL url = new URL(testURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(inputStream);

            //getting root element of the XML doc
            Element rootElement = xmlDocument.getDocumentElement();
            MyLogger.m("" + rootElement.getTagName());

            NodeList list = rootElement.getChildNodes();

            return list;
        } catch (Exception e) {
            MyLogger.m(e + "");
        }
        return null;
    }

}
