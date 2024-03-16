package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUI {
	static JFrame jframe;
	static Image currImage;
	static boolean anyevents = false;
	static JLabel dynamicTL,staticTL;
	static JPanel dynamicBar;
	static JButton playButton,loopB;
	static Timer timer;
	static Font font;
	static JLabel currSongName;
	static JPanel currSong;
	static ArrayList<String> audioName;
	static JButton nextAudio;
	static JButton prevAudio;
	static JPanel bottomPanel;
	static boolean runAll = false;
	static JMenu menu;
	static Image bPImage,bSImage;
	public static void main(String[]args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		jframe = new JFrame("WAV Media");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setMinimumSize(new Dimension(900,400));
		font = new Font("Arial", Font.PLAIN, 16);
		//setting icon image
		ImageIcon icon = new ImageIcon("./src/project/musicIcon.png");   
		jframe.setIconImage(icon.getImage());
	
		
		//JMenuBar
		JMenuBar menuBar = new JMenuBar();
		menu = new JMenu("::choose song");
		menu.setFont(font);
//		adding all files to menu
        
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                // This method is called when the menu is selected (clicked)
                menuSetup();
            }

			@Override
			public void menuDeselected(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        //Adding scrolling for many items
        JScrollPane scrollPane = new JScrollPane(menu);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        menu.add(scrollPane);
        //adding menu to menuBar
        menuBar.add(menu);
        
        //creating checkBox;
        JCheckBox checkBox = new JCheckBox("Run-All");
        checkBox.setFont(font);
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    runAll = true;
                } else {
                    runAll = false;
                }
            }
        });
        menuBar.add(checkBox);
        //creating settings menu
        JMenu settings = new JMenu("settings");
        settings.setFont(font);
        JMenuItem changeDirectory = new JMenuItem("changeDirectory");
        changeDirectory.setFont(font);
        changeDirectory.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		
        		if(AudioPlayer.setPath()) {
        			menuSetup();
        			playButton.doClick();
        			resetMedia();
        			AudioPlayer.close();
        			currSongName.setText("");
                    currSong.add(currSongName);
                    anyevents = false;
                    playButton.setEnabled(false);
                    loopB.setEnabled(false);
                    nextAudio.setEnabled(false);
                    prevAudio.setEnabled(false);
        		}
        		
        	}
        });
        settings.add(changeDirectory);
        menuBar.add(settings);
		jframe.setJMenuBar(menuBar);

		
		//JPanel
		bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(50,50,50));
		bottomPanel.setPreferredSize(new Dimension(jframe.getWidth(),calculatePanelHeight(jframe)));
		jframe.setLayout(new BorderLayout());
		jframe.add(bottomPanel,BorderLayout.SOUTH);
		bottomPanel.setMaximumSize(new Dimension(jframe.getMaximumSize().width,130));
		
		//JPanel for dynamic time
		JPanel dynamicTime = new JPanel();
		bottomPanel.add(dynamicTime);
		dynamicTime.setBackground(new Color(50,50,50));
		dynamicTime.setBounds(0,5,(int)(jframe.getWidth()*12/100),14);
		dynamicTime.setLayout(new BorderLayout());
		
		//JLable for dynamic time
		dynamicTL = new JLabel("00:00:00");
		dynamicTime.add(dynamicTL);
        dynamicTL.setFont(font);
        dynamicTL.setHorizontalAlignment(SwingConstants.RIGHT);
        dynamicTL.setForeground(Color.WHITE);

		
		//JPanel for static time
		JPanel staticTime = new JPanel();
		bottomPanel.add(staticTime);
		staticTime.setBackground(new Color(50,50,50));
		staticTime.setBounds((int)(jframe.getWidth()-jframe.getWidth()*12/100),5,(int)(jframe.getWidth()*12/100),14);
		staticTime.setLayout(new BorderLayout());
		
		
		//JLable for static time
		staticTL = new JLabel("00:00:00");
		staticTime.add(staticTL);
        staticTL.setFont(font);
        staticTL.setHorizontalAlignment(SwingConstants.LEFT);
        staticTL.setForeground(Color.white);
		
		
		//JPanel for static Bar
		JPanel staticBar = new JPanel();
		staticBar.setBackground( Color.white);
		bottomPanel.add(staticBar);
		staticBar.setBounds((int)dynamicTime.getWidth()+5,7,(int)(jframe.getWidth()-staticTime.getWidth()-dynamicTime.getWidth())-10,8);
		
		//JPanel for Dynamic Bar
		dynamicBar = new JPanel();
		dynamicBar.setBackground(Color.orange);
		staticBar.setLayout(new BorderLayout());
		staticBar.add(dynamicBar,BorderLayout.WEST);
		dynamicBar.setPreferredSize(new Dimension(0,8));
		
		//play-pause button
        playButton = new JButton();
        playButton.setEnabled(false);
        playButton.setBackground(Color.white); 
        playButton.setBorder(BorderFactory.createEmptyBorder());
        playButton.setLayout(new BorderLayout());
        bottomPanel.add(playButton);
        //setting button dimension
        setPlayButton(jframe,playButton,0);
        
//        Current song Panel
        currSong = new JPanel();
        currSong.setBackground(new Color(150,75,0));
        currSong.setPreferredSize(new Dimension(jframe.getWidth()/2,(int)(jframe.getHeight()*0.07)));
        currSong.setLayout(new BorderLayout());
        //current song name
        currSongName = new JLabel();
        currSongName.setForeground(new Color(250, 235, 215));
        currSongName.setFont(new Font("Times New Roman", Font.BOLD,(int)(jframe.getHeight()*0.04)));
        currSongName.setHorizontalAlignment(SwingConstants.LEFT);
        jframe.add(currSong,BorderLayout.NORTH);
  
        currSong.add(currSongName);
        
        //loopButton
        loopB = new JButton("--");
        loopB.setBorder(BorderFactory.createEmptyBorder());
        bottomPanel.add(loopB);
        setPlayButton(jframe,loopB,-2);
        loopB.setEnabled(false);
        loopB.setLayout(new BorderLayout());
        loopB.setFont(font);
        loopB.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if(AudioPlayer.loopState == false) {
        			AudioPlayer.loopState = true;
        			loopB.setText("O");
        		}
        		else {
        			AudioPlayer.loopState = false;
        			loopB.setText("--");
        		}
        	}
        });
        nextAudio = new JButton(">>");
        nextAudio.setBorder(BorderFactory.createEmptyBorder());
        bottomPanel.add(nextAudio);
        setPlayButton(jframe,nextAudio,1);
        nextAudio.setLayout(new BorderLayout());
        nextAudio.setEnabled(false);
        nextAudio.setFont(font);
        nextAudio.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		//find the currentAudio
        		for(int i=0;i<audioName.size();i++) {
        			if((audioName.get(i)).equals(AudioPlayer.audioFileName)) {
        				int index = (i+1)%audioName.size();
        				try {
                        	AudioPlayer.close();
                        	resetMedia();
							new AudioPlayer(audioName.get(index));
							currSongName.setText(" Now Playing: "+audioName.get(index));
	                        currSong.add(currSongName);
	                        playButton.doClick();
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
							e1.printStackTrace();
						}
        				break;
        			}
        		}
        	}
        });
        
        prevAudio = new JButton("<<");
        prevAudio.setBorder(BorderFactory.createEmptyBorder());
        bottomPanel.add(prevAudio);
        setPlayButton(jframe,prevAudio,-1);
        prevAudio.setLayout(new BorderLayout());
        prevAudio.setEnabled(false);
        prevAudio.setFont(font);
        prevAudio.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		//find the currentAudio
        		for(int i=0;i<audioName.size();i++) {
        			if((audioName.get(i)).equals(AudioPlayer.audioFileName)) {
        				int index =(i+audioName.size()-1)%audioName.size();
        				try {
                        	AudioPlayer.close();
                        	resetMedia();
							new AudioPlayer(audioName.get(index));
							currSongName.setText(" Now Playing: "+audioName.get(index));
	                        currSong.add(currSongName);
	                        playButton.doClick();
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
							e1.printStackTrace();
						}
        				break;
        			}
        		}
        	}
        });
        //Play Button icon
        
        bPImage = ImageIO.read(new File("./src/project/playButton.png"));
        playButton.setIcon(new ImageIcon(bPImage));
        currImage = bPImage;
        //Pause Button Icon
        bSImage = ImageIO.read(new File("./src/project/pauseButton.png"));
		// Add a component listener to listen for resize events
        jframe.addComponentListener(new ComponentAdapter() {
        	
            @Override
            public void componentResized(ComponentEvent e) {
            	
                
            	bottomPanel.setPreferredSize(new Dimension(jframe.getWidth(),calculatePanelHeight(jframe)));
            	jframe.revalidate();
            	setPlayButton(jframe,playButton,0);
            	setPlayButton(jframe,nextAudio,1);
            	setPlayButton(jframe,prevAudio,-1);
            	setPlayButton(jframe,loopB,-2);
            	currSongName.setFont(new Font("Times New Roman", Font.BOLD,(int)(jframe.getHeight()*0.04)));
                
                dynamicTime.setBounds(0,5,(int)(jframe.getWidth()*12/100),14);
                staticTime.setBounds((int)(jframe.getWidth()-jframe.getWidth()*12/100),5,(int)(jframe.getWidth()*12/100),14);
                staticBar.setBounds((int)dynamicTime.getWidth()+5,7,(int)(jframe.getWidth()-staticTime.getWidth()-dynamicTime.getWidth())-10,8);
                setImage(playButton,currImage);
          
                nextAudio.setFont(new Font("Times New Roman", Font.BOLD,(int)Math.min((jframe.getHeight()*0.03),15)));
                prevAudio.setFont(nextAudio.getFont());
                loopB.setFont(nextAudio.getFont());
                currSong.setPreferredSize(new Dimension(jframe.getWidth()/2,(int)(jframe.getHeight()*0.07)));
                
            }
        });
      
        //Add a component listener to listen for maximize and minimize of window
        jframe.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == JFrame.MAXIMIZED_BOTH || e.getNewState() == JFrame.NORMAL) {
                    // Adjust panel size when maximized or restored from maximized
                    bottomPanel.setPreferredSize(new Dimension(jframe.getWidth(), calculatePanelHeight(jframe)));
                    bottomPanel.setMaximumSize(new Dimension(jframe.getMaximumSize().width,130));
                    jframe.revalidate();
                    setPlayButton(jframe,playButton,0);
                    setPlayButton(jframe,nextAudio,1);
                    setPlayButton(jframe,prevAudio,-1);
                    setPlayButton(jframe,loopB,-2);
                    
                	currSongName.setFont(new Font("Times New Roman", Font.BOLD,(int)(jframe.getHeight()*0.04)));
                    dynamicTime.setBounds(0,5,(int)(jframe.getWidth()*12/100),14);
                    staticTime.setBounds((int)(jframe.getWidth()-jframe.getWidth()*12/100),5,(int)(jframe.getWidth()*12/100),14);
                    staticBar.setBounds((int)dynamicTime.getWidth()+5,7,(int)(jframe.getWidth()-staticTime.getWidth()-dynamicTime.getWidth())-10,8);
                    setImage(playButton,currImage);
                    
                    nextAudio.setFont(new Font("Times New Roman", Font.BOLD,(int)Math.min((jframe.getHeight()*0.03),15)));
                    prevAudio.setFont(nextAudio.getFont());
                    loopB.setFont(nextAudio.getFont());
                    currSong.setPreferredSize(new Dimension(jframe.getWidth()/2,(int)(jframe.getHeight()*0.07)));
                }
            }
        });
        //
        staticBar.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if(AudioPlayer.audiostate == true) {
        			timer.stop();
        			float x = e.getX();
        			dynamicBar.setPreferredSize(new Dimension((int)x,8));
        			dynamicBar.revalidate();
        			float cliplength = AudioPlayer.clip.getMicrosecondLength();
        			float widthOfBar = staticBar.getWidth();
        			float microseconds = (x/widthOfBar)*(cliplength);
        			AudioPlayer.setPosition((long)microseconds);
        			setTime(dynamicTL,AudioPlayer.clip.getMicrosecondPosition());
        			timer.start();
        		}
        	}
        });
        //one second Timer event
        timer = new Timer(1000, new ActionListener() {
        	double fracPosition = 0;
        	
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if(fracPosition == 1) {
        			playButton.doClick();
        			currImage = bPImage;
					setImage(playButton,currImage);
        			if(AudioPlayer.audiostate == false) {
        				AudioPlayer.Reset();
        				dynamicTL.setText("00:00:00");
        				staticTL.setText("00:00:00");
        			}
        		}
        		String dTS = dynamicTL.getText();
        		String sTS = staticTL.getText();
        			       		
        		if(!(sTS.equals(dTS))) setTime(dynamicTL, -1);
        		fracPosition =(double)AudioPlayer.clip.getMicrosecondPosition()/AudioPlayer.clip.getMicrosecondLength();
        		if(fracPosition == 1) {
        			dynamicBar.setPreferredSize(new Dimension((int)staticBar.getWidth(),8));
        		}
        		else {
        			double length =  fracPosition * staticBar.getWidth();
        			length = Math.min(length, staticBar.getWidth());
        			dynamicBar.setPreferredSize(new Dimension((int)length,8));
        		}
        		dynamicBar.revalidate();
        		if(AudioPlayer.audiostate == false) {
        			if(AudioPlayer.loopState == true) {
        				playButton.doClick();
        			}
        			else if(runAll == true) {
        				nextAudio.doClick();
        			}
        		}
        	}
        });
        //play_pause event
        playButton.addActionListener(new ActionListener() {
        	
        	  @Override
        	  public void actionPerformed(ActionEvent e) {
        		dynamicTL.setForeground(Color.green);
        		staticTL.setForeground(Color.green);
        	    if(AudioPlayer.audiostate == false ) {
                	setTime(staticTL,AudioPlayer.clip.getMicrosecondLength());
					AudioPlayer.Play();
					currImage = bSImage;
					setImage(playButton,currImage);
					timer.start();
        	    }
        	    else {
        	    	AudioPlayer.Pause();
        	    	dynamicTL.setForeground(Color.red);
        	    	currImage = bPImage;
					setImage(playButton,currImage);
        	    	timer.stop();
        	    }
        	  }
        	});
        
        
		//last statement
        bottomPanel.setLayout(new BorderLayout());
        jframe.getContentPane().setBackground(new Color(30, 215, 96));
		jframe.setVisible(true);
		jframe.revalidate();
	}
	
	private static int calculatePanelHeight(JFrame jframe) {
		return (int)Math.min(( jframe.getHeight()* 0.23),130);
	}
	private static void setPlayButton(JFrame jframe,JButton Button,int i) {
		int x,y,w;
		if(i == 0) {
			w = (int)calculatePanelHeight(jframe)/2;
			x = (int)(jframe.getWidth()/2-w/2);
			y = (int)(calculatePanelHeight(jframe)/4);
			Button.setBounds(x,y,w,w);
		}
		else if(i==1) {
			w = (int)calculatePanelHeight(jframe)/2;
			x = (int)(jframe.getWidth()*2.5/4-w/2);
			y = (int)(calculatePanelHeight(jframe)/2.5);
			Button.setBounds(x,y,w/2,w/2);
		}
		else if(i==-1) {
			w = (int)calculatePanelHeight(jframe)/2;
			x = (int)(jframe.getWidth()*1.6/4-w/2);
			y = (int)(calculatePanelHeight(jframe)/2.5);
			Button.setBounds(x,y,w/2,w/2);
		}else if(i==-2) {
			w = (int)calculatePanelHeight(jframe)/2;
			x = (int)(jframe.getWidth()*1/4-w/2);
			y = (int)(calculatePanelHeight(jframe)/2.5);
			Button.setBounds(x,y,w/2,w/2);
		}
	}
	private static void setTime(JLabel TL,long microseconds) {
		String Shour,Smin,Ssec;
		int Thours,rmins,rsecs;
		if(microseconds != -1) {
			
		long ms = microseconds;
    	int Tsecs =(int) ms/1000000;
    	int Tmins = Tsecs/60;
    	Thours = Tmins/60;
    	rmins = Tmins %60;
    	rsecs = Tsecs % 60;
		}
		else {
			String dTS = TL.getText();
			rsecs = (dTS.charAt(6)-'0')*10+(dTS.charAt(7)-'0');
    		rmins = (dTS.charAt(3)-'0')*10+(dTS.charAt(4)-'0');
    		Thours = (dTS.charAt(0)-'0')*10+(dTS.charAt(1)-'0');
    		if(rsecs == 59) {
    			rsecs = 0;
    			rmins++;
    		}
    		else rsecs++;
    		if(rmins == 59) {
    			rmins = 0;
    			Thours++;
    		}
		}
    	//setting hours
    	if(Thours == 0)Shour = "00";
    	else if(Thours/10 == 0)Shour = "0"+Thours;
    	else Shour = ""+Thours;
    	//setting minutes
    	if(rmins == 0) Smin = ":00";
    	else if(rmins/10 == 0) Smin = ":0"+rmins;
    	else Smin = "00:"+rmins;
    	//setting seconds
    	if(rsecs == 0)Ssec = ":00";
    	else if(rsecs/10 == 0) Ssec = ":0"+rsecs;
    	else Ssec = ":"+rsecs;
    	//updating time label
    	TL.setText(Shour+Smin+Ssec);
		
	}
	private static void setImage(JButton B,Image i) {
		Image bImage = i.getScaledInstance(B.getWidth(), B.getHeight(), Image.SCALE_SMOOTH);;
        B.setIcon(new ImageIcon(bImage));
	}
	private static void resetMedia() {
		dynamicTL.setText("00:00:00");
		staticTL.setText("00:00:00");
		dynamicBar.setPreferredSize(new Dimension(0,8));
		dynamicBar.revalidate();
		AudioPlayer.audiostate = false;
	}

	private static String getFileExtension(String filename) {
		int index = filename.lastIndexOf(".");
		if(index>0 && index<filename.length()) {
			return filename.substring(index+1).toLowerCase();
		}
		return "";
	}
	private static void menuSetup() {
		menu.removeAll();
		if(!(audioName == null)) audioName.clear();
		AudioPlayer.readPath();
		File directory = new File(AudioPlayer.Path);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            audioName = new ArrayList<>();
            for (int i=0;i<files.length;i++) {
                String name = files[i].getName();
                String extension = getFileExtension(name);
                if(!(extension.equals("wav"))) continue;
                name = name.replace(".wav", "");
                audioName.add(name);
                JMenuItem menuItem = new JMenuItem(name);
                menuItem.setFont(font);
                menu.add(menuItem);
//                 Add action listener to the menu item
                menuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                        	if(anyevents)
                        		AudioPlayer.close();
                        	resetMedia();
                           new AudioPlayer(menuItem.getText());
                           currSongName.setText(" Now Playing: "+menuItem.getText());
                           currSong.add(currSongName);
                           anyevents = true;
                           playButton.setEnabled(true);
                           loopB.setEnabled(true);
                           nextAudio.setEnabled(true);
                           prevAudio.setEnabled(true);
                           playButton.doClick();
                        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
               
            }
        }
	}
}

