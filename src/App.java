import java.io.PrintStream;

public class App {
    public static void main(String[] args) throws Exception {
        // System.setOut(new PrintStream(System.out, true, "ISO-8859-1"));

        CandidateFileReader file = new CandidateFileReader("/home/lhrscopel/Documentos/poo/POO-TI_1/csv/consulta_cand_2024_ES.csv", 57053);
        ElectionFileReader file2 = new ElectionFileReader("/home/lhrscopel/Documentos/poo/POO-TI_1/csv/votacao_secao_2024_ES.csv", 57053);
        ElectionReport report = new ElectionReport(file, file2);
    }
}
