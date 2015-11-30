package resources;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import javax.sound.sampled.*;

public class ResourceManager {
	
	private static HashMap<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();
	private static HashMap<String, SpriteSheet> spriteSheetMap = new HashMap<String, SpriteSheet>();
	private static HashMap<String, Font> fontMap = new HashMap<String, Font>();
	//private static HashMap<String, AudioStream> soundMap = new HashMap<String, AudioStream>();

	public static void clearResources(){

		imageMap.clear();
		//soundMap.clear();
		spriteSheetMap.clear();
		fontMap.clear();

	}

	public static void addSound(String soundName){

	}

	public static void playSound(String soundName){

		try {

   			File file;
    		AudioInputStream stream;
    		AudioFormat format;
   			DataLine.Info info;
    		Clip clip;

    		file = new File("content/sound/" + soundName);
    		stream = AudioSystem.getAudioInputStream(file);
    		format = stream.getFormat();
    		info = new DataLine.Info(Clip.class, format);
    		clip = (Clip) AudioSystem.getLine(info);
    		clip.open(stream);
    		clip.start();

		}
		catch (Exception e) {
		    
			e.printStackTrace();

		}

	}

	public static BufferedImage getImage(String imageName){

		if (imageMap.get(imageName) == null){

			addImage(imageName);

		}

		return imageMap.get(imageName);

	}

	/*public static Font getFont(String fontName){

		if (fontMap.get(fontName) == null){

			addFont(fontName);

		}

		return fontMap.get(fontName);

	}*/

	public static SpriteSheet getSpriteSheet(String sheetName){

		return spriteSheetMap.get(sheetName);

	}

	public static void createSpriteSheet(String sheetName, int frameWidth, int frameHeight){

		spriteSheetMap.put(sheetName, new SpriteSheet(sheetName, frameWidth, frameHeight));

	}

	public static void addFont(String fontName){

		try {
			
			Font f = Font.createFont(Font.TRUETYPE_FONT, new File("content/fonts/" + fontName + ".ttf"));

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(f);

		} catch (Exception e) {e.printStackTrace();}
		//fontMap.put(fontName, new SpriteFont(getSpriteSheet(fontName)));

	}

	private static void addImage(String imageName){

		try {

			BufferedImage newImage = ImageIO.read(new File("content/" + imageName));
			imageMap.put(imageName, newImage);

		} catch (Exception e){

			System.out.println("ResourceManager could not load image <" + imageName + "> please make sure this file is a .png");

		}
	}
}