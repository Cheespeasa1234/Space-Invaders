import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class AssetManager {
	
	private String root = "";
	
	public AssetManager(String root) {
		this.root = root;
	}
	
	public Image getImage(String reldir) {
		return new ImageIcon(AssetManager.class.getResource(root + reldir)).getImage();
	}
	
	public void setRoot(String reldir) {
		root = reldir;
	}
	
	public String getRoot() {
		return root;
	}
	
}
