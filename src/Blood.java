
import java.awt.*;

public class Blood {
	public Blood() {
		x = arr[0][0];
		y = arr[0][1];
	}
	int step = 0;
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	private int arr[][] = {{100,100},{125,138},{150,120},{160,125},{170,130},{180,135},{185,140}
							};
	int x,y;
	
	public void draw(Graphics g){
		if(!live){
			return ;
		}
		Color c = g.getColor();
		g.setColor(Color.orange);
		g.fillRect(x, y, 20, 15);
		g.setColor(c);
		move();
	}
	
	private void move(){
		step++;
		if(step == arr.length){
			step = 0;
		}
		x = arr[step][0];
		y = arr[step][1];
	}
	
	public Rectangle getRect()
	{
		return new Rectangle(x,y,20,15);
	}
}
