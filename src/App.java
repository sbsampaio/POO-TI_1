import java.io.PrintStream;

public class App {
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "ISO-8859-1"));

        CSVFileReader file = new CandidateFileReader("../csv/consulta_cand_2024_ES.csv");
        CSVFileReader file2 = new ElectionFileReader("../csv/votacao_secao_2024_ES.csv", 57053);
    }
}
