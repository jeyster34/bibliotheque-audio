package fr.lteconsulting.commandes;

import fr.lteconsulting.Commande;
import fr.lteconsulting.ContexteExecution;
import fr.lteconsulting.modele.Bibliotheque;
import fr.lteconsulting.modele.Disque;
import fr.lteconsulting.outils.GenerateurDisque;
import fr.lteconsulting.outils.Saisie;

public class GenerationDisques implements Commande
{
	@Override
	public String getNom()
	{
		return "Générer des disques au hasard";
	}

	@Override
	public void executer( ContexteExecution contexte )
	{
		int nbDisques = Saisie.saisieInt( "Combien de disques voulez-vous générer?" );
		if( nbDisques <= 0 )
			return;

		System.out.println( "Génération des disques :" );

		GenerateurDisque generateur = new GenerateurDisque();
		while( nbDisques-- > 0 )
		{
			Disque disque = generateur.genererDisque();

			disque.afficher( false );

			contexte.getBibliotheque().ajouterDisque( disque );
		}
	}
}
