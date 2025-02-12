import java.io.*;
import java.util.*;

enum KEYS_NAME {
    SG_UE,
    CD_CARGO,
    NR_CANDIDATO,
    NM_URNA_CANDIDATO,
    NR_PARTIDO,
    SG_PARTIDO,
    NR_FEDERACAO,
    DT_NASCIMENTO,
    CD_SIT_TOT_TURNO,
    CD_GENERO;

    public int idx() {
        return this.ordinal();
    }
}

public class CandidateFileReader implements CSVFileReader {
    private String[] keys;
    private List<String[]> values = new ArrayList<>();

    public static final int KEYS_NUM = 10;

    enum CandidateSituation {
        ELECTED,
        NOT_ELECTED;
    }

    @Override
    public void validateFilePath(String filePath) throws IOException {
        if (!filePath.endsWith(".csv"))
            throw new IOException(filePath + " is not a valid CSV file!");
    }

    private String[] readKeysCSV(File file) {
        String[] keys = null;

        try {
            Scanner scanner = new Scanner(file);

            if (scanner.hasNext())
                keys = scanner.nextLine().split(";");

            scanner.close();

            if (keys == null)
                throw new IOException("CSV File is empty!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keys;
    }

    private List<String[]> readValuesCSV(File file) {
        List<String[]> values = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file);

            // discards first line of csv
            if (scanner.hasNextLine())
                scanner.nextLine();

            while (scanner.hasNextLine())
                values.add(scanner.nextLine().split(";"));

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return values;
    }

    public CandidateFileReader(String filePath) throws IOException {
        validateFilePath(filePath);

        try {
            File file = new File(filePath);

            this.keys = readKeysCSV(file);

            this.values = readValuesCSV(file);
        } catch (Exception e) {
            e.printStackTrace();
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
}
