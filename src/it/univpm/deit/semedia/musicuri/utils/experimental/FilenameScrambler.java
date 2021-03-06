/*
 Copyright (c) 2005, Dimitrios Kourtesis
 
 This file is part of MusicURI.
 
 MusicURI is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.
 
 MusicURI is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with MPEG7AudioEnc; see the file COPYING. If not, write to
 the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 MA  02111-1307 USA
 */

package it.univpm.deit.semedia.musicuri.utils.experimental;

import it.univpm.deit.semedia.musicuri.core.Toolset;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;


/**
 * Utility class used for scrambling the filenames of audio files in order to generate new test cases.
 * The srambling is done either using sentences generated by context-free grammars, or by inserting 
 * random words and random character flips. This code was initially used to test the robustness 
 * of various approximate string matching algorithms.
 */
public class FilenameScrambler
{
	// list of words to ignore
	private static ArrayList ignored = new ArrayList();
	
	// 100 non-existing band names, created using a context free grammar
	// taken from http://www.elsewhere.org/cgi-bin/bandname
	private static String[] ContextFreeGrammarBandNames = 
	{ "Order Of The Old Time Tree", "The Universal Ladies", "Four Creeks With Colors", 
		"The Salamander Gambit", "Bob Stardust and the Telluride Buckaroos", "Still Clown", 
		"Hammerbreeder", "The Venomous Badgers", "Donna Piro and the Jerusalem Police", 
		"Stack of Creeks", "Large Dog", "The Famous Box Society", "Bob Lovlace and the Smokable Whiskey Session", 
		"A Taste of Lakes", "Badgerbeagle", "Box of Wookiees", "Five Chains and Omega", 
		"Six Good Ducks", "Frogbreeder", "The Moon Company", "The Metaphysical Alpha Session", 
		"Donna Claypool and the Detroit Anteaters", "A Taste of Puppies", "A Sack of Guys", 
		"The Ravenous Devil Headquarters", "The Venomous Flower Scheme", "Four Fellows and a Mouse", 
		"Grace Badabing and the Venomous Motor Symphony", "Pink Color", "A Tower of Creeks", 
		"Kamikaze Cholera", "Adam Banzai and the Screaming Carpenters", "Handful of Sounds", 
		"Bumblebeewater", "Peter Tarantino and the Montgomery Daleks", "House of Fears", 
		"Purityduck", "Box of Wookiees", "Screaming Bishop", "Four Ladies and a Thumb", 
		"Crystal Cow", "The Flying Grrrl Trio", "Three Rasta Horses", "Peter Stallone and the Cleveland Crickets", 
		"Buddy Johnson and the Still Oddities", "A Handful of Colors", "The Honey Group", 
		"The Confederate River Symphony", "Bag of Ducks", "Leather Cat", "The Flying Temple Combo", 
		"Metaphysical Milk", "The Amazing Delta Brotherhood", "Buckaroo Lovlace and the Large Juggler Gambit", 
		"Box of Goats", "Four Womyn and a Axe", "Old Time Fear", "A Handful of Mice", "Puppyfennel", 
		"Britney Summers and the Messiest Patrol", "Grace Stallone and the San Francisco All Stars", 
		"Wall of Heads", "The Thick Star Factor", "Temple of Badgers", "Templebumblebee", "The Ganja Quintet", 
		"Britney LaZonga and the Kamikaze Boingers", "Hamsterpurity", "A Stack of Oysters", 
		"Buck Cooper and the Rasta Oddities", "Modern Hog", "A Tower of Anvils", 
		"Buck and the Stellar Replicants", "The Garden Brotherhood", "Mothergun", "Starving Duck", 
		"The Live Delta Incident", "Two Womyn and a Synth", "Taste of Horses", "A House of Hogs",  
		"Linda Cornell and the Blindfolded Angels", "Les Claypool and the Heavenly All Stars", 
		"Sylvester and the Stellar Leisurelies", "Six Clowns and a Woman", "The Liquid Gamma Company", 
		"Temple of Ladies", "The Snout Conspiracy", "A Fistful of Beagles", "A Fistful of Fish", 
		"Five Candles With Guns", "The Swollen Beagles", "Grace Turgidson and the Kamikaze Fulcrum Symphony", 
		"Fabulous War", "The Live Ladies", "Two Venomous Sheep", "The Red Beagle Shrine", "A Temple of Heads", 
		"The Abundant Badger Sound", "The Anvil Policy", "Quentin Phillips and the Seattle Plumbers" };

	// 123 words extracted from George Orwell's "1984"
	// taken from http://www.vocabulary.com/VUctnineteen.html
	private static String[] GeorgeOrwell1984 = 
	{ 
		"depict", "simultaneously", "predicament", "interminable", "unorthodox", "renegade", "polysyllabic", 
		"refute", "flog", "inscrutable", "discountenanced", "gamboling", "saboteur", "raspingly", 
		"reverberate", "reproach", "fathom", "disdain", "annihilate", "repudiate", "shrewish", "orifice", 
		"collate", "multifarious", "kaleidoscope", "denounce", "implicate", "hoard", "remorselessly", 
		"venerate", "heretic", "catapult", "irrepressible", "proliferate", "debauchery", "promiscuity", 
		"aquiline", "jostle", "flog", "listless", "incriminate", "posterity", "stratum", "balminess", 
		"sordid", "altercation", "meditatively", "innumerable", "cumbersome", "officiousness", "unprocurable", 
		"niggling", "hallucination", "malignant", "guise", "dapple", "swine", "strenuousness", "demeanor", 
		"stagnant", "commodity", "chastity", "inconceivable", "eccentricity", "unendurable", "effigies", 
		"rowdy", "indignation", "perish", "mutability", "invariably", "queue", "intermittent", "luminous", 
		"scrounge", "pathos", "remonstrance", "proles", "intimidate", "wainscoting", "salutation", "allusion", 
		"fretted", "voluptuous", "reprisal", "fecundity", "dilapidated", "ravage", "austere", "spurious", 
		"preponderance", "tacitly", "ruminant", "plunder", "irrevocable", "irreconcilable", "recurrence", 
		"hierarchy", "infallible", "oligarchical", "scrutinize", "dissipate", "repression", "supple", 
		"reverence", "gnawing", "peddler", "timorously", "irony", "sabotage", "truncheon", "embezzlement", 
		"inquisitor", "despicable", "bludgeon", "forlorn", "degradation", "taut", "improvisation", 
		"interpose", "haggling", "digression", "tiddlywinks"
		};
	
		
	public static void main(String[] args) throws Exception
	{
		
		//*****************************************************************************
		//*************************   F I L E   I N P U T   ***************************
		//*****************************************************************************
		
		if ((args.length == 1) && (new File (args[0]).exists()))
		{
			// get the file's canonical path
			File givenHandle = new File(args[0]);
			String queryAudioCanonicalPath = givenHandle.getCanonicalPath();
			System.out.println("Input: " + queryAudioCanonicalPath);
			
			
			if (givenHandle.isDirectory())
			{
				File[] list = givenHandle.listFiles();
				if (list.length == 0)
				{
					System.out.println("Directory is empty");
					return;
				}
				else
				{
					File currentFile;
					for (int i = 0; i < list.length; i++)
					{
						currentFile = list[i];
						if (Toolset.isSupportedAudioFile(currentFile))
						{
							String oldFilename = currentFile.getName();
							String newFilename = generateSubstitute(oldFilename);
							//String newFilename = generateBogusBandNameSubstitute(oldFilename, i);
							System.out.println("From : " + oldFilename);
							System.out.println("To   : " + newFilename);
							String parent = currentFile.getParent();
							doRename(parent+"\\"+oldFilename, parent+"\\"+newFilename);
						}
						
					}
				}
			}
			
			if (givenHandle.isFile())
			{
				if (Toolset.isSupportedAudioFile(givenHandle))
				{
					String oldFilename = givenHandle.getName();
					String newFilename = generateSubstitute(oldFilename);
					//String newFilename = generateBogusBandNameSubstitute(oldFilename, 0);
					System.out.println("From : " + oldFilename);
					System.out.println("To   : " + newFilename);
					String parent = givenHandle.getParent();
					doRename(parent+"\\"+oldFilename, parent+"\\"+newFilename);
				}
			}
			
		}//end if
		
		
		else
		{
			System.err.println("FilenameScrambler");
			System.err.println("Usage: java tester.FilenameScrambler {original.mp3}");
		}
		
	}//end main method
	
	
	public static String generateBogusBandNameSubstitute(String oldFilename, int index)
	{
		String band = ContextFreeGrammarBandNames[index];
		String newFilename = "";
		
		StringTokenizer chop = new StringTokenizer(oldFilename,"-");
		//System.out.println("num of tokens : " + chop.countTokens());
		
		int testCaseId = Toolset.getTestCaseIdentifier(oldFilename);
		String idPrePadding = "";
		if (testCaseId >= 1 )
		{
			if (testCaseId <10 )idPrePadding = "000";
			if (testCaseId >=10 && testCaseId < 100 )idPrePadding = "00";
			if (testCaseId >=100 && testCaseId < 1000 )idPrePadding = "0";
		}
				
		int i = 0;
		String token;
		int numOfToks = chop.countTokens();
		while (chop.hasMoreTokens())
		{
			token = chop.nextToken();
			//System.out.println(i + " token : " + token);
			if ( i == numOfToks-1 ) 
			{
				//System.out.println(i + "/ newFilename: " + newFilename); 
				newFilename = newFilename.concat(idPrePadding + testCaseId + " " + band + " -" + token);
				//System.out.println(i + "/ newFilename: " + newFilename);
			}
			i++;
		}
		

		return newFilename;
	}
		

	public static void doRename (String oldname, String newname)
	{
		File file = new File(oldname);
		File file2 = new File(newname);
		
		// Rename file (or directory)
		boolean success = file.renameTo(file2);
		if (!success) 
		{
			if (!file.exists()) System.out.println("File does not exist");
			if (!file.canWrite()) System.out.println("No write access");
			System.out.println("File could not be renamed");
		}
	}
	
	
	public static String generateSubstitute(String oldFilename)
	{
		// The 50 most common words in the english language
		// http://esl.about.com/library/vocabulary/bl1000_list1.htm
		// http://www.world-english.org/english500.htm
		// http://www.duboislc.org/EducationWatch/First100Words.html
		
		ignored.add("the");		ignored.add("of");		ignored.add("and");		
		ignored.add("a");		ignored.add("to");		ignored.add("in");		
		ignored.add("is");		ignored.add("you");		ignored.add("that");		
		ignored.add("it");		ignored.add("he");		ignored.add("was");		
		ignored.add("for");		ignored.add("on");		ignored.add("are");		
		ignored.add("as");		ignored.add("with");	ignored.add("his");
		ignored.add("they");	ignored.add("i");		ignored.add("at");		
		ignored.add("be");		ignored.add("this");	ignored.add("have");		
		ignored.add("from");	ignored.add("or");		ignored.add("one");		
		ignored.add("had");		ignored.add("by");		ignored.add("word");		
		ignored.add("but");		ignored.add("not");		ignored.add("what");		
		ignored.add("all");		ignored.add("were");	ignored.add("we");		
		ignored.add("when");	ignored.add("your");	ignored.add("can");		
		ignored.add("said");	ignored.add("there");	ignored.add("use");		
		ignored.add("an");		ignored.add("each");	ignored.add("which");		
		ignored.add("she");		ignored.add("do");		ignored.add("how");		
		ignored.add("their");	ignored.add("if");
						
		// Some frequently occuring words related to music filenames
		ignored.add("mp3");		ignored.add("&");		ignored.add("featuring");
		ignored.add("+");		ignored.add("feat");	ignored.add("presenting");		
		ignored.add("pres");	ignored.add("live");	ignored.add("track");		
		ignored.add("album");	ignored.add("various");	ignored.add("artists");		
		ignored.add("artist");	ignored.add("va");		ignored.add("collection");		
		ignored.add("sampler");	ignored.add("mix");		ignored.add("complilation");		
		ignored.add("mixed");	ignored.add("remix");	ignored.add("remixed");		
		ignored.add("lp");		ignored.add("ep");		ignored.add("cd");		
		ignored.add("");
		
		String identifier = "";
		int testCaseId = Toolset.getTestCaseIdentifier(oldFilename);
		if (testCaseId >= 1 )
		{
			if (testCaseId >=0 && testCaseId <10 )identifier = "000" + testCaseId + " ";
			if (testCaseId >=10 && testCaseId < 100 )identifier = "00" + testCaseId + " ";
			if (testCaseId >=100 && testCaseId < 1000 )identifier = "0" + testCaseId + " ";
		}
		String oldFilenameWithoutIdentifier = Toolset.removeTestCaseIdentifier(oldFilename);
		String extension = oldFilenameWithoutIdentifier.substring(oldFilenameWithoutIdentifier.lastIndexOf('.') + 1);
		String noId_noExtension = oldFilenameWithoutIdentifier.substring(0, oldFilenameWithoutIdentifier.lastIndexOf('.'));
		oldFilenameWithoutIdentifier.replaceAll(".mp3","");
		String scrambledTokenFilename = "";
		String whitespaceFreeFilename = "";
		String finalFilename = "";
		
		StringTokenizer tok = new StringTokenizer(noId_noExtension," `~!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/\t\n\r");
			
		int numOfTokensInString = tok.countTokens();		
		String[] tokens = new String[numOfTokensInString]; // list of tokens to return
		int i = 0;
		
		while (tok.hasMoreTokens())
		{
			String token = tok.nextToken();	
			tokens[i] = token;
	    	i++;
		}

		boolean okToScramble = false;
		String selectedToken = "";
		int randomInteger = 0;
		Random rand = null;
		
	    while (!okToScramble)
	    {
			rand = new Random();
			// Random integer from from 0 to numOfTokensInString
		    randomInteger = rand.nextInt(numOfTokensInString);
		    selectedToken = tokens[randomInteger];
		    if (!ignored.contains(selectedToken) //not a common english word 
		    	&& !isInteger(selectedToken) 	 //not an integer
		    	//&& selectedToken.length() > 2
		    	)   //not a small word
		    	okToScramble = true;
	    }
		okToScramble = false;
		int excludedAlreadyScrambledTokenNumber = randomInteger; //exclude this token from further mangling
		String scrambledToken = insertRandomAsciiFlip (selectedToken, 2);
	    scrambledTokenFilename = noId_noExtension.replaceFirst(selectedToken, scrambledToken);
				    
	    while (!okToScramble)
	    {
			rand = new Random();
			// Random integer from from 0 to numOfTokensInString
		    randomInteger = rand.nextInt(numOfTokensInString);
		    selectedToken = tokens[randomInteger];
		    if (!ignored.contains(selectedToken) //not a common english word 
		    	&& !isInteger(selectedToken) 	 //not an integer
		    	//&& selectedToken.length() > 2	 //not a small word
		    	&& randomInteger != excludedAlreadyScrambledTokenNumber) //not already scrambled 
		    	okToScramble = true;
	    }
	    scrambledToken = insertRandomAsciiFlip (selectedToken, 2);
	    scrambledTokenFilename = scrambledTokenFilename.replaceFirst(selectedToken, scrambledToken);
		
	    rand = new Random();
	    randomInteger = rand.nextInt(122); // Random integer from from 0 to 125
	    String appendedNonSenseFilename = scrambledTokenFilename.concat("#" + randomInteger +" - " + GeorgeOrwell1984[randomInteger]);
	    
		finalFilename = whitespaceFreeFilename.concat(identifier + removeSpaces(appendedNonSenseFilename) + "." + extension);
		return finalFilename;		
	}
	
	
	// [JDK1.4] http://www.rgagnon.com/javadetails/java-0352.html
	public static String removeSpaces(String whitespacesIncluded)
	{
		StringTokenizer st = new StringTokenizer(whitespacesIncluded, " ", false);
		String whitespaceFree = "";
		while (st.hasMoreElements()) whitespaceFree += st.nextElement();
		return whitespaceFree;
	}
		
		
	public static String insertRandomAsciiFlip (String original, int numberOfFlips)
	{
		//System.out.println("Modified token : " + original);
		Random rand = new Random();
		Random rand2 = new Random();
		int randomInteger;
		int randomInteger2;
		char[] scrambledCharArray = original.toCharArray();
		
		for (int i = 0; i < numberOfFlips; i++)
		{
			randomInteger = rand.nextInt(original.length()); //random position
			randomInteger2 = 65 + rand2.nextInt(25); //result will be an integer ranging 65-90 (capital ascii characters) 
			scrambledCharArray[randomInteger] = (char)randomInteger2;
		}
		
		String ret = new String(scrambledCharArray);
		return ret;
	}
	
	
	public static String replace(String str, String oldToken, String newToken) 
	{
		//System.out.println("str: " + str);
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
    
        while ((e = str.indexOf(oldToken, s)) >= 0) 
        {
            result.append(str.substring(s, e));
            result.append(newToken);
            s = e + oldToken.length();
        }
        result.append(str.substring(s));
        
        String tmp = result.toString(); 
        	//System.out.println("str: " + tmp);
        return tmp;
    }
	
		
	public static boolean isInteger(String token)
	{
    	try
    	{
    		Integer.parseInt(token);
    		return true;
    	}
    	catch (NumberFormatException e)
    	{
    		// it wasn't
    		return false;
    	}
	}
	
}
