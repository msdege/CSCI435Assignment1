package com.mkyong.xml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class parsexml {
	
	public static int iter = 0;
	
	public static ArrayList<Integer> parseBounds(String bounds) {
		
		ArrayList<Integer> coordinates = new ArrayList<Integer>();
		
		String digits = "";
		for (int i=0; i < bounds.length(); i++) {
			
			if (Character.isDigit(bounds.charAt(i))) {	
				digits = digits + bounds.charAt(i);
			}
			else if (bounds.charAt(i) == ',' || bounds.charAt(i) == ']') {
				coordinates.add(Integer.parseInt(digits));
				digits="";
			}
		}
		
		return coordinates;
	}
	
	public static BufferedImage encloseGUIelements(ArrayList<Integer> coords, BufferedImage image, File child) throws IOException {
		Graphics g = image.createGraphics();
		
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setStroke(new BasicStroke(5));
		
		g2.setColor(java.awt.Color.yellow);
		g2.drawRect(coords.get(0), coords.get(1), coords.get(2)-coords.get(0), coords.get(3)-coords.get(1));
		
		return image;
	}
	
	public static void main(String[] args) throws IOException, SAXException, IllegalArgumentException {
		
		File dir = new File(System.getProperty("user.dir") + "\\XML&PNGData");
		File[] directoryListing = dir.listFiles();
		
		if (directoryListing != null) {
			
			for (File child : directoryListing) {
				
				if (child.toString().contains(".xml")) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					ArrayList<ArrayList<Integer>> leafbounds = new ArrayList<ArrayList<Integer>>();
		 		 
			 		try {
			 	          // parse XML file
			 	          DocumentBuilder db = dbf.newDocumentBuilder();
			 	          Document doc = db.parse(child);
			 	          NodeList list = doc.getDocumentElement().getElementsByTagName("node");
	
			 	          for (int temp = 0; temp < list.getLength(); temp++) {
	
			 	              Node node = list.item(temp);
	
			 	              if (node.getNodeType() == Node.ELEMENT_NODE) {
			 	            	  
			 	            	  if (!node.hasChildNodes()) {
			 	            		 
			 	            		  Element element = (Element) node;
			 		                  
			 		                  String bounds = element.getAttribute("bounds");
			 		                  
			 		                  leafbounds.add(parseBounds(bounds));
			 	            	  }
	
			 	              }
			 	          }
			 	          
			 	         BufferedImage image = ImageIO.read(new File(child.toString().substring(0, child.toString().length()-4) + ".png"));
				 		 
				 		 for (int i =0; i < leafbounds.size(); i++) {
				 			 image = encloseGUIelements(leafbounds.get(i), image, child);
				 		 }
				 		 
				 		 ImageIO.write(image, "PNG", new File(System.getProperty("user.dir") + "\\XML&PNGData\\output", "output" + String.valueOf(iter) + ".png"));
				 		 iter+=1;
				 		 
				 		 System.out.println("Finished processing the file " + child.toString());
	
			 	      } catch (Exception e) {
			 	          System.out.println("Error parsing the file " + child.toString());
			 	      }
		
			      }
			  }
		  } 
		
	} 

}
