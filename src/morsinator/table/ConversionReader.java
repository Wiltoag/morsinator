package morsinator.table;

import java.io.*;

import morsinator.MorsinatorParseException;
import morsinator.collections.*;

public interface ConversionReader {
    /**
     * Remplis les collections de conversions à partir d'un flux textuel
     * 
     * @param reader text d'entrée d'entrée
     * @param tm     liste de conversion texte -> morse
     * @param mt     arbre de conversion morse -> texte
     * @throws IOException
     */
    public void fill(Reader reader, TextConversion tm, MorseConversion mt) throws MorsinatorParseException, IOException;
}
