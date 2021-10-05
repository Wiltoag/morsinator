package morsinator;

import morsinator.reader.*;
import morsinator.collections.*;
import morsinator.converter.*;
import morsinator.interfaces.MainWindowController;

import java.io.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
public class Morsinator extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("morsinator/interfaces/MainWindow.fxml"));
        fxmlLoader.setController(new MainWindowController(stage));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 500);
        
        stage.setTitle("Morsinator");
        stage.setMinHeight(400);
        stage.setMinWidth(500);
        stage.setScene(scene);
        //TODO add window icon
        //stage.getIcons().add(new Image("..."));
        stage.show();
    }

    private static void printHelpAndExit() {
            System.err.println("morsinator <option-conversion> <table-conversion> <fichier-entrée> <fichier-sortie>\n\n" +
                               "Options :\n" +
                               "    -tm  --texte-morse    Convertit de texte vers morse\n" +
                               "    -mt  --morse-texte    Convertit de morse vers texte");
            System.exit(1);
    }

    private static boolean parseOption(String[] args) {
        if(args[0].equals("-tm") || args[0].equals("--texte-morse")) {
            return false;
        } else if(args[0].equals("-mt") || args[0].equals("--morse-texte")) {
            return true;
        } else {
            System.err.println("L'option " + args[0] + " est inconnue");
            System.exit(1);
            return false;
        }
    }

    private static InputStream getConversionFileStream(String[] args) {
        try {
            return new FileInputStream(args[1]);
        } catch(FileNotFoundException exception) {
            System.err.println("Table de conversion introuvable");
            System.exit(1);
        }

        return null;
    }

    private static void getConversionCollections(String[] args, TextConversion textConversion, MorseConversion morseConversion) {
        InputStream conversionFile = getConversionFileStream(args);
        ConversionReader conversionReader = new TextualConversionReader();

        try {
            conversionReader.fill(new InputStreamReader(new BufferedInputStream(conversionFile)), textConversion,
                    morseConversion);
            conversionFile.close();
        } catch (MorsinatorParseException exception) {
            System.err.println("Erreur de lecture de la table de conversion\n" + args[1] + ":" + exception.getRow()
                    + " : " + exception.getMessage());
            System.exit(1);
        } catch (IOException exception) {
            System.err.println("Erreur de fermeture du fichier\n" + exception.getMessage());
            System.exit(1);
        }
    }

    private static InputStream openInputStream(String[] args) {
        try {
            return new FileInputStream(args[2]);
        } catch(FileNotFoundException exception) {
            System.err.println("Fichier d'entrée introuvable");
            System.exit(1);
            return null;
        }
    }

    private static OutputStream openOutputStream(String[] args) {
        try {
            return new FileOutputStream(args[3]);
        } catch(FileNotFoundException exception) {
            System.err.println("Fichier de sortie introuvable");
            System.exit(1);
            return null;
        }
    }

    private static Reader openReader(String[] args) {
        InputStream inputStream = openInputStream(args);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        return new InputStreamReader(bufferedInputStream);
    }

    private static Writer openWriter(String[] args) {
        OutputStream outputStream = openOutputStream(args);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        return new OutputStreamWriter(bufferedOutputStream);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            launch();
            return;
        } else if (args.length != 4) {
            printHelpAndExit();
        }

        boolean morseToText = parseOption(args);
        TextConversion textConversion = new ConversionList();
        MorseConversion morseConversion = new ConversionBinaryTree();

        getConversionCollections(args, textConversion, morseConversion);
        Reader reader = openReader(args);
        Writer writer = openWriter(args);

        MorseConverter morseConverter = new TextualMorseConverter();

        if(morseToText) {
            try {
                morseConverter.morseToText(reader, writer, morseConversion);
            } catch(MorsinatorParseException e) {
                System.err.println("Erreur de traduction du fichier morse\n" + args[2] + ":" + e.getRow() + " : " + e.getMessage());
                System.exit(1);
            }
        } else {
            try {
                morseConverter.textToMorse(reader, writer, textConversion);
            } catch(MorsinatorParseException e) {
                System.err.println("Erreur de traduction du fichier texte\n" + args[2] + ":" + e.getRow() + " : " + e.getMessage());
            }
        }

        try {
            writer.close();
        } catch(IOException e) {
            System.err.println("Erreur à la fermeture du flux d'écriture dans le fichier de sortie");
            System.exit(1);
        }
    }
}