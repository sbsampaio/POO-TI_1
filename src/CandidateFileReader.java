import java.io.*;
import java.util.*;

enum KEYS_NAME {
    SG_UE, // codigo do municipio
    CD_CARGO, // codigo do cargo
    NR_CANDIDATO, // numero do candidato
    NM_URNA_CANDIDATO, // nome do candidato
    NR_PARTIDO, // numero do partido
    SG_PARTIDO, // sigla do partido
    NR_FEDERACAO, // numero da federacao (-1 para partido isolado)
    DT_NASCIMENTO, // data de nascimento
    CD_SIT_TOT_TURNO, // situacao do candidato no turno (2 ou 3 - eleito) (-1 - nao processar)
    CD_GENERO; // genero (2 - masculino, 4 - feminino)

    public int idx() {
        return this.ordinal();
    }
}

public class CandidateFileReader implements CSVFileReader {
    private String[] keys;
    private List<String[]> values = new ArrayList<>();

    public static final int KEYS_NUM = 10;

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

        // select the following columns: SG_UE, CD_CARGO, NR_CANDIDATO, NM_URNA_CANDIDATO, NR_PARTIDO,
        // SG_PARTIDO, NR_FEDERACAO, DT_NASCIMENTO, CD_SIT_TOT_TURNO, CD_GENERO
        String[] selectedKeys = {
                keys[11], keys[13], keys[16], keys[18], keys[25],
                keys[26], keys[28], keys[36], keys[48], keys[38]
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

                // select the following columns: SG_UE, CD_CARGO, NR_CANDIDATO, NM_URNA_CANDIDATO, NR_PARTIDO,
                // SG_PARTIDO, NR_FEDERACAO, DT_NASCIMENTO, CD_SIT_TOT_TURNO, CD_GENERO
                String[] selectedValues = {
                        row[11], row[13], row[16], row[18], row[25],
                        row[26], row[28], row[36], row[48], row[38]
                };

                // remove all the quotes (and double quotes) from the values
                for (int i = 0; i < KEYS_NUM; i++) {
                    selectedValues[i] = selectedValues[i].trim().replaceAll("^\"|\"$", "");
                }

                // only add the councilors that are in the actual city
                if (selectedValues[1].equals("13")) {
                    if (Integer.parseInt(selectedValues[0]) == cityCode){
                        values.add(selectedValues);

                //     if (selectedValues[2].length() == 5) {
                //         values.add(selectedValues);
                //         // System.out.println("CODIGO DO MUNICIPIO: " + selectedValues[0]);
                    }
                }
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Candidate file itens: " + values.size());
        return values;
    }

    public CandidateFileReader(String filePath, int cityCode) throws IOException {
        validateFilePath(filePath);

        try {
            File file = new File(filePath);

            this.keys = readKeysCSV(file);

            this.values = readValuesCSV(file, cityCode);
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
