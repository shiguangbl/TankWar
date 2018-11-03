 import java.awt.*;

public class Explode {
	int x,y;
	boolean live = true; 
	int diameter [] = {4,20,30,40,50,30,10,2};
	int step = 0;
	TankClient tc;
	
	public Explode(int x,int y,TankClient tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			tc.explodes.remove(this);
			return;
		}
		if(step == diameter.length){
			live = false;
			step = 0;
			return;
		}

		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, diameter[step], diameter[step]);
		step++;
		g.setColor(c);
	}

}
