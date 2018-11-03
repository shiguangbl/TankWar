import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;;

public class Tank {
	
	public static final int XSPEED = 3;
	public static final int YSPEED = 3;
	int x, y,oldX,oldY;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	private boolean bl = false,bu = false,br = false,bd = false;
	enum Direction{L ,LU ,U ,RU ,R ,RD ,D ,LD ,STOP};
	private Direction dir = Direction.STOP;
	private Direction ptdir = Direction.D;

	TankClient tc = null;
	boolean good;
	private boolean live = true;
	private int life = 100; 
	private bloodBar bb = new bloodBar();
	
	private static Random r = new Random();
	private int step = r.nextInt(14)+3;
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public Tank(int x, int y,boolean good,Direction dir,TankClient tc){
		this.x = x;
		this.y = y;
		this.good = good;
		this.dir = dir;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			if(!good){
				tc.tanks.remove(this);
			}	
			return;
		}
		if(good) bb.drawBar(g);
		Color c = g.getColor();
		if(good) g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		drawpt(g);
		move();
	}
	
	public void drawpt(Graphics g){
		switch(ptdir) {
		case L:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x, y + HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x + WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x + WIDTH, y);
			break;
		case R:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x + WIDTH, y + HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x + WIDTH, y + HEIGHT);
			break;
		case D:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x + WIDTH/2, y + HEIGHT);
			break;
		case LD:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x, y + HEIGHT);
			break;
		default:
			break;
		}
	}
	
	private void move(){
		oldX = x;
		oldY = y;
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
		case STOP:
			break;
		}
		if(dir != Direction.STOP)
			ptdir = dir;
		if(x < 0) x = 0;
		if(y < 25) y = 25;
		if(x > TankClient.GAME_WEIGHT - WIDTH) x = TankClient.GAME_WEIGHT - WIDTH;
		if(y > TankClient.GAME_HEIGHT - HEIGHT) y = TankClient.GAME_HEIGHT - HEIGHT;
	
		if(!good) {			//随机让敌军移动和开火
			if(step == 0){
				Direction[] dn = Direction.values();
				int rn = r.nextInt(dn.length);
				dir = dn[rn];
				step = r.nextInt(15)+20;
			}
			if(r.nextInt(50)>48) fire();
			step--;
		}
	
	}
	
	public Missile fire(){
		if(!live)
			return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2 ;
		int y= this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,this.ptdir,good,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public void superFire(){
		if(live){
			Direction [] dn = Direction.values();
			int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2 ;
			int y= this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
			for(int i=0;i<8;i++){
				Missile m = new Missile(x,y,dn[i],good,tc);
				tc.missiles.add(m);
			}
		}

	}
	
	public boolean collidesWithWall(Wall w){
		if(this.getRect().intersects(w.getRect())&&this.live)
		{
			stay();
			return true;
		}
		return false;
	}
	
	public boolean collidesWithTanks(List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){
			if(this != tanks.get(i)&&this.getRect().intersects(tanks.get(i).getRect())){
				stay();
				return true;
			}
		}
		return false;
	}
	
	public boolean fillBlood(Blood b){
			if(this.getRect().intersects(b.getRect())&&tc.b.isLive()){
				life = 100;
				return true;
			}
			return false;
	}
	
	public void stay(){
		x = oldX;
		y = oldY;
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		switch(key){
			case KeyEvent.VK_LEFT :
				bl = true;
				break;
			case KeyEvent.VK_UP :
				bu = true;
				break;
			case KeyEvent.VK_RIGHT :
				br = true;
				break;
			case KeyEvent.VK_DOWN :
				bd = true;
				break;
		}
		dirLocation();
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
			case KeyEvent.VK_X:
				fire();
				break;
			case KeyEvent.VK_A:
				superFire();
				break;
			case KeyEvent.VK_LEFT :
				bl = false;
				break;
			case KeyEvent.VK_UP :
				bu = false;
				break;
			case KeyEvent.VK_RIGHT :
				br = false;
				break;
			case KeyEvent.VK_DOWN :
				bd = false;
				break;
		}
		dirLocation();
	}
	private void dirLocation(){
		if(bl && !bu && !br && !bd) dir = Direction.L; 		//此方向控制有一定技巧
		else if(bl && bu && !br && !bd) dir = Direction.LU;
		else if(!bl && bu && !br && !bd) dir = Direction.U;
		else if(!bl && bu && br && !bd) dir = Direction.RU;
		else if(!bl && !bu && br && !bd) dir = Direction.R;
		else if(!bl && !bu && br && bd) dir = Direction.RD;
		else if(!bl && !bu && !br && bd) dir = Direction.D;
		else if(bl && !bu && !br && bd) dir = Direction.LD;
		else if(!bl && !bu && !br && !bd) dir = Direction.STOP;
	}

	public Rectangle getRect()
	{
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	class bloodBar{
		public void drawBar(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.BLACK);
			g.drawRect(x, y-20, WIDTH, 15);
			g.setColor(Color.ORANGE);
			g.fillRect(x, y-20, WIDTH*life/100, 15);
			g.setColor(c);
		}
	}

}
