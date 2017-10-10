package fr.lteconsulting.commandes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fr.lteconsulting.Commande;
import fr.lteconsulting.modele.Bibliotheque;
import fr.lteconsulting.modele.Chanson;
import fr.lteconsulting.modele.Disque;
import fr.lteconsulting.outils.Saisie;

public class ChargerFichier implements Commande
{
	private Bibliotheque bibliotheque;

	public ChargerFichier( Bibliotheque bibliotheque )
	{
		this.bibliotheque = bibliotheque;
	}

	@Override
	public String getNom()
	{
		return "Chargement de la bibliotheque à partir du fichier";
	}

	@Override
	public void executer()
	{
		try
		{
			System.out.println( "Chargement du fichier en cours..." );

			InputStream inputStream;
			if( "oui".equals( Saisie.saisie( "Utiliser la version zip ? (oui/non)" ) ) )
				inputStream = zipStream();
			else
				inputStream = flatStream();

			InputStreamReader inputStreamReader = new InputStreamReader( inputStream, "UTF8" );
			BufferedReader reader = new BufferedReader( inputStreamReader );

			String shouldBeMagicSignature = reader.readLine();
			if( !SauvegardeFichier.MAGIC_SIGNATURE.equals( shouldBeMagicSignature ) )
			{
				System.out.println( "Le fichier est corrompu !" );
				return;
			}

			try
			{
				int nombreDisquesCharges = lireBibliothequeDepuisFichier( reader );
				System.out.println( "OK, " + nombreDisquesCharges + " disques ont été chargés depuis le fichier " + SauvegardeFichier.NOM_FICHIER );
			}
			catch( Exception e )
			{
				System.out.println( "Erreur pendant la lecture du fichier !" );
			}

			reader.close();
		}
		catch( FileNotFoundException e )
		{
			System.out.println( "Le fichier n'existe pas !" );
		}
		catch( UnsupportedEncodingException e )
		{
			System.out.println( "Problème d'encodage, désolé !" );
		}
		catch( IOException e )
		{
			System.out.println( "Erreur d'entrée sortie, le disque dur fonctionne-t-il toujours ?" );
		}
	}

	private int lireBibliothequeDepuisFichier( BufferedReader reader ) throws IOException
	{
		int nombreDisques = Integer.parseInt( reader.readLine() );

		for( int i = 0; i < nombreDisques; i++ )
		{
			Disque disque = lireDisqueDepuisFichier( reader );
			if( disque != null )
				bibliotheque.ajouterDisque( disque );
		}

		return nombreDisques;
	}

	private Disque lireDisqueDepuisFichier( BufferedReader reader ) throws IOException
	{
		String nom = reader.readLine();
		String codeBarre = reader.readLine();
		int nombreChansons = Integer.parseInt( reader.readLine() );

		Disque disque = new Disque();
		disque.setNom( nom );
		disque.setCodeBarre( codeBarre );

		while( nombreChansons-- > 0 )
		{
			Chanson chanson = lireChansonDepuisFichier( reader );
			if( chanson != null )
				disque.addChanson( chanson );
		}

		return disque;
	}

	private Chanson lireChansonDepuisFichier( BufferedReader reader ) throws IOException
	{
		String nom = reader.readLine();
		int duree = Integer.parseInt( reader.readLine() );

		return new Chanson( nom, duree );
	}

	private InputStream zipStream() throws IOException
	{
		FileInputStream fileInputStream = new FileInputStream( SauvegardeFichier.NOM_FICHIER + ".zip" );
		ZipInputStream zis = new ZipInputStream( fileInputStream, Charset.forName( "UTF8" ) );

		ZipEntry ze = zis.getNextEntry();
		if( ze == null )
			return null;

		if( !SauvegardeFichier.NOM_FICHIER.equals( ze.getName() ) )
			return null;

		return zis;
	}

	private InputStream flatStream() throws FileNotFoundException
	{
		File file = new File( SauvegardeFichier.NOM_FICHIER );
		return new FileInputStream( file );
	}
}
