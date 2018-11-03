import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	int x, y;
	Tank.Direction dir;		//枚举类型的定义类型 public static final  所以可以使用类名调用
	TankClient tc;
	private boolean live = true;
	private boolean good;
	
	public Missile(int x, int y, Tank.Direction ptdir,boolean good,TankClient tc) {
		this.x = x;
		this.y = y;
		this.good = good;
		this.dir = ptdir;//将炮筒初始的方向赋给子弹方向
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			tc.missiles.remove(this);
		}
		Color c = g.getColor();
		if(!good) g.setColor(Color.BLUE);
		else g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
	}
	
	private void move(){
		switch(dir){
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;	
		case U:
			y -= YSPEED;
			break;	
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;	
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		default:
			break;
		}
		if(x < 0||y < 0||x > TankClient.GAME_WEIGHT||y > TankClient.GAME_HEIGHT){
			live = false;
		}
	}
	
	public boolean hitTank(Tank t)
	{
		if(this.getRect().intersects(t.getRect())&& t.isLive()&&this.good != t.good)
		{
			if(t.good){
				t.setLife(t.getLife() - 20);
				if(t.getLife() <=0) t.setLive(false);
			}else
				t.setLive(false);
			this.live = false;
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
	
	public boolean hitWall(Wall w){
		if(this.getRect().intersects(w.getRect())&&this.live)
		{
			this.live = false;
			return true;
		}
		return false;
		
	}
	
	public Rectangle getRect()
	{
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
}
