package com.example.xmljsonparse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.xmljsonparse.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpListener();
    }

    private void setUpListener() {

        binding.parseXmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseXml();
            }
        });

        binding.parseJsonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseJson();
            }
        });

    }

    private void parseXml() {
        try {
            InputStream inputStream = getAssets().open("city.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // Parse the XML document from the input stream and obtain a Document object
            Document document = documentBuilder.parse(inputStream);
            // Get the root element of the XML document
            Element element = document.getDocumentElement();
            // Normalizes the document for consistent processing.
            element.normalize();

            NodeList nodeList = document.getElementsByTagName("place");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    // Cast the node to an Element
                    Element place = (Element) node;
                    String cityName = place.getElementsByTagName("City_Name").item(0).getTextContent();
                    String latitude = place.getElementsByTagName("Latitude").item(0).getTextContent();
                    String longitude = place.getElementsByTagName("Longitude").item(0).getTextContent();
                    String temperature = place.getElementsByTagName("Temperature").item(0).getTextContent();
                    String humidity = place.getElementsByTagName("Humidity").item(0).getTextContent();

                    // Set data to TextViews
                    binding.xmlCityName.setText(cityName);
                    binding.xmlLatitude.setText(latitude);
                    binding.xmlLongitude.setText(longitude);
                    binding.xmlTemperature.setText(temperature);
                    binding.xmlHumidity.setText(humidity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseJson() {
        try {
            InputStream inputStream = getAssets().open("cityA.json");
            // Determine the size of the JSON file
            int size = inputStream.available();
            // Create a byte buffer to read the JSON file content into
            byte[] buffer = new byte[size];
            // Read the content of the JSON file into the buffer
            inputStream.read(buffer);
            // Close the input stream to release resources
            inputStream.close();
            String jsonString = new String(buffer);

            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0); // Assuming there is only one object in the array

            String cityName = jsonObject.getString("City_Name");
            String latitude = jsonObject.getString("Latitude");
            String longitude = jsonObject.getString("Longitude");
            String temperature = jsonObject.getString("Temperature");
            String humidity = jsonObject.getString("Humidity");

            // Set data to TextViews
            binding.jsonCityName.setText(cityName);
            binding.jsonLatitude.setText(latitude);
            binding.jsonLongitude.setText(longitude);
            binding.jsonTemperature.setText(temperature);
            binding.jsonHumidity.setText(humidity);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


}