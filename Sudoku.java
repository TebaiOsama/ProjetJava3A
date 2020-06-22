public class Sudoku{ 
    private int tab[][]; //grille
    public int sol[][]; //grille solution
    private int N; // Taille de la grille. 
    private int nCarre; // taille d'un carre a l'interieur de la grille
    private int nVides; // cases a vider 
    private boolean isDone; 

  
    // Constructeur 
    Sudoku(int N, int nVides){ 
    	
    	isDone=false;
        this.N = N; 
        this.nVides = nVides; 
  
        //On trouve la taille des carres
        nCarre = (int)Math.sqrt(N);
  
        tab = new int[N][N]; 
        sol = new int[N][N]; 
    } 
  
   public void creation(){
	   //On remplit la diagonale qui va de la case 0 0 a la case N N
	   for(int i=0; i<N;i++) {
		   for(int j=0; j<N;j++) {
			   tab[i][j]=0;
		   }
	   }
	   resDiagonale(); 
 
	   //On remplit le reste 
	   conclusionRes(0, nCarre); 
   		
	   
	   isDone=true;
   }
    public void resolution(){ 
    	if(isDone)//On supprime les cases pour les faire deviner au joueur 
    		supprimerCases(); 
    } 
  
    //On va resoudre la diagonale, carre par carre 
    void resDiagonale(){ 
        for (int i = 0; i<N; i=i+nCarre) 
        	resCarre(i, i); 
    } 
 
  
    //Resoudre un carre 
    void resCarre(int ligne, int colonne){ 
        int val; 
        for (int i=0; i<nCarre; i++) { 
            for (int j=0; j<nCarre; j++){ 
                do{ 
                    val = (int) Math.floor((Math.random()*N+1)); 
                } while(!verifCarre(ligne, colonne, val)); 
  
                tab[ligne+i][colonne+j] = val; 
                sol[ligne+i][colonne+j] = val;
            } 
        } 
    } 
 
  
    // On verifie si on peut mettre val dans la case ij
    boolean verifications(int i, int j, int val) { 
        return (verifCarre(i-i%nCarre, j-j%nCarre, val) && verifLigne(i, val) && verifColonne(j, val)); 
    } 
  
    //On verifie si dans le carre il y a dejà la valeur val 
    boolean verifCarre(int ligne, int colonne, int val){ 
        for (int i = 0; i<nCarre; i++) 
            for (int j = 0; j<nCarre; j++) 
                if (tab[ligne+i][colonne+j]==val) 
                    return false; 
        return true; 
    } 
    
    //On verifie si dans ligne il y a deja la valeur val 
    boolean verifLigne(int i,int val) { 
        for (int j = 0; j<N; j++) 
           if (tab[i][j] == val) 
                return false; 
        return true; 
    } 
  
    //On verifie si dans la colonne il y a deja la valeur val
    boolean verifColonne(int j,int num) { 
        for (int i = 0; i<N; i++) 
            if (tab[i][j] == num) 
                return false; 
        return true; 
    } 
  
    //Fonction recursive pour completer les carrés restants, apres avoir completé la diagonale 
    boolean conclusionRes(int i, int j){ 
        if (j>=N && i<N-1) { 
            i = i + 1; 
            j = 0; 
        } 
        if (i>=N && j>=N) 
            return true; 
  
        
        if (i < nCarre && j<nCarre) //si il se trouve dans le premier carre de la diagonale
                j = nCarre; 
        else if (i < N-nCarre){ //si il se trouve dans un carre entre le premier et le dernier
            if (j==(int)(i/nCarre)*nCarre) 
                j += nCarre; 
        } 
        else if (j == N-nCarre){ //si il se trouve dans le dernier carre de la diagonale
                i++; 
                j = 0; 
                if (i>=N) 
                    return true;          
        } 
  
        for (int val = 1; val<=N; val++){ 
            if (verifications(i, j, val)){ 
            	tab[i][j] = val;
            	sol[i][j] = val;
                if (conclusionRes(i, j+1)) 
                    return true; 
  
                tab[i][j] = 0; 
                sol[i][j] = 0;
            } 
        } 
        return false; 
    } 
  
    //On supprime les cases pour demarrer le jeu
    public void supprimerCases(){ 
       
        for(int del=0; del<nVides; del++) {
        	int i = (int) Math.floor((Math.random()*N));
        	int j = (int) Math.floor((Math.random()*N));

            if (tab[i][j] == 0) //si la case est vide, on ne peut pas la supprimer donc on remet increment pas del
            	del--;    
            else //sinon, on efface la case
            	tab[i][j] = 0;
        } 
    } 
  
    public int[][] getGrille(){
    	return tab;
    }
    
    public int[][] getSolution(){
    	return sol;
    }
    
    public void setGrille(int[][] tab) {
    	this.tab=tab;
    }
    
    // Affichage de la grille de jeu (DEBUG)
    public void affichageGrille(){ 
        for (int i = 0; i<N; i++){ 
            for (int j = 0; j<N; j++) 
                System.out.print(tab[i][j] + " "); 
            System.out.println(); 
        } 
        System.out.println(); 
    } 
    
  
} 