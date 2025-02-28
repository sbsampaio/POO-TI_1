import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class App {
    public static void main(String[] args) throws Exception {
        int cityCode = Integer.parseInt(args[0]);
        String candidateFilePath = args[1];
        String electionFilePath = args[2];
        String electionDateString = args[3];

        // // Exibe os parâmetros recebidos
        // System.out.println("Código do Município: " + cityCode);
        // System.out.println("Arquivo de Candidatos: " + candidateFilePath);
        // System.out.println("Arquivo de Votação: " + electionFilePath);
        // System.out.println("Data da Eleição: " + electionDateString);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate electionDate = LocalDate.parse(electionDateString, formatter);

        CandidateFileReader file = new CandidateFileReader(candidateFilePath,
                cityCode);
        ElectionFileReader file2 = new ElectionFileReader(electionFilePath,
                cityCode);
        ElectionReport report = new ElectionReport(file, file2, candidateFilePath,
                electionDate);
    }
}
