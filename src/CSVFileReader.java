import java.io.IOException;
import java.util.*;

public interface CSVFileReader {
    public String[] getKeys();

    public List<String[]> getValues();

    public void validateFilePath(String filePath) throws IOException;
}