package fr.cda.projet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import fr.cda.models.Commande;
import fr.cda.models.Produit;
import fr.cda.util.*;

// Classe de definition du site de vente
//
public class Site {
    private ArrayList<Produit> stock;       // Les produits du stock
    private ArrayList<Commande> commandes;  // Les bons de commande

    // Constructeur
    //
    public Site() {
        stock = new ArrayList<Produit>();
        //manière sécurisé de lire les fichiers contenu dans le dossier ressources
        // Je n'avais pas vu qu'il y avait une fonction pour lire les fichiers txt dans util/Terminal... héhé


        //  pour chaque ligne on cree un Produit que l'on ajoute a stock
        try {
            InputStream produits = getClass().getClassLoader().getResourceAsStream("data/Produits.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(produits, StandardCharsets.UTF_8));
            String ligne;

            // Parcourir chaque ligne
            while ((ligne = reader.readLine()) != null) {
                // Découper la ligne en fonction du point-virgule
                String[] elements = ligne.split(";");

                String reference = elements[0]; // les variables reference et libelle ne sont pas obligatoire
                String libelle = elements[1];
                double prix = Double.parseDouble(elements[2]);
                int quantite = Integer.parseInt(elements[3]);

                Produit p = new Produit(reference, libelle, prix, quantite);
                stock.add(p);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }


        //TODO
        //ajouter les articles et quantités commandées dans la arraylist commandes

        commandes = new ArrayList<>();

        try {
            InputStream streamCommande = getClass().getClassLoader().getResourceAsStream("data/Commandes.txt");
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(streamCommande, StandardCharsets.UTF_8));
            String ligne;

            // Parcourir chaque ligne
            while ((ligne = reader2.readLine()) != null) {
                // Découper la ligne en fonction du point-virgule
                String[] elements = ligne.split(";");
                String[] decoupe = elements[3].split("=");
                String referenceProduit = decoupe[0];

                int numeroCommande = Integer.parseInt(elements[0]); // les variables reference et libelle ne sont pas obligatoire
                String date = elements[1];
                String nomPrenom = elements[2];
                Produit prodCom = trouverProduit(referenceProduit);
                int qtt = Integer.parseInt(decoupe[1]); // a voir ou mettre la quantité pour plus tard
                prodCom.setQuantite(qtt);

                // TODO : Les même id de commandes doivent aller dans la même ligne de commande
                // TODO : boucle pour lire notre arraylist si ArrayList[1];equals(elements[0]) alors on ajoute le nom du produit à l'arraylist nom produit
                Commande commandeExistante = null; //new Commande(listProdCom,nomPrenom, date, referenceCommande);


                for (Commande c : commandes) {
                    if (c.getNumero() == numeroCommande) {
                        commandeExistante = c; // On a trouvé une commande existante -> on stop
                        break;
                    }
                }
                // si la commande existe bien, on ajoute juste la référence du produit
                if (commandeExistante != null) {
                    commandeExistante.getReferences().add(referenceProduit);
                } else {
                    ArrayList<String> listProdCom = new ArrayList<>();
                    listProdCom.add(referenceProduit);

                    Commande com = new Commande(listProdCom, date, nomPrenom, numeroCommande);
                    commandes.add(com);
                }



            }
            reader2.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }

        //  pour chaque ligne on cree une commande ou on ajoute une reference
        //  d'un produit a une commande existante.
        // AC AC
    }

    public Produit trouverProduit(String reference) {
        if (stock.isEmpty()) {
            return null;
        }
        for (Produit p : stock) {
            if (p.getReference().equals(reference)) {
                return p;
            }
        }
        return null;
    }

    //Méthode pour récupérer le prix d'un produit
    public double chercherPrix(String reference) {
        for (Produit p : stock) {
            if (p.getReference().equals(reference)) {
                return p.getPrix();
            }
        }
        return 0.0;
    }

    // Methode qui retourne sous la forme d'une chaine de caractere
    //  tous les produits du stock
    // à retraivller pour affichage
    public String listerTousProduits() {
        String res = "";
        for (Produit prod : stock)
            res = res + prod.toString() + "\n";

        return res;
    }

    // Methode qui retourne sous la forme d'une chaine de caractere
    //  toutes les commandes
    //
    public String listerToutesCommandes() {
        String res = "Voici la liste des commandes passées : \n";

        for (Commande com : commandes) {
            res = res + com.toString() + "\n";
        }

        res = res + "Fin des commandes.\n";

        return res;
    }

    // Methode qui retourne sous la forme d'une chaine de caractere
    //  une commande
    //
    public String listerCommande(int numero) {
        String res = "Cette methode n'est pas codee\n";
        res = res + "Numero de commande : " + numero + "\n";
        res = res + "Elle doit retourner le contenu d'une commande\n";

        return res;
    }

    // Chargement des données du stock
    //
    private void initialiserStock(String nomFichier) {

    }
}