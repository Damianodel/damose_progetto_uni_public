package gestione_dati_gtfs_online;

public class BinaryToStringConverter {

    public static void main(String[] args) {
        // Esempio di stringa binaria
        String binaryString = "01101000 01100101 01101100 01101100 01101111";

        // Chiamata al metodo per convertire la stringa binaria in testo
        String text = binaryToString(binaryString);

        // Stampa del risultato
        System.out.println("Testo convertito: " + text);
    }

    /**
     * Questo metodo converte una stringa binaria in una stringa di testo.
     *
     * @param binaryString La stringa binaria da convertire.
     * @return La stringa di testo risultante.
     */
    public static String binaryToString(String binaryString) {
        // Dividi la stringa binaria in blocchi di 8 bit (1 byte)
        String[] binaryArray = binaryString.split(" ");

        // StringBuilder per costruire la stringa risultante
        StringBuilder result = new StringBuilder();

        // Iterazione su ogni blocco di 8 bit
        for (String binary : binaryArray) {
            // Conversione del blocco binario in un intero
            int decimal = Integer.parseInt(binary, 2);

            // Conversione dell'intero in un carattere e aggiunta al risultato
            char character = (char) decimal;
            result.append(character);
        }

        // Restituisce la stringa risultante
        return result.toString();
    }
}