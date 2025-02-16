import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ElectionFileReader implements CSVFileReader {
    private String[] keys;
    private List<String[]> values = new ArrayList<>();
    private LocalDate electionDate;

    @Override
    public void validateFilePath(String filePath) throws IOException {
        if (!filePath.endsWith(".csv"))
            throw new IOException(filePath + " is not a valid CSV file!");
    }

    private String[] readKeysCSV(File file) {
        String[] keys = null;

        try {
            Scanner scanner = new Scanner(file, "ISO-8859-1");

            if (scanner.hasNext())
                keys = scanner.nextLine().split(";");

            scanner.close();

            if (keys == null)
                throw new IOException("CSV File is empty!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // select the following columns: CD_CARGO, CD_MUNICIPIO, NR_VOTAVEL, QT_VOTOS
        String[] selectedKeys = {
                keys[17], keys[13], keys[19], keys[21]
        };

        return selectedKeys;
    }

    private List<String[]> readValuesCSV(File file, int cityCode) {
        List<String[]> values = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file, "ISO-8859-1");

            // discards first line of csv
            if (scanner.hasNextLine())
                scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] row = scanner.nextLine().split(";");

                // select the following columns: CD_CARGO, CD_MUNICIPIO, NR_VOTAVEL, QT_VOTOS
                String[] selectedValues = {
                        row[17], row[13], row[19], row[21]
                };

                // only add the councilors that are in the actual city
                int candidateNumber = Integer.parseInt(selectedValues[2]);
                if (selectedValues[0].equals("13") && selectedValues[1].equals(String.valueOf(cityCode))) {
                    if (candidateNumber != 95 && candidateNumber != 96 && candidateNumber != 97 && candidateNumber != 98) { // null or blank votes
                        values.add(selectedValues);
                    // System.out.println("CODIGO DO MUNICIPIO: " + selectedValues[1]);
                    }
                    // System.out.println("Values: " + Arrays.toString(selectedValues));
                }

            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return values;
    }

    public ElectionFileReader(String filePath, int cityCode) throws IOException {
        validateFilePath(filePath);

        try {
            File file = new File(filePath);

            this.electionDate = getElectionDate(file);

            this.keys = readKeysCSV(file);

            this.values = readValuesCSV(file, cityCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ComputeVotes(HashMap<Integer, Candidate> candidates,
            HashMap<Integer, PoliticalParty> politicalParties) {

        for (String[] value : values) { 
            int candidateNumber = Integer.parseInt(value[2]);
            int newVotes = Integer.parseInt(value[3]);

            if (value[2].length() == 5) { // nominal vote

                Candidate actualCandidate = candidates.get(candidateNumber);

                actualCandidate.incCandidateVotes(newVotes);

                PoliticalParty actualParty = politicalParties.get(actualCandidate.getPartyNumber());
                actualParty.addNominalVotes(newVotes);
            } else {
                System.out.println("voto de legenda: " + candidateNumber);
                PoliticalParty actualParty = politicalParties.get(candidateNumber);
                actualParty.addLegendVotes(newVotes);

            }

        }
    }

    public static LocalDate getElectionDate(File file) throws IOException {
        try {
            Scanner scanner = new Scanner(file, "ISO-8859-1");

            // discards first line of csv
            if (scanner.hasNextLine())
                scanner.nextLine();

            String[] row = scanner.nextLine().split(";");

            // select the election date
            String dateStr = row[0].replaceAll("^\"|\"$", "");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            scanner.close();

            return LocalDate.parse(dateStr, formatter);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String[] getKeys() {
        return this.keys.clone();
    }

    @Override
    public List<String[]> getValues() {
        return new ArrayList<String[]>(this.values);
    }

    public LocalDate getElectionDate() {
        return this.electionDate;
    }
}