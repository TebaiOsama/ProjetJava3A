import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.NumberFormatter;



public class Fenetre  extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private Sudoku sudoku; 
	private Map<String, JTextField> fieldMap;
	private int nAide;
	private int diff;
	
	public Fenetre() {
		nAide=3;
		diff=24; //On initialise le jeu en facile
		
		final int WIDTH = 640;
		final int HEIGHT = WIDTH / 9 * 11;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Sudoku");
		pack();
		setVisible(true);
		setResizable(false);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
				
		JMenuBar mb = new JMenuBar();
		
		JMenu menu = new JMenu("Menu");
		JMenu submenu = new JMenu("Difficulté");
		
		JMenuItem s1 = new JMenuItem("Debutant");  //submenus pour les difficultés
		JMenuItem s2 = new JMenuItem("Avancé"); 
		JMenuItem s3 = new JMenuItem("Expert"); 
		JMenuItem m1 = new JMenuItem("Quitter"); 
		
		s1.addActionListener(new ActionListener() { //les differentes difficultés se differencient par le nombre de cases qu'on remet à 0
	        public void actionPerformed(ActionEvent ev) {	        	
	        	diff=24;
	        }
	    });
		s2.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ev) {
	        	diff=34;	        	
	        }
	    });
		s3.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ev) {
	        	diff=44;        	
	        }
	    });
		m1.addActionListener(new ActionListener() { //bouton pour quitter
	        public void actionPerformed(ActionEvent ev) {
	          System.exit(0);
	        }
	    });

		submenu.add(s1);
		submenu.add(s2);
		submenu.add(s3);
		
		menu.add(submenu);
		menu.add(m1);
		mb.add(menu);
		mb.setPreferredSize(new Dimension(WIDTH,30));
		setJMenuBar(mb);
		
        JLabel titre= new JLabel("SUDOKU", SwingConstants.CENTER); 
        titre.setFont(new Font("Arial", Font.BOLD, 32)); 
        titre.setBorder(new EmptyBorder(40, 10, 10, 10));	
        add(titre, BorderLayout.NORTH);

		JPanel pr1 = new JPanel();
		
		JLabel lbl = new JLabel("Projet Java 3AIT, TEBAI Osama", SwingConstants.CENTER);
		lbl.setFont(new Font("Arial", Font.PLAIN, 22)); 
		pr1.add(lbl);
		add(pr1);
		
	    NumberFormat format = NumberFormat.getInstance(); //On créé un format a utiliser sur la grille
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(1);
	    formatter.setMaximum(9);
	    formatter.setAllowsInvalid(false);
	    formatter.setCommitsOnValidEdit(true);
	    		
		JPanel pr3 = new JPanel(new GridLayout(1,3));
		pr3.setBorder(new EmptyBorder(10, 10, 300, 10));
		
		add(pr3, BorderLayout.SOUTH);
		
		JButton start = new JButton("Démarrer"); //bouton qui demarre le jeu
		start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) { 
				sudoku = new Sudoku(9, diff);
				sudoku.creation(); 
				sudoku.resolution();
				remove(pr1);
				setJMenuBar(null);
				JPanel pr1 = new JPanel(new GridLayout(9,9));
				titre.setBorder(new EmptyBorder(10, 10, 10, 10));	
				add(remplissageGrille(formatter, pr1), BorderLayout.CENTER);
				pr3.remove(start);
				pr3.setBorder(new EmptyBorder(10, 10, 10, 10));			
				JButton restart = new JButton("Récommencer"); //demander si cette partie ou une autre
				restart.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) { 					
						Object[] options = {"Nouvelle partie","Récommencer"};
						int n = JOptionPane.showOptionDialog(frame,
						    "Voulez vous commencer une nouvelle partie, ou recommencer celle-ci ?",
						    "Attention",
						    JOptionPane.YES_NO_CANCEL_OPTION,
						    JOptionPane.QUESTION_MESSAGE,
						    null,
						    options,
						    options[1]);
						if(n==0) { //Si on veut faire une nouvelle partie, on supprime cette fenetre et on en ouvre une autre
							dispose();
							new Fenetre();			
						}
						else if(n==1) { //Pour recommencer une partie, on remet les aides à 3 et on remplit à nouveau la grille
							nAide=3;
							remove(pr1);
							JPanel pr1 = new JPanel(new GridLayout(9,9));		
							add(remplissageGrille(formatter, pr1), BorderLayout.CENTER);
							pr1.revalidate();
							pr1.repaint();
							revalidate();
							repaint();
						}
					}
				});
				
				restart.setPreferredSize(new Dimension(70, 40));
				restart.setBackground(new Color(59, 89, 182));
				restart.setForeground(Color.WHITE);
				restart.setFocusPainted(false);
				restart.setFont(new Font("Tahoma", Font.BOLD, 19));
					
				pr3.add(restart);
				JButton valider = new JButton("Valider"); //verifier si c'est complet, et dire si c'est tout bon ou pas
				valider.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {			
						if(isFinished(fieldMap)) {
							int counter=0;
							boolean isCorrect=true;
							for(int i=0; i<9;i++) {
								for(int j=0; j<9; j++) {
									if(!Objects.equals(String.valueOf(sudoku.sol[i][j]), fieldMap.get(String.valueOf(counter)).getText())) {
										isCorrect=false;					
									}
									counter++;
								}	
							}
							if(isCorrect)
								JOptionPane.showMessageDialog(null, "Votre solution est correcte !");
							else
								JOptionPane.showMessageDialog(null, "Tentez à nouveau !");

						}
						else
							JOptionPane.showMessageDialog(null, "Vous n'avez pas remplit toutes vos cases !");
					}
				});
				
				valider.setPreferredSize(new Dimension(70, 40));
				valider.setBackground(new Color(59, 89, 182));
				valider.setForeground(Color.WHITE);
				valider.setFocusPainted(false);
				valider.setFont(new Font("Tahoma", Font.BOLD, 19));
				
				pr3.add(valider);
				JButton aide = new JButton("Aide"); //Indices pour l'utilisateur
				aide.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) { 
						Object[] options = {"Oui","Non"};
						if(nAide==0) {
							JOptionPane.showMessageDialog(null, "Vous n'avez aucun aide restant.");
						}
						else {
							int n = JOptionPane.showOptionDialog(frame,
							    "Voulez vous utiliser un aide ? Vous avez "+nAide+" aide"+ (nAide==1?"":"s") +" restant" +(nAide==1?"":"s")+ ".",
							    "",
							    JOptionPane.YES_NO_CANCEL_OPTION,
							    JOptionPane.QUESTION_MESSAGE,
							    null,
							    options,
							    options[1]);
							if(n==0) {
								ArrayList<ArrayList<Integer>> caseVide = new ArrayList<ArrayList<Integer>>();
								for(int i=0; i<9; i++) {
									for(int j=0; j<9; j++) {
										if(sudoku.getGrille()[i][j]==0) {
											ArrayList<Integer> ligne = new ArrayList<Integer>();
											ligne.add(i);
											ligne.add(j);
											caseVide.add(ligne);
										}
									}
								}
	
								int rand = (int) Math.floor((Math.random()*caseVide.size()));
							
								fieldMap.get(String.valueOf(caseVide.get(rand).get(0)*9+caseVide.get(rand).get(1))).setText(String.valueOf(sudoku.getSolution()[caseVide.get(rand).get(0)][caseVide.get(rand).get(1)]));

								pr1.revalidate();
								pr1.repaint();
								nAide--;
							}
						}
					}
				});
				
				aide.setPreferredSize(new Dimension(70, 40));
				aide.setBackground(new Color(59, 89, 182));
				aide.setForeground(Color.WHITE);
				aide.setFocusPainted(false);
				aide.setFont(new Font("Tahoma", Font.BOLD, 19));
				
				pr3.add(aide);
				
				pr1.revalidate();
				pr1.repaint();
				revalidate();
				repaint();

			}
		});
		start.setPreferredSize(new Dimension(70, 80));
		start.setBackground(new Color(59, 89, 182));
		start.setForeground(Color.WHITE);
		start.setFocusPainted(false);
		start.setFont(new Font("Tahoma", Font.BOLD, 19));
		pr3.add(start);

		revalidate();
		repaint();	
	}
	
	public JPanel remplissageGrille(NumberFormatter formatter, JPanel pr1) {
		int counter=0;
	    fieldMap = new HashMap<String, JTextField>();
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				JTextField txt = new JFormattedTextField(formatter);
				
				if(sudoku.getGrille()[i][j]==0) {
					
					txt.setText(" ");
					txt.setName("CaseVide");
					txt.setFont(new Font("Arial", Font.PLAIN, 22));
					txt.setEditable(true);
				}
				else {
					txt.setText(String.valueOf(sudoku.getGrille()[i][j]));
					txt.setFont(new Font("Arial", Font.BOLD, 22));
					txt.setEditable(false);
				}
				
				if(j%3==0) {
					if(i%3==0) {
						//txt.createMatteBorder(0, 0, 1, 1, Color.BLACK));
						txt.setBorder(new MatteBorder(3, 3, 1, 1, Color.BLACK));
					}
					else if(i==8)
						txt.setBorder(new MatteBorder(1, 3, 3, 1, Color.BLACK));
					else
						txt.setBorder(new MatteBorder(1, 3, 1, 1, Color.BLACK));
				}
				else if(j==8) {
					if(i%3==0) 
						txt.setBorder(new MatteBorder(3, 1, 1, 3, Color.BLACK));
					else if(i==8)
						txt.setBorder(new MatteBorder(1, 1, 3, 3, Color.BLACK));
					else
						txt.setBorder(new MatteBorder(1, 1, 1, 3, Color.BLACK));
				}
				else if(i%3==0) {
					if(j%3==0) 
						txt.setBorder(new MatteBorder(3, 3, 1, 1, Color.BLACK));
					else if(j==8)
						txt.setBorder(new MatteBorder(3, 1, 1, 3, Color.BLACK));
					else
						txt.setBorder(new MatteBorder(3, 1, 1, 1, Color.BLACK));
				}
				else if(i==8) {
					if(j%3==0) 
						txt.setBorder(new MatteBorder(1, 3, 3, 1, Color.BLACK));
					else if(j==8)
						txt.setBorder(new MatteBorder(1, 1, 3, 3, Color.BLACK));
					else
						txt.setBorder(new MatteBorder(1, 1, 3, 1, Color.BLACK));
				}
				else
					txt.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
				//txt.setBorder(BorderFactory.createLineBorder(Color.black, 1)); 
				pr1.setBorder(new MatteBorder(3,3,3,3, Color.BLACK)); 
				
				fieldMap.put(String.valueOf(counter), txt);
				txt.setHorizontalAlignment(JTextField.CENTER);		
				pr1.add(txt);
				counter++;
				
			}
		}
		return pr1;
	}
	public boolean isFinished(Map<String, JTextField> fieldMap) { //on verifie si on a fini
		
		for(int i=0; i<81; i++) {
			if(fieldMap.get(String.valueOf(i)).getName()=="CaseVide") {			
				if(!(fieldMap.get(String.valueOf(i)).getText().contains("1") || fieldMap.get(String.valueOf(i)).getText().contains("2") || 
						fieldMap.get(String.valueOf(i)).getText().contains("3") || fieldMap.get(String.valueOf(i)).getText().contains("4") || 
						fieldMap.get(String.valueOf(i)).getText().contains("5") || fieldMap.get(String.valueOf(i)).getText().contains("6") || 
						fieldMap.get(String.valueOf(i)).getText().contains("7") || fieldMap.get(String.valueOf(i)).getText().contains("8") || 
						fieldMap.get(String.valueOf(i)).getText().contains("9"))){
					return false;
				}
			}			
		}
		return true;		
	}
	
	
	public static void main(String[] args){ 
		//System.out.print(3%3);
		Fenetre fen = new Fenetre();
	}
}
