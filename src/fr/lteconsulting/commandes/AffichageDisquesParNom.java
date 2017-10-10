package fr.lteconsulting.commandes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.lteconsulting.Commande;
import fr.lteconsulting.modele.Bibliotheque;
import fr.lteconsulting.modele.Disque;
import fr.lteconsulting.outils.ComparateurDisqueParNom;

public class AffichageDisquesParNom implements Commande
{
	private Bibliotheque bibliotheque;

	public AffichageDisquesParNom( Bibliotheque bibliotheque )
	{
		this.bibliotheque = bibliotheque;
	}

	@Override
	public String getNom()
	{
		return "Afficher les disques par nom";
	}

	@Override
	public void executer()
	{
		List<Disque> disques = new ArrayList<Disque>( bibliotheque.getDisques() );

		Collections.sort( disques, new ComparateurDisqueParNom() );

		for( Disque disque : disques )
			disque.afficher( false );
	}
}