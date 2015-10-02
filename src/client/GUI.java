package client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import common.Felder;
import common.Spielfeld;



public class GUI extends JPanel{	
	private static final long serialVersionUID = 1L;

	Spielfeld sf;
	Felder field[][];    	
	Graphics g;
	BufferedImage img;
	
	
	
    public GUI(){		
		init();		
	}	
	

	
	public void draw(Spielfeld spfd){
		sf = spfd;
		field = sf.getFelder();
		g = getGraphics();		
		for (int i=0;i<10 ;i++) {
			for(int j=0; j<10;j++){	
				g.setColor(Color.WHITE);
				g.fillRect(Spielfeld.W * j, Spielfeld.W * i, Spielfeld.W, Spielfeld.W);
				g.setColor(Color.BLUE);
				g.drawRect(Spielfeld.W * j, Spielfeld.W * i, Spielfeld.W, Spielfeld.W);
				if(field[i][j] == Felder.VERFEHLT){
					g.setColor(Color.RED);
					g.fillRect(Spielfeld.W * j, Spielfeld.W * i, Spielfeld.W, Spielfeld.W);
				}else if(field[i][j] == Felder.BOMBE) {					
					g.drawImage(img, Spielfeld.W * j, Spielfeld.W * i, null);
			    }
			}    
		}
	}
  
	
	private void init() {					
		try{
			img = ImageIO.read(new File(".\\images\\Bomb-Icon.png"));
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}	
}	



































