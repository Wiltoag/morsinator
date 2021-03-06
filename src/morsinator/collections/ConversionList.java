package morsinator.collections;

import java.util.Map.Entry;
import java.util.HashSet;
import java.util.Set;

import morsinator.collections.generics.MorsiList;
import morsinator.table.ConversionRow;

public class ConversionList extends MorsiList<ConversionRow> implements TextConversion {
    @Override
    public void addRow(char letter, String morse) {
        String oMorse;

        try {
            oMorse = getMorse(letter);
        } catch (IllegalArgumentException e) {
            add(new ConversionRow(letter, morse));
            return;
        }

        throw new IllegalArgumentException("Lettre '" + letter + "' déjà ajoutée sous le code morse \"" + oMorse + '"');
    }

    @Override
    public String getMorse(char letter) {
        letter = Character.toUpperCase(letter);

        for(Node<ConversionRow> node = getFirst(); node != null; node = node.getNext()) {
            ConversionRow row = node.getValue();

            if(row.letter == letter) {
                return row.morse;
            }
        }

        throw new IllegalArgumentException("La lettre " + letter + " n'a pas de traduction dans la table fournie");
    }

    @Override
    public Set<Entry<Character, String>> getRows() {
        HashSet<Entry<Character, String>> set = new HashSet<>();
        for (Node<ConversionRow> it = getFirst(); it != null; it = it.getNext()) {
            final Node<ConversionRow> current = it;
            set.add(new Entry<Character, String>() {

                @Override
                public Character getKey() {
                    return current.getValue().letter;
                }

                @Override
                public String getValue() {
                    return current.getValue().morse;
                }

                @Override
                public String setValue(String value) {
                    String old = current.getValue().morse;
                    current.setValue(new ConversionRow(current.getValue().letter, value));
                    return old;
                }

            });
        }
        return set;
    }

    @Override
    public void removeRow(char letter, String morse) {
        for (Node<ConversionRow> it = getFirst(); it != null; it = it.getNext()) {
            if (it.getValue().letter == letter) {
                remove(it);
                return;
            }
        }
    }
}