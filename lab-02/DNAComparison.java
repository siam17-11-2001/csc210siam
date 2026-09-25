import java.util.ArrayList;
import java.util.Map;

public class DNAComparison {
    private static final Map<String, String> AMINO = buildCodonTable();

    private static Map<String, String> buildCodonTable() {
        String[] groups = {
            "TTT TTC", "TTA TTG CTT CTC CTA CTG", "ATT ATC ATA", "ATG",
            "GTT GTC GTA GTG", "TCT TCC TCA TCG AGT AGC", "CCT CCC CCA CCG",
            "ACT ACC ACA ACG", "GCT GCC GCA GCG", "TAT TAC", "TAA TAG TGA",
            "CAT CAC", "CAA CAG", "AAT AAC", "AAA AAG", "GAT GAC",
            "GAA GAG", "TGT TGC", "TGG", "CGT CGC CGA CGG AGA AGG",
            "GGT GGC GGA GGG"
        };
        String[] acids = {"F", "L", "I", "M", "V", "S", "P", "T", "A", "Y",
                          "Stop", "H", "Q", "N", "K", "D", "E", "C", "W", "R", "G"};
        Map<String, String> table = new java.util.HashMap<>();
        for (int i = 0; i < groups.length; i++) {
            for (String codon : groups[i].split(" ")) table.put(codon, acids[i]);
        }
        return table;
    }

    public static ArrayList<String> dnaToCodons(String dna) {
        if (dna == null || dna.length() % 3 != 0) {
            throw new IllegalArgumentException("DNA length must be a multiple of 3");
        }
        ArrayList<String> codons = new ArrayList<>();
        for (int i = 0; i < dna.length(); i += 3) codons.add(dna.substring(i, i + 3));
        return codons;
    }

    public static String codonToAminoAcid(String codon) {
        String aminoAcid = AMINO.get(codon);
        if (aminoAcid == null) throw new IllegalArgumentException("Invalid codon: " + codon);
        return aminoAcid;
    }

    public static ArrayList<String> dnaToAminoAcids(String dna) {
        ArrayList<String> result = new ArrayList<>();
        for (String codon : dnaToCodons(dna)) result.add(codonToAminoAcid(codon));
        return result;
    }

    public static boolean isMatch(ArrayList<String> aminoSeq1, ArrayList<String> aminoSeq2) {
        return aminoSeq1.equals(aminoSeq2);
    }

    public static void main(String[] args) {
        String DNA1 = "CTGATATTGTATCCGGCCGAT";
        String DNA2 = "CTAGCCGGTGGTTATTAATAGTAAACTATTCCA";
        String DNA3 = "TTAATCCTCTACCCCGCAGAC";
        ArrayList<String> first = dnaToAminoAcids(DNA1);
        ArrayList<String> second = dnaToAminoAcids(DNA2);
        ArrayList<String> third = dnaToAminoAcids(DNA3);
        System.out.println("DNA1 amino acids: " + first);
        System.out.println("DNA2 amino acids: " + second);
        System.out.println("DNA3 amino acids: " + third);
        System.out.println("DNA1 and DNA2 identical: " + isMatch(first, second));
        System.out.println("DNA1 and DNA3 identical: " + isMatch(first, third));
        System.out.println("DNA2 and DNA3 identical: " + isMatch(second, third));
    }
}
