package com.search;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.FileReader;

public class repImage {

	public static void main(String [] args) throws IOException{
		String systemPath = System.getProperty("user.dir");

        BufferedReader br = null;
        String line = "";
        boolean getOgImage = true;
        boolean header = true;

        br = new BufferedReader(new FileReader(systemPath+"/data/metadata/IRMovieList.csv"));
        while ((line = br.readLine()) != null) {
        	String[] movie = line.split(",");
        
        	String url = movie[1]; // url of webpage (use [2]?)
        	String name = movie[0];
        	name = name.replaceAll("[^\\w\\s]", ""); 
        	System.out.println(name);
        	// IMPORTANT: replacing all symbols that are not letters or white space
        	Document doc = Jsoup.parse(new URL(url),100000); 
        	Elements metaOgImage = doc.select("meta[property=og:image]");
        	BufferedImage repImage = null; 
        	String repImageUrl = "none";

        	if ((getOgImage) && (metaOgImage != null)) { //selecting image provided by the site (method 1)
        		repImageUrl = metaOgImage.attr("content");
        		URL img_url = new URL(repImageUrl);
        		repImage = ImageIO.read(img_url);
        		System.out.println("Representative image url: " + repImageUrl);
        	}
        	else { // else getting the biggest picture (method 2)

        		int max = 0;
        		Elements images = doc.select("img[src]");
        		int length = images.size();

        		for (int i = 0; i < length; i++) {

        			String absUrl = images.get(i).absUrl("src");

        			if (!(absUrl.isEmpty())) {
        				URL img_url = new URL(absUrl);
        				try {
        					BufferedImage bi = ImageIO.read(img_url);
        					int h = bi.getHeight();
        					int w = bi.getWidth();

        					if ((h * w) > max) { // checking if the picture if bigger than the biggest one found so far
        						max = h * w;
        						repImageUrl = images.get(i).absUrl("src");
        						repImage = bi;
        					}

        				}
        				catch(java.lang.NullPointerException e) {
        					continue;
        				}
        			}
        		}
        		System.out.println(name);
        		System.out.println("Representative image url: " + repImageUrl);
        	}
        	
        	if (repImage == null) {
        		System.out.println("Can't retrieve the image");
        	}

        	// finding new height and width for the thumbnail
        	// the maximal value should be 100
        	int new_width = repImage.getWidth();
        	int new_height = repImage.getHeight();
        	if (repImage.getWidth() > 100) {
        		new_width = 100;
        		new_height = (new_width * repImage.getHeight()) / repImage.getWidth();
        	}
        	if (new_height > 100) {
        		new_height = 100;
        		new_width = (new_height * repImage.getWidth()) / repImage.getHeight();
        	}
        	Image resizedImage = repImage.getScaledInstance(new_width, new_height, Image.SCALE_DEFAULT); // resizing the image

        	File outputfile = new File(systemPath+ "/src/main/webapp/img/thumbnails/" + (name.replaceAll("\\s","")).toLowerCase() + ".jpg");
        	BufferedImage buffImg = toBufferedImage(resizedImage);
        	ImageIO.write(buffImg, "jpg", outputfile); // saving the image to file
        }
        br.close();
	}

	private static BufferedImage toBufferedImage(Image src) { // converting image to buffered image in order to write it to a file
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }
}
