import java.awt.*;
import java.awt.event.*;
//import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame{
	
	public static final int GAME_WEIGHT = 900;
	public static final int GAME_HEIGHT = 600;//代码的重构  使代码变的易维护 易修改
	
	Tank MyTank = new Tank(100,500,true,Tank.Direction.STOP,this);//*********想法 ：将爆炸的位置设为每辆坦克的中心
	
	List<Tank> tanks = new ArrayList<Tank>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	Wall w = new Wall(300,300,50,200,this);
	Wall w2 = new Wall(100,300,200,50,this);
	Blood b = new Blood();
	
	Image offScreenImage = null;
	
	public void launchFrame(){
		setBounds(100,60,GAME_WEIGHT,GAME_HEIGHT);
		setResizable(false);
		setBackground(Color.GREEN);
		setTitle("TankWar");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		new Thread(new TankThread()).start();
		
		for(int i=0;i<10;i++){
			tanks.add(new Tank(50*(i+2),50,false,Tank.Direction.D,this));
		}
		addKeyListener(new KeyMonitor());
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		
		g.drawString("missiles  count:" + missiles.size(), 10, 50);
		g.drawString("explode   count:" +explodes.size(), 10, 70);
		g.drawString("tanks     count:" +tanks.size(), 10, 90);
		g.drawString("tanks     life:" +MyTank.getLife(), 10, 110);
		
		MyTank.draw(g);
		if(MyTank.fillBlood(b))
				b.setLive(false);
		
		for(int i=0;i<missiles.size();i++){
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(MyTank);
			m.hitWall(w);
			m.hitWall(w2);
			m.draw(g);
		}
		for(int i=0 ;i<explodes.size();i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		for(int i=0;i<tanks.size();i++){
			Tank t = tanks.get(i);
			t.collidesWithWall(w);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		w.draw(g);
		w2.draw(g);
		b.draw(g);
		
	}

	public void update(Graphics g) {//拦截update方法 双缓冲问题
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WEIGHT,GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(getBackground());
		gOffScreen.fillRect(0, 0, GAME_WEIGHT,GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	
	class TankThread implements Runnable{
		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class KeyMonitor extends KeyAdapter{
		public void keyReleased(KeyEvent e) {
			MyTank.keyReleased(e);
		}
		
		public void keyPressed(KeyEvent e) {
			MyTank.keyPressed(e);
		}
		
	}

	public static void main(String[] args) {
		TankClient tw = new TankClient();
		tw.launchFrame();
	}

}